def call() {
    pipeline {
        agent any

        environment {
		    STAGE = ''
		}

        parameters {
		    choice choices: ['gradle', 'maven'], description: 'Indicar la herramienta de construccion.', name: 'buildTool'
		    string(name: 'Stage', defaultValue: '', description: 'Ingresar los Stages de ejecucion')
		}

        options {
            timestamps()
        }

        stages {
            stage('Pipeline') {
                steps {
                    script {
                        println 'Pipeline'
                            
                        if(verifyBranchName() == 'main'){
                            figlet 'No se permite ejecutar desde main'
                        } else {
                            if (params.buildTool == 'gradle'){
                                gradle(verifyBranchName())
                            } else {
                                def ejecucion = load 'maven.groovy'
                                maven(verifyBranchName())
                            }
                        }
                    }
                }
            }
		}

        post {
            always {
                slackSend channel: '#jenkins-ci', color: 'normal', message: "${username}" + ' ha ejecutado un Pipeline.', teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'slack-token'
                slackSend channel: '#jenkins-ci', color: 'normal', message: 'Job Name: ' + env.JOB_NAME + ', BuildTool: ' +  params.buildTool + '.', teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'slack-token'
            }
            success{
                slackSend channel: '#jenkins-ci', color: '#29AE4A', message: 'Ejecucion exitosa de ' + verifyBranchName() + '. Se han ejecutado los siguientes Stages: ' + params.Stage + '.', teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'slack-token'
            }
            failure {
                slackSend channel: '#jenkins-ci', color: '#EC4D34', message: 'Ejecucion Fallida en Stage: ' + "${STAGE}" + '.', teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'slack-token'
            }
        }
	}

}

def verifyBranchName(){
	if (env.GIT_BRANCH.contains('feature-') || env.GIT_BRANCH.contains('develop')){
		return 'CI'
	} else {
        if (env.GIT_BRANCH.contains('main')){
            return 'main'
        } else {
		    return 'CD'
	    }
    }
}

return this;

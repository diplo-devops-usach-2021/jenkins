import cl.devops.*

def call() {

    def USUARIO
    def BRANCH

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
                        cleanWs()
                        wrap([$class: 'BuildUser']) {
                            USUARIO = env.BUILD_USER_FIRST_NAME + " " + env.BUILD_USER_LAST_NAME
                        }                        
                        println "Pipeline iniciado por ${USUARIO}"
                        Git git =  new Git()
                        TIPO = git.verifyBranchName()
                        println "El tipo de ejecucion es: ${TIPO} ${params.buildTool}"                  
                        if(TIPO == 'OTRO'){
                            currentBuild.result = 'FAILURE'
                            error("No se puede ejecutar la rama MAIN")
                        }
                        if (params.buildTool.equals("gradle")){
                            println "Selecciono GRADLE"
                            def tool = load 'gradle.groovy'
                            tool.gradle(TIPO)
                        } else {
                            println "Selecciono MAVEN"
                            def ejecucion = load 'maven.groovy'
                            tool.maven(TIPO)
                        }
                        
                    }
                }
            }
		}

        /*post {
            always {
                slackSend channel: '#jenkins-ci', color: 'normal', message: "${USUARIO}" + ' ha ejecutado un Pipeline.', teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'slack-token'
                slackSend channel: '#jenkins-ci', color: 'normal', message: 'Job Name: ' + env.JOB_NAME + ', BuildTool: ' +  params.buildTool + '.', teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'slack-token'
            }
            success{
                slackSend channel: '#jenkins-ci', color: '#29AE4A', message: 'Ejecucion exitosa de ' + TIPO + '. Se han ejecutado los siguientes Stages: ' + params.Stage + '.', teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'slack-token'
            }
            failure {
                slackSend channel: '#jenkins-ci', color: '#EC4D34', message: 'Ejecucion Fallida en Stage: ' + "${STAGE}" + '.', teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'slack-token'
            }
        }*/
	}

}
return this;

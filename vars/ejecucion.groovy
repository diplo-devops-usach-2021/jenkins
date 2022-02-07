import cl.devops.*

def call() {

    def USUARIO
    def TIPO

    pipeline {
        agent any

        environment {
		    STAGE = ''
		}

        parameters {
		    choice choices: ['gradle', 'maven'], description: 'Indicar la herramienta de construccion.', name: 'buildTool'
		    string(name: 'Stage', defaultValue: 'compile unitTest jar sonar nexusUpload gitCreateRelease', description: 'Ingresar los Stages de ejecucion')
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

                        if(params.Stage.contains("gitCreateRelease") && "${env.BRANCH_NAME}" != "develop"){
                            currentBuild.result = 'FAILURE'
                            error("El stage ${params.Stage} no esta disponible para otras ramas que no sea la DEVELOP")
                        }
                        TIPO = git.verifyBranchName()
                        println "El tipo de ejecucion es: ${TIPO} ${params.buildTool}"                  
                        if(TIPO == 'OTRO'){
                            currentBuild.result = 'FAILURE'
                            error("No se puede ejecutar la rama MAIN")
                        }
                        if (params.buildTool.equals("gradle")){
                            println "Selecciono GRADLE"
                            gradle(TIPO)
                        } else {
                            println "Selecciono MAVEN"
                            maven(TIPO)
                        }
                        
                    }
                }
            }
		}

        post {
            success{
                slackSend channel: '#jenkins-ci', color: '#29AE4A', message: "[Grupo5][Pipeline ${TIPO}][Rama: ${env.BRANCH_NAME}][Stage: ${params.Stage}][Resultado: Ok].", teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'slack-token'
            }
            failure {
                slackSend channel: '#jenkins-ci', color: '#EC4D34', message: "[Grupo5][Pipeline ${TIPO}][Rama: ${env.BRANCH_NAME}][Stage: ${params.Stage}][Resultado: No Ok].", teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'slack-token'
            }
        }
	}

}
return this;

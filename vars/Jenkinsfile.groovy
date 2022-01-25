def call(){
pipeline {
            agent any            
            stages {
                stage('Iniciando...') {
                    steps {
                        script {
                            sh 'ls'
                        }
                    }
                }
            }
        }
}

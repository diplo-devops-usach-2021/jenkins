def call() {
    pipeline {
            agent any
            options {
                timestamps()
            }
            stages{
                stage("Compile"){        
                    steps{
                        script{
                            sh 'ls -lh'
                            sh 'pwd'
                            sh 'who am i'
                            sh 'date'
                        }
                    }
                }
            }
    }
}

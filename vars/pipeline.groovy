def call() {
    pipeline {
            agent any
            options {
                timestamps()
            }
            stage("Compile"){        
                sh 'ls -lh'
                sh 'pwd'
                sh 'who am i'
                sh 'date'
            }
    }
}

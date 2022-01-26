def call() {
    stage("Compile"){        
        sh 'ls -lh'
        sh 'pwd'
        sh 'who am i'
        sh 'date'
    }
}

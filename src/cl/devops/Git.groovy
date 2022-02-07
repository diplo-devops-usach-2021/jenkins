package cl.devops


def obtieneRamaActual(){
    def stdout = bat(returnStdout:true , script: 'git rev-parse --abbrev-ref HEAD').trim()
    def result = stdout.readLines().drop(1).join(" ")    
    return result
}

def merge(String ramaOrigen, String ramaDestino){
    println "Realizando merge desde ${ramaOrigen} a ${ramaDestino}"
    sh """
    git remote update
    git fetch 
    git checkout --track origin/main
    """
    bat(returnStdout:false , script: "git fetch --all")
    println "Paso 1"
    bat(returnStdout:false , script: "git checkout master")
    println "Paso 2"
    bat(returnStdout:false , script: "git merge ${ramaOrigen}")
    println "Paso 3"
}

def tag(String version, String descripcion){
    println "Realizando Tag: ${version} descripcion:  ${descripcion}"
    bat(returnStdout:false , script: "git pull origin main")
    bat(returnStdout:false , script: "git checkout ${ramaDestino}")
    bat(returnStdout:false , script: "git tag -a ${version} -m '${descripcion}'")
}
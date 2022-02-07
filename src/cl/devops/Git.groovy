package cl.devops


def merge(String ramaOrigen, String ramaDestino){
    println "Realizando merge desde ${ramaOrigen} a ${ramaDestino}"
    sh """
    git remote update
    git fetch --all
    """
    println "Paso 1"
    sh(returnStdout:false , script: "git checkout master")
    println "Paso 2"
    sh(returnStdout:false , script: "git merge ${ramaOrigen}")
    println "Paso 3"
}

def tag(String version, String descripcion){
    println "Realizando Tag: ${version} descripcion:  ${descripcion}"
    sh(returnStdout:false , script: "git pull origin main")
    sh(returnStdout:false , script: "git checkout ${ramaDestino}")
    sh(returnStdout:false , script: "git tag -a ${version} -m '${descripcion}'")
}
package cl.devops


def merge(String ramaOrigen, String ramaDestino){
    println "Realizando merge desde ${ramaOrigen} a ${ramaDestino}"
    sh """
        git fetch --all
        git branch -v -a
        git switch ${ramaDestino}
        git merge ${ramaOrigen}
    """
}

def tag(String version, String descripcion){
    println "Realizando Tag: ${version} descripcion:  ${descripcion}"
    sh(returnStdout:false , script: "git pull origin main")
    sh(returnStdout:false , script: "git checkout ${ramaDestino}")
    sh(returnStdout:false , script: "git tag -a ${version} -m '${descripcion}'")
}
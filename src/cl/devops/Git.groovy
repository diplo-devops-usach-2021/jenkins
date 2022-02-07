package cl.devops


def merge(String ramaOrigen, String ramaDestino){
    println "Realizando merge desde ${ramaOrigen} a ${ramaDestino}"
    sh """
        git checkout ${ramaDestino}
        git merge ${ramaOrigen}
    """
}

def tag(String version, String descripcion){
    println "Realizando Tag: ${version} descripcion:  ${descripcion}"
    sh """
        git checkout ${ramaDestino}
        git tag -a ${version} -m '${descripcion}'
    """
}
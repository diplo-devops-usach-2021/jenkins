package cl.devops


static def obtieneRamaActual(){
    def ramaActual = bat returnStdout: true, script: 'git rev-parse --abbrev-ref HEAD'
    result = ramaActual.readLines().drop(1).join(" ")    
    return result
}

static def merge(String ramaOrigen, String ramaDestino){
    println "Realizando merge ${ramaOrigen} y ${ramaDestino}"
    bat """
        git checkout ${ramaDestino} 
        git merge ${ramaOrigen}
    """
}

static def tag(String version, String descripcion){
    println "Realizando Tag: ${version} descripcion:  ${descripcion}"
    bat """
        git checkout main
        git tag -a ${version} -m "${descripcion}"
    """
}
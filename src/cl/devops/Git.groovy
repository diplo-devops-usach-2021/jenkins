package cl.devops


def obtieneRamaActual(){
    def stdout = bat(returnStdout:true , script: 'git rev-parse --abbrev-ref HEAD').trim()
    def result = stdout.readLines().drop(0).join(" ")
    println result
    println stdout     
    return result
}

def merge(String ramaOrigen, String ramaDestino){
    println "Realizando merge ${ramaOrigen} y ${ramaDestino}"
    bat """
        git checkout ${ramaDestino} 
        git merge ${ramaOrigen}
    """
}

def tag(String version, String descripcion){
    println "Realizando Tag: ${version} descripcion:  ${descripcion}"
    bat """
        git checkout main
        git tag -a ${version} -m "${descripcion}"
    """
}
package cl.devops


def obtieneRamaActual(){
    def stdout = bat(returnStdout:true , script: 'git branch --show-current').trim()
    println stdout   
    def result = stdout.readLines().drop(1).join(" ")
    println result      
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
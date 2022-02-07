package cl.devops


def obtieneRamaActual(){
    def stdout = bat(returnStdout:true , script: 'git rev-parse --abbrev-ref HEAD').trim()
    println stdout   
    def result = stdout.readLines().drop(1).join(" ")
    println result      
    return result
}

def merge(String ramaOrigen, String ramaDestino){
    println "Realizando merge ${ramaOrigen} y ${ramaDestino}"
    bat(returnStdout:true , script: 'git fetch')
    bat(returnStdout:true , script: 'git checkout ${ramaDestino}'
    bat(returnStdout:true , script: 'git merge ${ramaOrigen}'
}

def tag(String version, String descripcion){
    println "Realizando Tag: ${version} descripcion:  ${descripcion}"
    bat """
        git fetch
        git checkout main
        git tag -a ${version} -m "${descripcion}"
    """
}
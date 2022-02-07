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
    bat(returnStdout:false , script: "git pull origin ${ramaDestino}")
    bat(returnStdout:false , script: "git checkout ${ramaDestino}")
    bat(returnStdout:false , script: "git merge ${ramaOrigen}")
}

def tag(String version, String descripcion){
    println "Realizando Tag: ${version} descripcion:  ${descripcion}"
    bat(returnStdout:false , script: "git pull origin main")
    bat(returnStdout:false , script: "git checkout ${ramaDestino}")
    bat(returnStdout:false , script: "git tag -a ${version} -m '${descripcion}'")
}
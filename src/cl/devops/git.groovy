package cl.devops


String obtieneRamaActual(){
    String ramaActual = sh(
				script: "git rev-parse --abbrev-ref HEAD",
				returnStdout: true
            )
    return ramaActual
}

def merge(String ramaOrigen, String ramaDestino){
    println "Realizando merge ${ramaOrigen} y ${ramaDestino}"
    sh '''
        git checkout ${ramaDestino} 
        git merge ${ramaOrigen}
    '''
}

def tag(String version, String descripcion){
    println "Realizando Tag: ${version} descripcion:  ${descripcion}"
    sh '''
        git checkout main
        git tag -a ${version} -m "${descripcion}"
    '''
}
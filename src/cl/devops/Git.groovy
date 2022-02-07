package cl.devops


String obtieneRamaActual(){
    String ramaActual = bat(
				script: "git rev-parse --abbrev-ref HEAD",
				returnStdout: true
            )
    return ramaActual.toString()
}

def merge(String ramaOrigen, String ramaDestino){
    println "Realizando merge ${ramaOrigen} y ${ramaDestino}"
    bat '''
        git checkout ${ramaDestino} 
        git merge ${ramaOrigen}
    '''
}

def tag(String version, String descripcion){
    println "Realizando Tag: ${version} descripcion:  ${descripcion}"
    bat '''
        git checkout main
        git tag -a ${version} -m "${descripcion}"
    '''
}
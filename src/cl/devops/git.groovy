package cl.devops

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
        git tag -a ${version} -m "${descripcion}"
    '''
}
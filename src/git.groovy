
def merge(String ramaOrigen, String ramaDestino){
    println "Este método realiza un merge ${ramaOrigen} y ${ramaDestino}"
    sh '''
        git checkout ${ramaDestino} 
        git merge ${ramaOrigen}
    '''
}

def tag(String version, String descripcion){
    println "realizando Tag: ${version} descripcion:  ${descripcion}"
    sh '''
        git tag -a ${version} -m "${descripcion}"
    '''
}
package cl.devops


static def obtieneRamaActual(){
    def ramaActual = bat returnStdout: true, script: 'git rev-parse --abbrev-ref HEAD'
    for (String key : ramaActual.keySet())
    {
        System.out.println("Key: " + key);   
        for(String str : h.get(key))
        {
        System.out.println("\t" +str);
        }
    }
    return ramaActual
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
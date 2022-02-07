package cl.devops


def merge(String ramaOrigen, String ramaDestino){
    println "Realizando merge desde ${ramaOrigen} a ${ramaDestino}"
    git branch: "${ramaDestino}", credentialsId: 'github-user', url: 'https://github.com/diplo-devops-usach-2021/ms-iclab.git'
    sh """
        git merge origin/${ramaOrigen}
    """
}

def tag(String version, String descripcion){
    println "Realizando Tag: ${version} descripcion:  ${descripcion}"
    sh """
        git checkout ${ramaDestino}
        git tag -a ${version} -m '${descripcion}'
    """
}
package cl.devops


def merge(String ramaOrigen, String ramaDestino){
    println "Realizando merge desde ${ramaOrigen} a ${ramaDestino}"
    git branch: "${ramaDestino}", credentialsId: 'github-user', url: 'https://github.com/diplo-devops-usach-2021/ms-iclab.git'
    sh """
        git config --global user.email "jgarciam@gmail.com"
        git config --global user.name "Jorge Garcia"
        git merge origin/${ramaOrigen}
    """
}

def tag(String version, String descripcion){
    println "Realizando Tag: ${version} descripcion:  ${descripcion}"
    git branch: "main", credentialsId: 'github-user', url: 'https://github.com/diplo-devops-usach-2021/ms-iclab.git'
    sh "git tag -a \'${version}\' -m \'${descripcion}\'"
    withCredentials([usernamePassword(credentialsId: 'github-user', passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
                        sh('git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com:diplodevops/ms-iclab.git')
                    }
}
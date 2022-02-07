package cl.devops


def merge(String ramaOrigen, String ramaDestino){
    println "Realizando merge desde ${ramaOrigen} a ${ramaDestino}"    
    obtenerRama(ramaDestino)
    withCredentials([usernamePassword(credentialsId: 'github-user', passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
        sh """          
            git merge origin/${ramaOrigen}
            git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/diplo-devops-usach-2021/ms-iclab.git
        """
    }
}

def tag(String version, String descripcion){
    println "Realizando Tag: ${version} descripcion:  ${descripcion}"
    obtenerRama("main")
    withCredentials([usernamePassword(credentialsId: 'github-user', passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
        sh """
            git tag -a \'${version}\' -m \'${descripcion}\'
            git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/diplo-devops-usach-2021/ms-iclab.git --tags
        """
    }
}

def verifyBranchName(){
	if (env.GIT_BRANCH.contains('feature-') || env.GIT_BRANCH.contains('develop')){
		return 'CI'
	} else {
        if (env.GIT_BRANCH.contains('main')){
            return 'OTRO'
        } else {
		    return 'CD'
	    }
    }
}

def obtenerRama(String rama){
    println "Obteniendo rama ${rama}"
    git branch: "${rama}", credentialsId: 'github-user', url: 'https://github.com/diplo-devops-usach-2021/ms-iclab.git'
}
package cl.devops


def merge(String ramaOrigen, String ramaDestino){
    println "Realizando merge desde ${ramaOrigen} a ${ramaDestino}"
    git branch: "${ramaDestino}", credentialsId: 'github-user', url: 'https://github.com/diplo-devops-usach-2021/ms-iclab.git'
    withCredentials([usernamePassword(credentialsId: 'github-user', passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
        sh """          
            git merge origin/${ramaOrigen}
            git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/diplo-devops-usach-2021/ms-iclab.git
        """
    }
}

def tag(String version, String descripcion){
    println "Realizando Tag: ${version} descripcion:  ${descripcion}"
    git branch: "main", credentialsId: 'github-user', url: 'https://github.com/diplo-devops-usach-2021/ms-iclab.git'    
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
            return 'main'
        } else {
		    return 'CD'
	    }
    }
}
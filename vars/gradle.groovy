/*
	forma de invocación de método call:
	def ejecucion = load 'script.groovy'
	ejecucion.call()
*/
import cl.devops.*

def call(String pipelineType){
	figlet 'Gradle'
	if (pipelineType == 'CI') {
		figlet 'Integracion Continua'
		if (params.Stage.contains('build')) {
		  stage('Compile & Unit Test & Package'){
				bat "gradle clean build"
			}
		} 
		if(params.Stage.contains('sonar')){
			stage('sonar') {
		        def scannerHome = tool 'SonarScanner';
				def nombre = "${env.JOB_NAME}-${env.BUILD_NUMBER}".replace("/","-")
				withSonarQubeEnv('My SonarQube Server') {  
					sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=${nombre} -Dsonar.java.binaries=build/classes"
				}
			}
		}
		if(params.Stage.contains('nexusUpload')){
			stage('nexusUpload') {
				nexusArtifactUploader artifacts: [[artifactId: "${pom.artifactId}", file: "build/${pom.artifactId}-${pom.version}.jar", type: 'jar']], credentialsId: 'nexus', groupId: "${pom.groupId}", nexusUrl: 'nexus:8081', nexusVersion: 'nexus3', protocol: 'http', repository: 'test-nexus', version: "${pom.version}"
			}
		}
		if("${env.BRANCH_NAME}" == "develop" && params.Stage.contains('gitCreateRelease')){
			stage('gitCreateRelease') {					
				git.crearRama("release-v${pom.version}")
			}
		}			
	} else {
		figlet 'Delivery Continuo'
		stage("gitDiff"){
			sh "git diff"
		}
		stage("nexusDownload"){
			def groupId = pom.groupId.replace(".","/")
			sh "curl -X GET -u admin:yakarta123. http://nexus:8081/repository/test-nexus/${groupId}/${pom.artifactId}/${pom.version}/${pom.artifactId}-${pom.version}.jar -O"
		}
		stage('run'){
			sh "nohup gradle bootRun &"
			sleep(10)
		}
		stage("test"){
			sh "curl -X GET 'http://jenkins:8082/rest/mscovid/test?msg=testing'"
		}
		stage("gitMergeMaster"){
			def ramaOrigen = "${env.BRANCH_NAME}"		
			git.merge(ramaOrigen, "main")
		}
		stage("gitMergeDevelop"){
			def ramaOrigen = "${env.BRANCH_NAME}"		
			git.merge(ramaOrigen, "develop")
		}
		stage("gitTagMaster"){
			git.tag("${pom.version}","Nuevo tag generado desde Jenknins")
		}
	  }
	}
}

return this;
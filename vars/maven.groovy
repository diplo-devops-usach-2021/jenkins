/*
	forma de invocación de método call:
	def ejecucion = load 'script.groovy'
	ejecucion.call()
*/
import cl.devops.*

def call(String pipelineType){
	figlet 'Maven'	
	Git git = new Git()
	git.obtenerRama("${env.BRANCH_NAME}")
	def pom = readMavenPom()
	if (pipelineType == 'CI') {
		figlet 'Integracion Continua'
		if (params.Stage.contains('compile')) {
			stage('compile') {
		        sh "./mvnw clean compile -e"
		    }
		}
		if(params.Stage.contains('unitTest')){
			stage('unitTest') {
		        sh "./mvnw clean test -e"
		    }
		}
		if(params.Stage.contains('jar')){    
		    stage('jar') {			
		        sh "./mvnw clean package -e -DskipTests"
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
		if(params.Stage.contains('gitDiff')){
			stage("gitDiff"){

			}
		}
		if(params.Stage.contains('nexusDownload')){
			stage("nexusDownload"){
				def groupId = pom.groupId.replace(".","/")
				sh "curl -X GET -u admin:yakarta123. http://nexus:8081/repository/test-nexus/${groupId}/${pom.artifactId}/1.0.0/${pom.artifactId}-1.0.0.jar -O"

			}
		}
		if(params.Stage.contains('run')){
			stage("run"){
				sh "chmod +X ${pom.artifactId}-1.0.0.jar"
				sh "nohup java -jar ${pom.artifactId}-1.0.0.jar"
				sleep(20)
			}
		}
		if(params.Stage.contains('test')){
			stage("test"){
				sh "curl -X GET 'http://localhost:8082/rest/mscovid/test?msg=testing'"
			}
		}

		/*stage('Tareas SCM') {
			String ramaOrigen = obtieneRamaActual()			
			git.merge(ramaOrigen, "main")
			git.merge(ramaOrigen, "develop")
			def pom = readMavenPom()
			git.tag("${pom.version}","Nuevo tag generado desde Jenknins")
		}*/

	}
} 
return this;
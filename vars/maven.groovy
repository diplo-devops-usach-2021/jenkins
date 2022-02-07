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
				withSonarQubeEnv('My SonarQube Server') {  
					sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=ejemplo-maven -Dsonar.java.binaries=build/classes"
				}
			}
		}
		if(params.Stage.contains('nexusUpload')){
			stage('nexusUpload') {
				nexusArtifactUploader artifacts: [[artifactId: "${pom.artifactId}", file: "build/${pom.artifactId}-${pom.version}.jar", type: 'jar']], credentialsId: 'nexus', groupId: "${pom.groupId}", nexusUrl: 'nexus:8081', nexusVersion: 'nexus3', protocol: 'http', repository: 'test-nexus', version: "${pom.version}"
			}
		}
		if("${env.BRANCH_NAME}".equals("develop")){
			if(params.Stage.contains('gitCreateRelease')){
				stage('gitCreateRelease') {					
					git.crearRama("release-v${pom.version}")
				}
			}
		}
	} /*else {
		figlet 'Delivery Continuo'
		if(params.Stage.contains('run')){		
		    stage('Run') {
				STAGE = env.STAGE_NAME
				figlet "${STAGE}"
		        bat "start /min mvn spring-boot:run &"
		    }
		} else { println 'No ha especificado ejecutar el Stage: RUN' }

		if(params.Stage.contains('sonar')){
			stage('Sonar') {
				STAGE = env.STAGE_NAME
				figlet "${STAGE}"
		        def scannerHome = tool 'sonar-scanner';
		        withSonarQubeEnv('sonar-server') {
		        bat "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=ejemplo-maven-developer -Dsonar.sources=src -Dsonar.java.binaries=build"
					}
			}
		} else { println 'No ha especificado ejecutar el Stage: SONAR' } 

		if(params.Stage.contains('testApp')){		
		    stage('Test Applications') {
				STAGE = env.STAGE_NAME
				figlet "${STAGE}"
		        bat "start chrome http://localhost:8081/rest/mscovid/test?msg=testing"
			}
		} else { println 'No ha especificado ejecutar el Stage: Test Applications' }

		stage('Tareas SCM') {
			String ramaOrigen = obtieneRamaActual()			
			git.merge(ramaOrigen, "main")
			git.merge(ramaOrigen, "develop")
			def pom = readMavenPom()
			git.tag("${pom.version}","Nuevo tag generado desde Jenknins")
		}
	}*/
} 
return this;
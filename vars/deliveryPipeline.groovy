#!/usr/bin/env groovy

def call(Map param){
	pipeline {
		agent {
			label "${param.agentName}"
		}
		stages {
			stage ("Build VM-APP") {
				when { expression { return "${param.agentName}" == 'masterworker'} }
				steps {
					sh 'mvn -B -DskipTests clean package'
				}
			}
			stage ("Test VM-APP") {
				when { expression { return "${param.agentName}" == 'masterworker'} }
				steps {
					sh 'mvn test'
				}
				post {
					always {
						junit 'target/surefire-reports/*.xml'
					}
				}
			}
			stage('Run VM-APP') {
				when { expression { return "${param.agentName}" == 'masterworker'} }
				steps {
					sh 'java -jar target/*.jar'
				}
			}
			stage('Build DOCKER-APP') {
				when { expression { return "${param.agentName}" == 'dockerworker'} }
				steps {
					sh 'mvn -B -DskipTests clean package'
				}
			}
			stage('Test DOCKER-APP') {
				when { expression { return "${param.agentName}" == 'dockerworker'} }
				steps {
					sh 'mvn test'
				}
				post {
					always {
						junit 'target/surefire-reports/*.xml'
					}
				}
			}
			stage('Build image for DOCKER-APP') {
				when { expression { return "${param.agentName}" == 'dockerworker'} }
				steps {
					sh 'docker build -t my-docker-app .'
				}
			}
			stage('Run app DOCKER-APP') {
				when { expression { return "${param.agentName}" == 'dockerworker'} }
				steps {
					sh 'docker run -p 8080:8181 my-docker-app'
				}
			}
		}
		post {
			always {
				deleteDir()
			}
    	}
    }
}
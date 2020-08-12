#!/usr/bin/env groovy

def call(Map param){
	def agentName = "dockerworker"
	pipeline {
		agent {
			label "$agentName"
		}
		stages {
			stage('Build') {
				steps {
					sh 'mvn -B -DskipTests clean package'
				}
			}
			stage('Test') {
				steps {
					sh 'mvn test'
				}
				post {
					always {
						junit 'target/surefire-reports/*.xml'
					}
				}
			}
		}
		post {
        always {
            telegramSend 'Hello World'
        }
    }
	}
}

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
            telegram()
        }
    }
	}
}

def telegram (){
    def commiter = sh(script: "git show -s --pretty=%cn",returnStdout: true).trim()
	telegramSend "${commiter}"
}
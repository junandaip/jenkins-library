#!/usr/bin/env groovy

def call(Map param){

	def agent = 'dockerworker'
	pipeline {
		agent {
			node{
				label "${agent}"
			}
		}
		options {
            ansiColor('xterm')
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

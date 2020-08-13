#!/usr/bin/env groovy


def call(Map param){

	def commiter = getCommiter()
	pipeline {
		agent {
			label "dockerworker"
		}
		stages {
			stage ("telegram notif"){
				steps{
					telegramSend "$commiter"
				}
			}
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

    }
}

def getCommiter (){
	//def commiter = sh(script: "git show -s --pretty=%cn",returnStdout: true).trim()
	return "Eric"
}
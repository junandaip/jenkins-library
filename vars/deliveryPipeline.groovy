#!/usr/bin/env groovy

def call(Map param){
	def agentName = "dockerworker"
	def commiter = getCommiter()
	pipeline {
		agent {
			label "$agentName"
		}
		stages {
			stage ('telegram nootif') {
				telegramSend "$commiter deploy app"
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
		post {
        	failure{
            	telegramSend 'deployment Fail'
        	}
        	success {
	            telegramSend 'deployment Success'
    	    }
    	}
    }
}

def getCommiter (){
	return sh(script: "git show -s --pretty=%cn",returnStdout: true).trim()
}
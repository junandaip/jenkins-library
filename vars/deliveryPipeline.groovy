#!/usr/bin/env groovy

def call(Map param){
	def agentName = "dockerworker"
	def commiter = getCommiter()

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
        	failure{
				script {
            		telegramSend 'deployment Fail'
				}
        	}
        	success {
				script {
	            	telegramSend 'deployment Success'
				}
    	    }
    	}
    }
}

def getCommiter (){
	return sh(script: "git show -s --pretty=%cn",returnStdout: true).trim()
}
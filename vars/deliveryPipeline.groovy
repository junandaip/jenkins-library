#!/usr/bin/env groovy

def call(Map param){
	pipeline {
		agent {
			label "${param.agentName}"
			// label "masterworker"
		}
		stages {
			stage ("Build VM-APP") {

			}
			stage ("telegram notif") {
				steps{
					echo "${getMessage()} ${param.text}"
				}
			}
			stage('Build VM-APP') {
				steps {
					sh 'mvn -B -DskipTests clean package'
				}
			}
			stage('Test VM-APP') {
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

def getMessage (){
	def commiter = sh(script: "git show -s --pretty=%cn",returnStdout: true).trim()
	def message = "$commiter deploying app"
	return message
}

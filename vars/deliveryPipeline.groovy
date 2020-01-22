#!/usr/bin/env groovy

def call(Map param){
	pipeline {
		agent {
			node{
				label 'slave2'
			}
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
			stage('Deliver') {
				steps {
					sh "sh jenkins/scripts/deliver.sh ${server}"
				}
			}
		}
	}
}

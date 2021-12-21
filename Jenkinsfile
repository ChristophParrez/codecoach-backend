pipeline {
    agent any
        tools {
            jdk 'jdk-17'
            maven 'Maven'
        }

    stages {
        stage('Get Git') {
            steps {
                checkout scm
            }
        }
        stage('Build 🏗') {
            steps {
                sh 'mvn clean test'
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
        }
    }
}
pipeline {
    agent any

      environment {
            DOCKER_IMAGE = 'nipa93/temperature-converter'
        }

    stages {
        stage('Checkout') {
            steps {
                 git branch: 'main',
                    url:'https://github.com/NipaDelara/OTP1_inclass1_assignment_Delara.git'
            }
        }
        stage('Build') {
            steps {
                bat 'mvn clean install'
            }
        }
        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }
        stage('Code Coverage') {
            steps {
                bat 'mvn jacoco:report'
            }
        }
        stage('Publish Test Results') {
            steps {
                junit '**/target/surefire-reports/*.xml'
            }
        }
        stage('Publish Coverage Report') {
            steps {
                jacoco()
            }
        }
         stage('Build Docker Image') {
                    steps {
                        bat 'docker build -t %DOCKER_IMAGE%:latest .'
                    }
                }
         stage('Docker Hub Login') {
                    steps {
                        withCredentials([
                            usernamePassword(
                                credentialsId: 'dockerhub-credentials',
                                usernameVariable: 'DOCKER_USER',
                                passwordVariable: 'DOCKER_PASSWORD'
                            )
                        ]) {
                             bat '''
                             @echo off
                             echo %DOCKER_PASSWORD%| docker login -u %DOCKER_USER% --password-stdin
                             '''
                        }
                    }
                }

                stage('Push Docker Image') {
                    steps {
                        bat 'docker push %DOCKER_IMAGE%:latest'
                    }
                }
    }
}
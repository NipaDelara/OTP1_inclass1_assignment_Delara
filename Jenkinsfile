
pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'nipa93/temperature-converter'
        PATH = "/opt/homebrew/bin:/opt/homebrew/sbin:/usr/local/bin:${env.PATH}"
    }

    stages {

        //Checkout project from GitHub
        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/NipaDelara/OTP1_inclass1_assignment_Delara.git'
            }
        }

        //Build project and run JUnit tests using MariaDB
        stage('Build and Test') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'mariadb-credentials',
                        usernameVariable: 'DB_USER',
                        passwordVariable: 'DB_PASSWORD'
                    )
                ]) {
                    withEnv(['DB_HOST=127.0.0.1']) {
                        sh 'mvn clean verify'
                    }
                }
            }
        }

        //Publish JUnit test results
        stage('Publish Test Results') {
            steps {
                junit '**/target/surefire-reports/*.xml'
            }
        }

        //Archive JaCoCo coverage report
        stage('Archive JaCoCo Report') {
            steps {
                archiveArtifacts artifacts: 'target/site/jacoco/**',
                                 allowEmptyArchive: false
            }
        }

        //Build Docker image for Mac
        stage('Build Docker Image') {
            steps {
                sh 'docker build --platform linux/amd64 -t $DOCKER_IMAGE:latest .'
            }
        }

        // Log in to Docker Hub securely
        stage('Docker Hub Login') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASSWORD'
                    )
                ]) {
                    sh '''
                        echo "$DOCKER_PASSWORD" | docker login \
                        -u "$DOCKER_USER" --password-stdin
                    '''
                }
            }
        }

        // Step 7: Push Docker image to Docker Hub
        stage('Push Docker Image') {
            steps {
                sh 'docker push $DOCKER_IMAGE:latest'
            }
        }
    }

    post {
        always {
            // Publish test results even if build fails
            junit testResults: '**/target/surefire-reports/*.xml',
                  allowEmptyResults: true

            // Archive JaCoCo report if available
            archiveArtifacts artifacts: 'target/site/jacoco/**',
                             allowEmptyArchive: true
        }

        success {
            echo 'Pipeline completed successfully.'
        }

        failure {
            echo 'Pipeline failed. Check Jenkins console output.'
        }
    }
}

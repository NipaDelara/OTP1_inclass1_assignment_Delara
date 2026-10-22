pipeline {
    agent any

 environment {
     DOCKER_IMAGE = 'nipa93/temperature-converter'
     PATH+HOMEBREW = '/opt/homebrew/bin:/opt/homebrew/sbin'
 }
    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/NipaDelara/OTP1_inclass1_assignment_Delara.git'
            }
        }

        stage('Build and Test') {
            steps {
                sh 'mvn clean verify'
            }
        }

        stage('Publish Test Results') {
            steps {
                junit '**/target/surefire-reports/*.xml'
            }
        }

        stage('Archive JaCoCo Report') {
            steps {
                archiveArtifacts artifacts: 'target/site/jacoco/**',
                                 allowEmptyArchive: false
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build --platform linux/amd64 -t $DOCKER_IMAGE:latest .'
            }
        }

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

        stage('Push Docker Image') {
            steps {
                sh 'docker push $DOCKER_IMAGE:latest'
            }
        }
    }

    post {
        always {
            junit testResults: '**/target/surefire-reports/*.xml',
                  allowEmptyResults: true

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


pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'nipa93/temperature-converter'
        PATH = "/opt/homebrew/bin:/opt/homebrew/sbin:/usr/local/bin:${env.PATH}"
        DB_HOST = '127.0.0.1'
    }

    stages {

        // Step 1: Checkout project from GitHub
        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/NipaDelara/OTP1_inclass1_assignment_Delara.git'
            }
        }

        // Step 2: Check Java, Maven and Docker
        stage('Check Tools') {
            steps {
                sh '''
                    java -version
                    mvn -version
                    docker --version
                '''
            }
        }

        // Step 3: Build and test with MariaDB
        stage('Build and Test') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'mariadb-credentials',
                        usernameVariable: 'DB_USER',
                        passwordVariable: 'DB_PASSWORD'
                    )
                ]) {
                    sh '''
                        mvn -B clean verify
                    '''
                }
            }
        }

        // Step 4: Publish JUnit results
        stage('Publish Test Results') {
            steps {
                junit testResults: '**/target/surefire-reports/*.xml',
                      allowEmptyResults: false
            }
        }

        // Step 5: Archive JaCoCo coverage report
        stage('Archive JaCoCo Report') {
            steps {
                archiveArtifacts(
                    artifacts: 'target/site/jacoco/**',
                    allowEmptyArchive: false
                )
            }
        }

        // Step 6: Archive Maven JAR file
        stage('Archive JAR') {
            steps {
                archiveArtifacts(
                    artifacts: 'target/*.jar',
                    allowEmptyArchive: false,
                    fingerprint: true
                )
            }
        }

        // Step 7: Build Docker image
        stage('Build Docker Image') {
            steps {
                sh '''
                    docker build --platform linux/amd64 \
                        -t "$DOCKER_IMAGE:latest" .
                '''
            }
        }

        // Step 8: Push image to Docker Hub
        stage('Push Docker Image') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASSWORD'
                    )
                ]) {
                    sh '''
                        set +x

                        echo "$DOCKER_PASSWORD" | \
                            docker login \
                            -u "$DOCKER_USER" \
                            --password-stdin

                        docker push "$DOCKER_IMAGE:latest"
                    '''
                }
            }
        }
    }

    post {
        always {
            // Publish available test reports
            junit testResults: '**/target/surefire-reports/*.xml',
                  allowEmptyResults: true

            // Archive coverage report if available
            archiveArtifacts(
                artifacts: 'target/site/jacoco/**',
                allowEmptyArchive: true
            )
        }

        success {
            echo 'Pipeline completed successfully.'
        }

        failure {
            echo 'Pipeline failed. Check Jenkins console output.'
        }
    }
}

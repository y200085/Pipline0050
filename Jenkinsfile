pipeline {
    agent any

    environment {
        DEV_PATH = '/var/jenkins_home/app/dev'
        PROD_PATH = '/var/jenkins_home/app/prod'
        DB_HOST = 'mysql-server'
    }

    stages {
        stage('Checkout Source Code') {
            steps {
                echo "Checking out repository on branch: ${env.BRANCH_NAME}"
                checkout scm
            }
        }

        stage('Sequential Build & Package') {
            steps {
                echo 'Building Maven project...'
                sh 'mvn clean package -DskipTests'
                stash name: 'compiled-artifact', includes: 'target/*.jar'
            }
        }

        stage('Parallel Quality & Test Gates') {
            parallel {
                stage('Unit & Integration Tests') {
                    steps {
                        echo 'Running JUnit test suites...'
                        sh 'mvn test'
                    }
                }
                stage('Static Code Analysis') {
                    steps {
                        echo 'Executing code linter and vulnerability checks...'
                        sh 'mvn verify -DskipTests'
                    }
                }
            }
        }

        stage('Load External Deployment Script') {
            steps {
                script {
                    // Loaded as a local workflow variable (prevents String casting errors)
                    deployHelper = load 'scripts/deploy-helper.groovy'
                }
            }
        }

        stage('Deploy to Development') {
            when {
                expression { env.BRANCH_NAME == 'development' }
            }
            steps {
                unstash 'compiled-artifact'
                script {
                    deployHelper.deployToEnvironment('Development', env.DEV_PATH)
                }
            }
        }

        stage('Deploy to Production') {
            when {
                expression { env.BRANCH_NAME == 'production' }
            }
            stages {
                stage('Production Approval Gate') {
                    steps {
                        timeout(time: 5, unit: 'DAYS') {
                            input(
                                message: "Approve deployment to PRODUCTION on branch ${env.BRANCH_NAME}?",
                                ok: 'Approve & Deploy',
                                submitter: 'admin,release-manager'
                            )
                        }
                    }
                }
                stage('Execute Production Deployment') {
                    steps {
                        unstash 'compiled-artifact'
                        script {
                            deployHelper.deployToEnvironment('Production', env.PROD_PATH)
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            cleanWs()
            echo 'Pipeline execution finished.'
        }
        success {
            echo 'Pipeline passed successfully!'
        }
        failure {
            echo 'Pipeline failed. Please review the build logs.'
        }
    }
}
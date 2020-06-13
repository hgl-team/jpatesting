def skipRemainingStages = false

pipeline{
    agent any
    stages{
        stage("Build artifact"){
            steps{
                echo "======== Building artifact"
                sh 'mvn clean compile package -U -DskipTests=true'
            }
            post{
                success{
                    echo "======== Build artifact executed successfully"
                }
                failure{
                    echo "======== Build artifact execution failed"
                }
            }
        }

        stage("Test artifact"){
            steps{
                echo "======== Testing artifact"
                sh 'mvn test'
            }
            post{
                success{
                    echo "======== Testing artifact executed successfully"
                }
                failure{
                    echo "======== Testing artifact execution failed"
                }
            }
        }

        stage("Deploying artifact"){
            steps{
                echo "======== Deploying artifact"
                sh 'mvn deploy'
            }
            post{
                success{
                    echo "======== Build artifact executed successfully"
                }
                failure{
                    echo "======== Build artifact execution failed"
                }
            }
        }
    }
    post{
        always {
            sh 'mvn clean'
        }
        success{
            echo "======== pipeline executed successfully"
        }
        failure{
            echo "======== pipeline execution failed"
        }
    }
}


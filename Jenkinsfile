def skipRemainingStages = false
def repositoryUrl = 'https://dev.lujociclas.com/gitbucket/git/hgl/jpatesting.git'
def repositoryCredentialId = 'jenkins-gitbucket'
def webhookToken = 'VhQqs8gsmMhTm2cjXwDwEDIwsxu7AUZn'
def canContinue = 1
def mvnProfile = ''

pipeline {
    agent any
    triggers {
        GenericTrigger(
            genericVariables: [
                [key: 'pushbranch', value: '$.ref', defaultValue: 'none'],
                [key: 'pullbranch', value: '$.pull_request.base.ref', defaultValue: 'none'],
                [key: 'action', value: '$.action', defaultValue: 'none'],
                [key: 'merged', value: '$.pull_request.merged', defaultValue: 'none'],
                [key: 'pullTitle', value: '$.pull_request.title', defaultValue: 'none'],
                [key: 'senderLogin', value: '$.sender.login']
            ],
            genericHeaderVariables: [
                [key: 'x-github-event']
            ],
            causeString: 'Triggered by $$x_github_event event on ${pushbranch}${pullbranch}',
            token: "${webhookToken}",
            printContributedVariables: true,
            printPostContent: true,
            silentResponse: false,
            regexpFilterText: '$x_github_event - ${pushbranch} or ${pullbranch} - ${merged} - ${action}',
            regexpFilterExpression: '(push|pull_request) - (refs/heads/develop|refs/heads/master|none) or (develop|master|none) - (true|none) - (closed|none)?'
        )
    }
    stages {
        stage('checkout branch') {
            when { equals expected: 1, actual: canContinue }
            steps {
                script {
                    echo "Checking out branch..."
                    if("${x_github_event}" == 'push') {
                        checkout([$class: 'GitSCM',
                            branches: [[name: "${pushbranch}"]],
                            userRemoteConfigs: [[url: "${repositoryUrl}", credentialsId: "${repositoryCredentialId}"]]])

                        if("${pushbranch}" == 'refs/heads/master') {
                            mvnProfile = 'production'
                        } else {
                            mvnProfile = 'develop'
                        }
                    } else if("${x_github_event}" == 'pull_request'){
                        git credentialsId: "${repositoryCredentialId}" , branch: "${pullbranch}", url: "${repositoryUrl}"

                        if("${pullbranch}" == 'master') {
                            mvnProfile = 'production'
                        } else {
                            mvnProfile = 'develop'
                        }
                    }
                    echo "Selected profile ${mvnProfile}"
                }
            }
            post{
                success{
                    echo "======== Checkout artifact executed successfully"
                }
                failure{
                    echo "======== Checkout artifact execution failed"
                    script {
                        canContinue = 0
                    }
                }
            }
        }
        stage("Build artifact"){
            when { equals expected: 1, actual: canContinue }
            steps{
                echo "======== Building artifact"
                sh "mvn dependency:purge-local-repository clean compile package -U -DskipTests=true -P ${mvnProfile},springboot"
            }
            post{
                success{
                    echo "======== Build artifact executed successfully"
                }
                failure{
                    echo "======== Build artifact execution failed"
                    script {
                        canContinue = 0
                    }
                }
            }
        }
        stage("Test artifact"){
            when { equals expected: 1, actual: canContinue }
            steps{
                echo "======== Testing artifact"
                sh "mvn test -P ${mvnProfile},springboot"
            }
            post{
                success{
                    echo "======== Testing artifact executed successfully"
                }
                failure{
                    echo "======== Testing artifact execution failed"
                    script {
                        canContinue = 0
                    }
                }
            }
        }
        stage("Deploying artifact"){
            when { equals expected: 1, actual: canContinue }
            steps{
                echo "======== Deploying artifact"
                sh "mvn verify deploy -P ${mvnProfile},springboot,docker -DskipTests=true"
            }
            post{
                success{
                    echo "======== Build artifact executed successfully"
                }
                failure{
                    echo "======== Build artifact execution failed"
                    script {
                        canContinue = 0
                    }
                }
            }
        }
    }
    post{
        always {
            sh 'mvn clean'
            emailext body: "Title: ${pullTitle}\nBranch: ${pushbranch}${pullbranch}\nJob: ${env.JOB_NAME}\nBuild ${env.BUILD_NUMBER}\nSender: ${senderLogin}\nStatus: ${currentBuild.currentResult}\nMore info at: ${env.BUILD_URL}",
                                        recipientProviders: [[$class: 'DevelopersRecipientProvider'], [$class: 'RequesterRecipientProvider']],
                                        subject: "Jenkins Build - Job ${env.JOB_NAME} ${currentBuild.currentResult}"
        }
        success{
            echo "======== pipeline executed successfully"
        }
        failure{
            echo "======== pipeline execution failed"
            emailext body: "Title: ${pullTitle}\nBranch: ${pushbranch}${pullbranch}\nJob: ${env.JOB_NAME}\nBuild ${env.BUILD_NUMBER}\nSender: ${senderLogin}\nStatus: ${currentBuild.currentResult}\nMore info at: ${env.BUILD_URL}",
                                        recipientProviders: [[$class: 'DevelopersRecipientProvider'], [$class: 'RequesterRecipientProvider']],
                                        subject: "Jenkins Build - Job ${env.JOB_NAME} ${currentBuild.currentResult}"
        }
    }
}
pipeline {
    agent any

    tools {
        gradle 'Gradle-8.14.2'
     }

    stages {
        stage('Package & Dockerize Services') {
            steps {
                script {
                    def services = ['account','blocker','cash','exchange','exchange-rate','front','notification','transfer']

                    services.each { svc ->
                        dir(svc) {
                            sh 'chmod +x gradlew'

                            sh './gradlew clean build -x test'

                            sh "docker build -t ${svc}:0.0.1-SNAPSHOT ."

                            sh "minikube image load ${svc}:0.0.1-SNAPSHOT"
                        }
                    }

                    dir('keycloak') {
                        sh 'docker build -t keycloak .'
                        sh 'minikube image load keycloak'
                    }
                }
            }
        }

        stage('Install Helm Chart') {
            steps {
                sh '''
                  helm dependency update ./k8s
                  helm upgrade --install myapp ./k8s
                '''
            }
        }
    }
}

pipeline {
    agent any

    environment {
        GITHUB_ORG = 'todo-microservice'
        CONTAINER_REG = "ghcr.io/${GITHUB_ORG}/"
        ARTIFACT_ID = readMavenPom().getArtifactId()
        JAR_NAME = "${ARTIFACT_ID}-${BUILD_NUMBER}"
        IMAGE_NAME = "${CONTAINER_REG}${ARTIFACT_ID}"
    }

    stages {
        stage("Build Application") {
            steps {
                sh 'echo Performing maven build: ${ARTIFACT_ID}'
            }
        }

        stage("Build Container Image") {
            steps {
                sh 'echo Building container image: ${IMAGE_NAME}'
            }
        }
        
        stage("Publish Container Image") {
            steps {
                sh 'echo Publishing container image: ${CONTAINER_REG}'
            }
        }
    }
}
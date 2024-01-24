pipeline {
    agent any 

    stages {
        stage('haha') {
            steps {
                echo "hello world"
                echo "sleep 30s"
                sh "sleep 30"
            }
        }

        stage('reset') {
            steps {
                echo "I want to have a rest"
                echo "sleep 40s"
                sh "sleep 40"
            }
        }
    }
}
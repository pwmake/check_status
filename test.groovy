
// def getRepoURL() {
//     sh "git config --get remote.origin.url > .git/remote-url"
//     return readFile(".git/remote-url").trim()
// }


// def getCommitSha() {
//     sh "git rev-parse HEAD > .git/current-commit"
//     return readFile(".git/current-commit").trim()    
// }


// def updateGithubCommitStatus(build) {
//     repoUrl = getRepoURL()
//     commitSha = getCommitSha()

//     step([
//         $class: 'GitHubCommitStatusSetter',
//         reposSource: [$class: "ManullyEnteredRepositorySource", url: repoUrl],
//         errorHandlers: [[$class: 'ShallowAnyErrorHandler']],
//         statusResultSource: [
//             $class: 'ConditionalStatusResultSource',
//             results: [
//                 [$class: 'BetterThanOrEqualBuildResult', result: 'SUCCESS', status: 'SUCCESS', message: build.descript],
//                 [$class: 'BetterThanOrEqualBuildResult', result: 'FAILURE', status: 'FAILURE', message: build.descript],
//                 [$class: 'AnyBuildResult', status: 'FAILURE', message: 'Loophole'],
//             ]
//         ]
//     ])
// }

node {

    stage('initialise') {
        deleteDir()
    }

    stage('scm-checkout') {
        checkout([
            $class: 'GitSCM',
            branches: scm.branches,
            doGenerateSubmoduleConfigurations: scm.doGenerateSubmoduleConfigurations,
            extensions: scm.extensions,
            userRemoteConfigs: scm.userRemoteConfigs
        ])
    }

    stage('build_sth') {
        withCredentials([usernamePassword(credentialsId: "gh-jk-public-read-only-token", passwordVariable: 'GH_TOKEN', usernameVariable: 'GH_USER')]){
            // echo "usage: bash $0 $1[git_user] $2[git_pass] $3[jk_job] $4[build_num] $5[state] $6[description]"
            sh(returnStdout: true, script: "bash -xe call_gh_api.sh $GH_USER $GH_TOKEN $JOB_BASE_NAME $BUILD_NUMBER 'pending' 'waitting to pass check' ")
            sh(returnStdout: true, script: "echo This is a testing")
            sh(returnStdout: true, script: "echo sleep 20")
            sh(returnStdout: true, script: "sleep 20")
        }
    }

    stage('update status') {
        withCredentials([usernamePassword(credentialsId: "gh-jk-public-read-only-token", passwordVariable: 'GH_TOKEN', usernameVariable: 'GH_USER')]){

            sh(returnStdout: true, script: "bash -xe call_gh_api.sh $GH_USER $GH_TOKEN $JOB_BASE_NAME $BUILD_NUMBER 'success' 'passed the config check' ")
        }
    }

}
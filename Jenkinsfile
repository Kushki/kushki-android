 
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import groovy.json.JsonSlurper;

 node {
 
    def projectName =  determineRepoName()
    def projectGit = "Kushki/${projectName}"
    def ENVIRONMENT = ""
    def PULL_REQUEST = env.BUILD_ID
    def CHANGE_BRANCH = env.BRANCH_NAME
    def CHANGE_TARGET = ("${env.BRANCH_NAME}".contains("release")||"${env.BRANCH_NAME}".contains("hotfix")) ? "master" : "release";

    sh "echo ${projectName} - ${projectGit}"
       if ("${CHANGE_TARGET}".contains("release")) {
            ENVIRONMENT = "ci"
        }
        if ("${CHANGE_TARGET}".contains("master")) {
            ENVIRONMENT = "qa"
        }
  
   stage('Pull') {
             sh("printenv")
             git branch: "${CHANGE_BRANCH}", url: "git@github.com:${projectGit}.git"
             sh "git rev-parse HEAD > .git/commit-id"
             commit_id = readFile('.git/commit-id')
             commit_id = commit_id.split("\n")[0]
             sh "aws ssm get-parameters --names /${ENVIRONMENT}/${projectName}/RUNSCOPE_SUITE_TEST --region us-east-1 --query \"Parameters[0].Value\" > .tmp"
             RUNSCOPE_SUITE_TEST  = readFile('.tmp')
             RUNSCOPE_SUITE_TEST = RUNSCOPE_SUITE_TEST.split("\n")[0]
             sh "rm -f .tmp || true"
             sh "aws ssm get-parameters --names /${ENVIRONMENT}/${projectName}/RUNSCOPE_ENV --region us-east-1 --query \"Parameters[0].Value\" > .tmp"
             RUNSCOPE_ENV  = readFile('.tmp')
             RUNSCOPE_ENV = RUNSCOPE_ENV.split("\n")[0]
             sh "git show --format='%ae' $commit_id  > .git/commit-author"
             GIT_AUTHOR_EMAIL  = readFile('.git/commit-author')
             GIT_AUTHOR_EMAIL = GIT_AUTHOR_EMAIL.split("\n")[0]
             
        }
    stage('Lint & Unit Test') {
      withCredentials([ string(credentialsId: 'awsUser', variable: 'awsUser')]) {
          awsCodeBuild artifactLocationOverride: 'artifact-build', artifactNameOverride: "${commit_id}.zip", artifactNamespaceOverride: "NONE", artifactPackagingOverride: 'ZIP', artifactPathOverride: "ci/${projectName}/PR${PULL_REQUEST}/", artifactTypeOverride: 'S3',  awsAccessKey: 'awsUser', awsSecretKey: "$awsUser", buildSpecFile: '', buildTimeoutOverride: '45', credentialsId: 'awsCodeBuildCredentialOK', credentialsType: 'jenkins',  envVariables: "[ { USRV_STAGE, ci }, {USRV_COMMIT, $commit_id}, {USRV_BRANCH,$CHANGE_BRANCH} ]", gitCloneDepthOverride: '', projectName: 'usrv_back_android_build', proxyHost: '', proxyPort: '', region: 'us-east-1', sourceControlType: 'jenkins', sourceVersion: '', sseAlgorithm: 'AES256'
          }
    }
    stage('Deploy') {
      steps {
        script {                                                        
          if (currentBuild.result == null         
              || currentBuild.result == 'SUCCESS') {  
             if(env.BRANCH_NAME ==~ /master/) {
               // Deploy when the committed branch is master (we use fastlane to complete this)     
               sh 'fastlane app_deploy'
          }
        }
      }
    }
}
}

String determineRepoName() {
    return "${env.JOB_NAME}".tokenize('/')[1]
}



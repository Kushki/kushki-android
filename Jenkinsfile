  node {
  
   stage('Pull') {
              sh "echo hola"
             
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

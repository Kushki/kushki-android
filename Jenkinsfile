  node {
  
   stage('Pull') {
              sh "echo hola"
             
        }
    stage('Lint & Unit Test') {
      sh './gradlew --no-search-upward --build-file kushki-android/build.gradle clean unitTest'
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

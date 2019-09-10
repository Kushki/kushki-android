pipeline {
  agent { 
    node { label 'android' }                     
  }
  stages {                                       
    stage('Lint & Unit Test') {
      parallel {                                 
        stage('checkStyle') {
          steps {
            // We use checkstyle gradle plugin to perform this
            sh './gradlew --no-search-upward --build-file kushki-android/build.gradle clean unitTest'
          }
        }

        stage('Unit Test') {
          steps {
            // Execute your Unit Test
            sh './gradlew --no-search-upward --build-file kushki-android/build.gradle clean integrationTest'
          }
        }
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
  post {                                           
    always {
      archiveArtifacts(allowEmptyArchive: true, artifacts: 'app/build/outputs/apk/production/release/*.apk')
      // And kill the emulator?
      sh 'adb emu kill'
    }
  }
}
#!/usr/bin/env bash
set -e

source pipeline/initialize-android.sh
artifact_version=$(./gradlew --quiet lib:printVersion)
git tag --annotate "v$artifact_version" -m "Release for version $artifact_version"
git push --tags
./gradlew lib:clean lib:bintrayUpload

# ENVIRONMENT VARIABLES:
# STAGE
#  BINTRAY_USER
#  BINTRAY_API_KEY
#  MAVEN_CENTRAL_USER
#  MAVEN_CENTRAL_TOKEN

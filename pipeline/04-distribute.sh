#!/usr/bin/env bash
set -e

source pipeline/initialize-android.sh
artifact_version=$(./gradlew --quiet kushki-android:printVersion)
git tag --annotate "v$artifact_version" -m "Release for version $artifact_version"
git push --tags
./gradlew kushki-android:clean kushki-android:bintrayUpload

# ENVIRONMENT VARIABLES:
# STAGE
#  BINTRAY_USER
#  BINTRAY_API_KEY
#  MAVEN_CENTRAL_USER
#  MAVEN_CENTRAL_TOKEN

#!/usr/bin/env bash
set -e

build_file=kushki-android/build.gradle
artifact_version=$(./gradlew --quiet --no-search-upward --build-file $build_file printVersion)
git tag --annotate "v$artifact_version" -m "Release for version $artifact_version"
git push --tags
./gradlew --no-search-upward --build-file $build_file clean bintrayUpload

# ENVIRONMENT VARIABLES:
# STAGE
#  BINTRAY_USER
#  BINTRAY_API_KEY
#  MAVEN_CENTRAL_USER
#  MAVEN_CENTRAL_TOKEN

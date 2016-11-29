#!/usr/bin/env bash
set -e

./gradlew --no-search-upward --build-file kushki-android/build.gradle clean integrationTest

#!/usr/bin/env bash
set -e

source pipeline/initialize-android.sh
./gradlew clean testReleaseUnitTest

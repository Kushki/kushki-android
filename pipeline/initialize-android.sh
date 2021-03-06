#!/bin/bash

# source: https://docs.snap-ci.com/the-ci-environment/languages/android/
set -e

INITIALIZATION_FILE="$ANDROID_HOME/.initialized-dependencies-$(git log -n 1 --format=%h -- $0)"

if [ ! -e ${INITIALIZATION_FILE} ]; then
  download-android
  echo y | android update sdk --no-ui --filter tools,platform-tools
  echo y | android update sdk --no-ui --filter build-tools-22.0.1 --all
  echo y | android update sdk --no-ui --filter android-22
  echo y | android update sdk --no-ui --filter extra-google-m2repository --all
  echo y | android update sdk --no-ui --filter extra-android-m2repository --all
  touch ${INITIALIZATION_FILE}
fi
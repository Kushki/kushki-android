#!/usr/bin/env bash
set -e

source pipeline/initialize-android.sh
./gradlew kushki-android:clean kushki-android:test

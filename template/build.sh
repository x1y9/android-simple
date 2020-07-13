#!/bin/sh

if [ $# -eq 0 ]; then
  echo 'build script for android project'
  echo ' '
  echo 'Build:   build [debug|release|publish] [flavor]'
  echo 'Clean:   build clean'
  echo 'Version: build version [number]'  
  exit 1
fi

FLAVOR=${2:-std}

if [ "$1" = "debug" ]; then
  ./gradlew assemble${FLAVOR}Debug
fi

if [ "$1" = "release" ]; then
  ./gradlew assemble${FLAVOR}Release
fi

if [ "$1" = "publish" ]; then
  echo 'check uncommit fils...'
  if ! git diff-files --quiet; then
    echo  some file uncommit
    exit 2
  fi
  echo 'do release build'
  if ! ./gradlew clean assemble${FLAVOR}Release; then
    echo build fail
    exit 3
  fi
  PACKAGE=`grep -m 1 -oP "applicationId\s+.\K([a-zA-Z0-9.]+)" app/build.gradle`
  VERSION=`grep -oP "versionName\s+.\K([0-9.]+)" app/build.gradle`
  HASH=`git rev-parse --short HEAD`
  echo publish to $PACKAGE-$VERSION-$HASH-$FLAVOR.apk
  cp app/build/outputs/apk/$FLAVOR/release/app-$FLAVOR-release.apk $PACKAGE-$VERSION-$HASH-$FLAVOR.apk

fi

if [ "$1" = "clean" ]; then
  ./gradlew clean
fi


if [ "$1" = "version" ]; then
  if [ "$2" = "" ]; then
    grep -oP 'versionName "\K([0-9.]+)' app/build.gradle
  else
    sed -i -E "s/versionName \"(.*?)\"/versionName \"$2\"/" app/build.gradle
    sed -i -E "s/versionCode ([0-9.]+)/versionCode $2/" app/build.gradle
    sed -i -E "/versionCode/s/\.([0-9][0-9])/\1/g" app/build.gradle
    sed -i -E "/versionCode/s/\./0/g" app/build.gradle
  fi
fi




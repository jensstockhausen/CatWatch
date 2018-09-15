#!/bin/bash
BASEDIR=$(dirname "$0")
echo "$BASEDIR"
cd $BASEDIR

logger CATWATCHER: starting application

cd ./application

java -Dlogfilepath=./../log/activity.log -jar CatWatch-1.0.0.jar >> catwatch.log




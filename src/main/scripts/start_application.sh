#!/bin/bash
cd "${dirname "$0"}"

logger CATWATCHER: starting application

cd ./application

java -Dlogfilepath=./../log/activity.log -jar CatWatch-1.0.0.jar >> catwatch.log




#!/bin/bash

cd "$(dirname "$0")"
mkdir ./log

logger CATWATCHER: starting activity logger

python start_log_activity.sh >> ./log/activity.log

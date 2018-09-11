#!/bin/bash
cd "${dirname "$0"}"

mkdir ./log

logger CATWATCHER: starting activity logger

python log_activity.py >> ./log/activity.log

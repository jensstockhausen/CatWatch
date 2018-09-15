#!/bin/bash
BASEDIR=$(dirname "$0")
echo "$BASEDIR"
cd $BASEDIR

printf "running in $PWD\n"

mkdir ./log

logger CATWATCHER: starting activity logger

python log_activity.py


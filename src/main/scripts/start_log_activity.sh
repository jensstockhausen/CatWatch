#!/bin/bash
cd "${dirname "$0"}"

printf "running in $PWD\n"

mkdir ./log

logger CATWATCHER: starting activity logger

python log_activity.py


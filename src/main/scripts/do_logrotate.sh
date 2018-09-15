#!/bin/bash
BASEDIR=$(dirname "$0")
echo "$BASEDIR"
cd $BASEDIR

logger CATWATCH: rotating activity log

logrotate ./logrotate.conf --state ./log/logrotate-state --verbose > ./log/do_logrotate.log

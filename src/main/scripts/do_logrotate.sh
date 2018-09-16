#!/bin/bash
echo `date`

BASEDIR=$(dirname "$0")
echo "$BASEDIR"
cd $BASEDIR

logger CATWATCH: rotating activity log

/usr/sbin/logrotate ./logrotate.conf --state ./log/logrotate-state --verbose

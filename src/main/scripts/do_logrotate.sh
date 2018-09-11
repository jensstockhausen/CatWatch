#!/bin/bash
cd "$(dirname "$0")"
logger CATWATCH: rotating activity log
logrotate ./logrotate.conf --state ./logrotate-state --verbose

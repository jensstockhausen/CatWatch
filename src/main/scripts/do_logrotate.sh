#!/bin/bash
cd "${dirname "$0"}"

logger CATWATCH: rotating activity log

logrotate ./logrotate.conf --state ./log/logrotate-state --verbose

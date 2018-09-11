#!/bin/bash

logger CATWATCHER: Checking status of WIFI

# to be adjusted to your needs
# ping your router
ping -c4 192.168.3.1 > /dev/null

if [ $? != 0 ]
then
  logger  CATWATCHER: No network connection, restarting wlan0
  /sbin/ifdown 'wlan0'
  sleep 5
  /sbin/ifup --force 'wlan0'
fi

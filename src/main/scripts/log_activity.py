#!/usr/bin/python

import RPi.GPIO as GPIO
import time as time
from datetime import datetime

GPIO.setmode(GPIO.BCM)

# mapping the GPIO pins
CHANNEL_0 = 2
CHANNEL_1 = 3
CHANNEL_2 = 4
CHANNEL_3 = 5

GPIO.setup(CHANNEL_0, GPIO.IN)
GPIO.setup(CHANNEL_1, GPIO.IN)
GPIO.setup(CHANNEL_2, GPIO.IN)
GPIO.setup(CHANNEL_3, GPIO.IN)


def write_log(channel, action):
    # expecting:
    # 2017-08-05 20:24:33.336 opening 4
    
    msg = datetime.now().strftime('%Y-%m-%d %H:%M:%S.%f')[:-3]
    msg += " " + action
    msg += " " + str(channel)
    print msg

    log = open("./log/activity.log","a")
    log.write("%s\r\n" % msg)
    log.close()


# write activity to log file
def handle_activity(channel):
    if not GPIO.input(channel):
        write_log(channel, "opening")
    else:
        write_log(channel, "closing")


write_log(0, "started")

GPIO.add_event_detect(CHANNEL_0, GPIO.BOTH, callback=handle_activity, bouncetime=1000)
GPIO.add_event_detect(CHANNEL_1, GPIO.BOTH, callback=handle_activity, bouncetime=1000)
GPIO.add_event_detect(CHANNEL_2, GPIO.BOTH, callback=handle_activity, bouncetime=1000)
GPIO.add_event_detect(CHANNEL_3, GPIO.BOTH, callback=handle_activity, bouncetime=1000)

while True:
    time.sleep(1e6)

GPIO.cleanup()

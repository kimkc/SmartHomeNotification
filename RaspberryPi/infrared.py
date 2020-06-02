# -*- coding: utf-8 -*-
##########################################################
# title          :infrared.py
# description    :This code sends a message to the server 
#                 when an infrared sensor is detected.
# author         :Hyunwoo Kim
# date           :19/09/27
# version        :1.0
# usage          :python infrared.py
# python_version :3.5.3
##########################################################

import time
import socket
from socket import *
import subprocess
import RPi.GPIO as GPIO

GPIO.setmode(GPIO.BCM)
PIN_NO = 16
GPIO.setup(PIN_NO, GPIO.IN, pull_up_down=GPIO.PUD_DOWN)
SERVER_IP = '15.164.158.59'
SERVER_PORT = 2123
SOUND_FILE = 'message.mp3'
MESSAGE = '거실에서 움직임이 탐지되었습니다.'

def output_sound():
    """ Sound file speaker output
    """
    try:
        subprocess.call(['ffplay', '-nodisp', '-autoexit', SOUND_FILE])
    except:
        pass

def detect_infrared():
    """ Infrared sensor detects movement
    """
    try:
        count = 0
        while True:
            if GPIO.input(PIN_NO) == True:
                count += 1
                print('[+] Detected ' + str(count))
                output_sound()
                send_message()
                time.sleep(2)
    except Exception as e:
        GPIO.cleanup()

def send_message():
    """ Send a message to the server
    """
    try:
        sock = socket(AF_INET, SOCK_STREAM)
        sock.connect((SERVER_IP, SERVER_PORT))
        print('[+] ' + SERVER_IP + ' connected!')
        position = MESSAGE.encode('utf-8')
        sock.send(bytes(position))
        sock.close()
        print('[+] Transfer completed!')
    except Exception as e:
        print('[-]', e)

if __name__ == '__main__':
    detect_infrared()
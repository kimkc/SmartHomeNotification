# -*- coding: utf-8 -*-
#############################################################
# title          :tts.py
# description    :This code receives the message 
#                 and converts the text to speech for output.
# author         :Hyunwoo Kim
# date           :19/09/27
# version        :1.0
# usage          :python tts.py
# python_version :3.5.3
#############################################################

import time
import socket
from socket import *
import subprocess
import urllib
import urllib.request

SERVER_PORT = 2123
REST_API_KEY = 'YOUR_REST_API_KEY'
dialogic_style = [
    'WOMAN_READ_CALM', 
    'MAN_READ_CALM', 
    'WOMAN_DIALOG_BRIGHT', 
    'MAN_DIALOG_BRIGHT'
]
speech_speed = [
    'SS_READ_SPEECH', 
    'SS_ALT_FAST_1', 
    'SS_ALT_SLOW_1'
]
SOUND_FILE = 'message2.mp3'

def output_sound():
    """ Sound file speaker output
    """
    try:
        subprocess.call(['ffplay', '-nodisp', '-autoexit', SOUND_FILE])
    except:
        pass

def text_to_speech(message, dialogic, speed):
    """ Convert message to sound file
    - param message: Message
    - param dialogic: Dialogic style
    - param speed: Speech speed
    - ptype message: String
    - ptype dialogic: String
    - ptype speed: String
    """
    request = urllib.request.Request(
        url = 'https://kakaoi-newtone-openapi.kakao.com/v1/synthesize',
        data = ('<speak><voice name="' + dialogic + '" speechStyle="' \
                + speed + '">' + message + '</voice></speak>').encode('utf-8'),
        headers = {
            'Content-Type': 'application/xml',
            'Authorization': 'KakaoAK ' + REST_API_KEY
        }
    )
    response = urllib.request.urlopen(request)
    with open(SOUND_FILE, 'wb') as fp:
        fp.write(response.read())

def receive_message():
    """ Receive messages sent from outside
    - return: Message
    - rtype: String
    """
    try:
        sock = socket(AF_INET, SOCK_STREAM)
        sock.setsockopt(SOL_SOCKET, SO_REUSEADDR, 1)
        sock.bind(('', SERVER_PORT))
        print('Waiting for connection...')
        sock.listen(1)
        sock_connect, addr = sock.accept()
        print('[+] ' + str(addr) + ' connected!')
        data = sock_connect.recv(1024)
        sock_connect.close()
        sock.close()
        return data.decode('euc-kr')
    except Exception as e:
        print('[-]', e)

if __name__ == '__main__':
    while True:
        message = receive_message()
        dialogic = dialogic_style[0]
        speed = speech_speed[0]
        text_to_speech(message, dialogic, speed)
        output_sound()
        time.sleep(5)
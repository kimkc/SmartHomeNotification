#-*- coding: utf-8 -*-
#################################################
# title          :kakaotalk.py
# description    :This code is to send KakaoTalk.
# author         :Hyunwoo Kim
# date           :19/09/26
# version        :1.0
# usage          :python kakaotalk.py
# python_version :3.5.3
#################################################

import os
import socket
from socket import *
import json
import requests
import webbrowser

SERVER_PORT = 2123
REST_API_KEY = 'YOUR_REST_API_KEY'
REDIRECT_URL = 'http://15.164.158.59/oauth/getCode.php'
CODE_FILE = '/var/www/html/oauth/code.txt'

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

def access():
    """ Access KakaoTalk authentication server to get access code
    """
    url = 'https://kauth.kakao.com/oauth/authorize?client_id=' \
          + REST_API_KEY + '&redirect_uri=' + REDIRECT_URL \
          + '&response_type=code&scope=talk_message'
    webbrowser.register('firefox', None)
    webbrowser.open(url)

def get_code():
    """ Return generated access code
    - return: Access code
    - rtype: String
    """
    try:
        with open(CODE_FILE, 'r') as fp:
            code = fp.read()
        os.system('rm -rf ' + CODE_FILE)
        return code
    except Exception as e:
        print('[-] Authentication code not created.')

def get_token():
    """ Get access token from Kakao authentication server using access code
    - return: Access token
    - rtype: String
    """
    try:
        url = 'https://kauth.kakao.com/oauth/token'
        payload = 'grant_type=authorization_code' \
                   + '&client_id=' + REST_API_KEY \
                   + '&redirect_url=' + '&code=' + get_code()
        headers = {
            'Content-Type': 'application/x-www-form-urlencoded',
            'Cache-Control': 'no-cache',
        }
        response = requests.request('POST', url, data=payload, headers=headers)
        token = response.json()
        return token['access_token']
    except Exception as e:
        print('[-] Access token not created.')

def send_message(token, message):
    """ Send message to KakaoTalk server
    - param token: Access token
    - param message: KakaoTalk message to send
    - ptype token: String
    - ptype message: String
    """
    try:
        url = 'https://kapi.kakao.com/v2/api/talk/memo/default/send'
        payload_dict = dict({
            'object_type': 'text',
            'text': message,
            'link': {
                'web_url': '',
                'mobile_web_url': ''
            },
            'button_title': 'Open',
        })
        payload = 'template_object=' + str(json.dumps(payload_dict))
        headers = {
            'Content-Type': 'application/x-www-form-urlencoded',
            'Cache-Control': 'no-cache',
            'Authorization': 'Bearer ' + token
        }
        requests.request('POST', url, data=payload, headers=headers)
        print('[+] Message sent successfully.')
    except Exception as e:
        print('[-] Message failed to send.')

if __name__ == '__main__':
    while True:
        message = receive_message()
        access()
        send_message(get_token(), message)
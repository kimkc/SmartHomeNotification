# -*- coding: utf-8 -*-
########################################################
# title          :detect.py
# description    :This code recognizes the learned face.
# author         :Hyunwoo Kim
# date           :19/09/25
# version        :1.0
# usage          :python detect.py
# python_version :3.5.3
########################################################

import os
import subprocess
import requests
from PIL import Image
import numpy as np
import cv2

IMAGE_DIR = 'image/'
TRAIN_DIR = 'train/'
TRAIN_YML = TRAIN_DIR + 'trainer.yml'
FACE_XML = 'haarcascade_frontalface_default.xml'
SEND_URL = 'http://106.10.35.183:8080/infrared?name='
MAX_COUNT = 5
SEND_COUNT = 3
SOUND_FILE = 'detect'
DEVICE_NUMBER = 0
WIDTH, HEIGHT = 1000, 600
detect_count = None

def output_sound(id_):
    """ Sound file speaker output
    - param id_: Unique id of the face
    - ptype id_: String
    """
    try:
        subprocess.call(
            ['ffplay', 
             '-nodisp', 
             '-autoexit', 
             SOUND_FILE + str(id_) + '.mp3']
        )
    except:
        pass

def send_name(name):
    """ Send name of detected face to server
    - param name: Name of the face
    - ptype name: String
    """
    requests.get(SEND_URL + name)
    print('[+] Send ' + name)

def show_face():
    """ Send name of detected face to server
    - param name: Name of the face
    - ptype name: String
    """
    global detect_count
    cam = cv2.VideoCapture(DEVICE_NUMBER)
    cam.set(3, WIDTH)
    cam.set(4, HEIGHT)
    minW, minH = 0.1 * cam.get(3), 0.1 * cam.get(4)
    recognizer = cv2.face.LBPHFaceRecognizer_create()
    recognizer.read(TRAIN_YML)
    face_detector = cv2.CascadeClassifier(FACE_XML)
    font = cv2.FONT_HERSHEY_SIMPLEX
    names = [os.listdir(IMAGE_DIR + '/' + str(i))[0].split('.')[0] \
             for i in range(len(os.listdir(IMAGE_DIR)))]
    detect_count = [0 for _ in range(len(names))]
    while True:
        ret, img = cam.read()
        gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
        faces = face_detector.detectMultiScale(
            gray, 
            scaleFactor = 1.2, 
            minNeighbors = 5, 
            minSize = (int(minW), int(minH))
        )
        for (x,y,w,h) in faces:
            cv2.rectangle(img, (x,y), (x+w,y+h), (0,255,0), 2)
            id_, confidence = recognizer.predict(gray[y:y+h,x:x+w])
            if confidence < 100:
                name = names[id_]
                confidence = '  {0}%'.format(round(100 - confidence))
                detect_count[id_] += 1
                if detect_count[id_] >= MAX_COUNT:
                    send_name(name)
                    if detect_count[id_] >= MAX_COUNT * SEND_COUNT:
                        detect_count[id_] = 0
                        output_sound(id_)
            else:
                name = 'unknown'
                confidence = '  {0}%'.format(round(100 - confidence))
            cv2.putText(img, str(name), (x+5,y-5), 
                        font, 1, (255,255,255), 2)
            cv2.putText(img, str(confidence), 
                        (x+5,y+h-5), font, 1, (255,255,0), 1)
        cv2.imshow('Detect face', img)
        if (cv2.waitKey(10) & 0xff) == 27:
            break
    cam.release()
    cv2.destroyAllWindows()

if __name__ == '__main__':
    show_face()
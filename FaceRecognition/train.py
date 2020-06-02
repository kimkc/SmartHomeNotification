# -*- coding: utf-8 -*-
##################################################
# title          :train.py
# description    :This code trains the face image.
# author         :Hyunwoo Kim
# date           :19/09/25
# version        :1.0
# usage          :python train.py
# python_version :3.5.3
##################################################

import os
from PIL import Image
import numpy as np
import cv2

IMAGE_DIR = 'image/'
TRAIN_DIR = 'train/'
TRAIN_YML = TRAIN_DIR + 'trainer.yml'
FACE_XML = 'haarcascade_frontalface_default.xml'
DEVICE_NUMBER = 0
WIDTH, HEIGHT = 640, 480
IMAGE_COUNT = 30

def save_face(face_name):
    """ Recognize faces and save them as black and white image files
    - param face_name: Name of person corresponding to face
    - ptype face_name: String
    - return: Unique id of the face
    - rtype: String
    """
    cam = cv2.VideoCapture(DEVICE_NUMBER)
    cam.set(3, WIDTH)
    cam.set(4, HEIGHT)
    face_detector = cv2.CascadeClassifier(FACE_XML)
    os.system('mkdir ' + IMAGE_DIR \
              + str(max(list(map(int, os.listdir(IMAGE_DIR)))) + 1))
    print('\nLook the camera and wait...')
    count = 0
    while True:
        ret, img = cam.read()
        gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
        faces = face_detector.detectMultiScale(gray, 1.3, 5)
        for (x,y,w,h) in faces:
            cv2.rectangle(img, (x,y), (x+w,y+h), (255,0,0), 2)
            count += 1
            cv2.imwrite(IMAGE_DIR + '/' \
                        + str(max(list(map(int, os.listdir(IMAGE_DIR))))) \
                        + '/' + str(face_name) + '.' + str(count) \
                        + '.jpg', gray[y:y+h,x:x+w])
            cv2.imshow('Save face image', img)
        if (cv2.waitKey(100) & 0xff) == 27:
            break
        if count >= IMAGE_COUNT:
             break
    print('\n[+] Face images saved.')
    cam.release()
    cv2.destroyAllWindows()
    return str(max(list(map(int, os.listdir(IMAGE_DIR)))))

def train_face(id_):
    """ Train face images to generate result files
    - param id_: Unique id of the face
    - ptype id_: String
    """
    recognizer = cv2.face.LBPHFaceRecognizer_create()
    face_detector = cv2.CascadeClassifier(FACE_XML)
    face_samples, ids = list(), list()
    for i in range(int(id_) + 1):
        path = IMAGE_DIR + str(i) + '/'
        images = [os.path.join(path, f) for f in os.listdir(path)]
        for image in images:
            PIL_img = Image.open(image).convert('L')
            img_numpy = np.array(PIL_img, 'uint8')
            faces = face_detector.detectMultiScale(img_numpy)
            for (x,y,w,h) in faces:
                face_samples.append(img_numpy[y:y+h,x:x+w])
                ids.append(i)
    recognizer.train(face_samples, np.array(ids))
    recognizer.write(TRAIN_YML)
    print('\n[+] Faces trained.')

if __name__ == '__main__':
    face_name = input('\nInput face name : ')
    new_id = save_face(face_name)
    train_face(new_id)
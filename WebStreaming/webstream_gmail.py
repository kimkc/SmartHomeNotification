# -*- coding: utf-8 -*-
##########################################################
# title          :webstream_gmail.py
# description    :This code streams a webcam. 
#                 Also send mail after intruder detection.
# author         :Hyunwoo Kim
# date           :19/09/28
# version        :1.0
# usage          :python webstream_gmail.py
# python_version :3.5.3
##########################################################

import time
import socket
from socket import *
import threading
import numpy as np
import cv2
import bottle
import smtplib
from email.mime.image import MIMEImage
from email.mime.multipart import MIMEMultipart

SAVE_DIR = './motion'
ID, PW = 'YOUR_GMAIL_ID', 'YOUR_GMAIL_PW'
IP, PORT = '192.168.0.218', '5123'
WIDTH, HEIGHT = '1000', '1000'
DEVICE_NUMBER = 1
send_set = False
send_frame = None

def thread_gmail():
    """ Save the detected image and call the function to send mail
    """
    global send_set, send_frame
    while True:
        if send_set:
            now = time.localtime()
            cur_time = str(now.tm_year) + '.' + str(now.tm_mon) + '.' \
                       + str(now.tm_mday) + '_' + str(now.tm_hour) + '-' \
                       + str(now.tm_min) + '-' + str(now.tm_sec)
            image_file = SAVE_DIR + '/' + cur_time + '_detect.jpg'
            cv2.imwrite(image_file, send_frame)
            print('[+] ' + image_file)
            send_gmail(send_frame, ID, PW)
            send_set = False
            time.sleep(3)
        time.sleep(0.1)

def send_gmail(image, userid, passwd):
    """ Send gmail
    - param image: Face image numpy array
    - param userid: G-mail id
    - param passwd: G-mail password
    - ptype image: Numpy array
    - ptype userid: String
    - ptype passwd: String
    """
    smtp_server = 'smtp.gmail.com'
    port = 587
    to = [userid]
    imageByte = cv2.imencode('.jpeg', image)[1].tostring()
    msg = MIMEMultipart()
    imageMime = MIMEImage(imageByte)
    msg.attach(imageMime)
    msg['From'] = 'Me'
    msg['To'] = to[0]
    msg['Subject'] = 'Motion Detected!'
    server = smtplib.SMTP(smtp_server, port)
    server.ehlo_or_helo_if_needed()
    server.starttls()
    server.ehlo_or_helo_if_needed()
    ret, m = server.login(userid, passwd)
    if ret != 235:
        return
    server.sendmail('me', to, msg.as_string())
    server.quit()

def get_gray_image(cam):
    """ Frame black and white conversion
    - param cam: Current frame
    - ptype cam: Numpy array
    - return: Black and white converted frame
    - rtype: Numpy array
    """
    return cv2.cvtColor(cam.read()[1], cv2.COLOR_RGB2GRAY)

def diff_image(images):
    """ Calculate difference between current and past Frame
    - param images: Images numpy array
    - ptype images: List
    - return: Frame of bitwise present and past frame
    - rtype: Numpy array
    """
    prev_image = cv2.absdiff(images[0], images[1])
    cur_image = cv2.absdiff(images[1], images[2])
    return cv2.bitwise_and(prev_image, cur_image)

def update_image(cam, images):
    """ Frame replacement
    - param cam: Current frame
    - param images: Images numpy array
    - ptype cam: Numpy array
    - ptype images: List
    """
    images[0] = images[1]
    images[1] = images[2]
    images[2] = get_gray_image(cam)

@bottle.get('/stream.mjpg')
def stream():
    """ Web streaming
    """
    global send_set, send_frame
    cam = cv2.VideoCapture(DEVICE_NUMBER)
    thresh = 32
    images = [None] * 3
    for n in range(len(images)):
        images[n] = get_gray_image(cam)
    bottle.response.set_header('Content-Type',
                               'multipart/x-mixed-replace; boundary=--MjpgBound')
    while True:
        ret, frame = cam.read()
        diff = diff_image(images)
        ret, thresh_img = cv2.threshold(diff, thresh, 1, cv2.THRESH_BINARY)
        count = cv2.countNonZero(thresh_img)
        if count > 20:
            nz = np.nonzero(thresh_img)
            cv2.rectangle(frame, (min(nz[1]),min(nz[0])), 
                          (max(nz[1]),max(nz[0])), (0,0,255), 2)
            send_frame = frame
            send_set = True
        jpeg_data = cv2.imencode('.jpeg', frame)[1].tostring()
        string = '--MjpgBound\r\n' \
                  + 'Content-Type: image/jpeg\r\n' \
                  + 'Content-length: ' + str(len(jpeg_data)) + '\r\n\r\n' \
                  + jpeg_data + '\r\n\r\n\r\n'
        yield string
        update_image(cam, images)
        if cv2.waitKey(10) == 27:
            break

@bottle.route('/')
def page():
    """ Return HTML page
    - return: HTML
    - rtype: String
    """
    return '<html><body><img src="stream.mjpg" width=' \
           + WIDTH + ' height=' + HEIGHT + '></body></html>'

if __name__ == '__main__':
    t1 = threading.Thread(target=thread_gmail, args=())
    t1.start()
    bottle.debug(True)
    bottle.run(host=IP, port=PORT)
    t1.join()
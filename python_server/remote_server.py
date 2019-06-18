import threading
from socket import *
import numpy as np
import cv2
import face_harr as harr
import firebase_supporter as firebase
import ctypes  # An included library with Python install.
import tkinter
import json
from time import sleep

ip_addr = "192.168.0.18"

def get_thermal():

    while True:
        thermalSock = socket(AF_INET, SOCK_STREAM)
        thermalSock.connect((ip_addr, 8080))

        data = []

        length = thermalSock.recv(5)
        length = int(json.loads(length))

        while True:
            temp = thermalSock.recv(1024)
            data += temp

            if len(data) >= length:
                break

        data = json.loads(bytearray(data).decode("utf-8"))

        np_data = np.array(data,np.uint16)
        np_data = np.right_shift(np_data,8,np_data)
        np_data = np.asarray(np_data, dtype=np.uint8)

        image = np_data.reshape((60,80))

        image = cv2.cvtColor(image, cv2.COLOR_GRAY2BGR)

        bbox, image_frame = harr.detection_face_harr(image)

        image_frame = cv2.resize(image_frame, (640, 480), interpolation=cv2.INTER_LINEAR)
        #cv2.putText(image_frame, 'Celsius : ' + str(curThermal), (10, 30), cv2.FONT_HERSHEY_SIMPLEX, 1, (255, 255, 255))
        cv2.imshow("test", image_frame)
        cv2.waitKey(1)

        if len(bbox) == 0:
            print("failure to detection!")

        else:
            isValidRect, curThermal = calculate_thermal(data, bbox)

            if isValidRect:
                print("success to detection!")

                image_frame = cv2.resize(image_frame, (640, 480), interpolation=cv2.INTER_LINEAR)
                cv2.putText(image_frame, 'Celsius : ' + str(curThermal), (10, 30), cv2.FONT_HERSHEY_SIMPLEX, 1, (255, 255, 255))
                cv2.imshow("test", image_frame)
                cv2.waitKey(1)

                if abs(curThermal - 36.5) > 2:
                    fb.alertRegistration("KwangwonChoi","thermal alert","danger, temperature is " + str(curThermal))

        sleep(2)

def motion_detect():
    motionSock = socket(AF_INET, SOCK_STREAM)
    motionSock.connect((ip_addr, 8081))
    falseCnt = 0
    while True:
        data = (motionSock.recv(1024)).decode('utf-8')
        if data == "true":
            print("motion detected!")
            falseCnt = 0

        if falseCnt >= 100:
            fb.alertRegistration("KwangwonChoi","motion alert","Didn't move for 20 seconds")
            falseCnt = 0

        falseCnt += 1

        sleep(0.1)


def messageGUI() :

    def messageCallback(message):
        dict = message[1]

        alertMessage = "환자 이름 : " + dict['id']\
                    + "\n제목 :" + dict['title']\
                    + "\n내용 : " + dict['contents']

        ctypes.windll.user32.MessageBoxW(0, alertMessage , "알림", 0)


    def btn() :
        name = totxt.get()
        context = contexttxt.get("1.0", 'end-1c')
        print("보호자 : ", name, "    보낼내용 : ", context)
        fb.alertRegistration(user=name, state="alert", details=context) #details는 GUI에서 받아오도록.

    fb.listenMessage(messageCallback) #쓰레드 돌면서 주기적으로 메세지 확인. 메세지 확인시 messageCallback함수 호출

    window = tkinter.Tk()
    window.title("Capston_Window")
    window.geometry("480x480+100+100")
    window.resizable(False, False)

    tolabel = tkinter.Label(window, text="보호자 : ")
    tolabel.grid(row="0", column="0")
    totxt = tkinter.Entry(window)
    totxt.config(width=50)
    totxt.grid(row="0", column="1")

    contextlabel = tkinter.Label(window, text="보낼 내용 : ")
    contextlabel.grid(row="1", column="0")
    contexttxt = tkinter.Text(window)
    contexttxt.config(width=50, height=10)
    contexttxt.grid(row="1", column="1")

    btn = tkinter.Button(window, text="전송", command=btn)
    btn.grid(row="4", column="1")

    window.mainloop()

def calculate_thermaldata(value):
    return (value - 27315) / 100

def calculate_thermal(data, bbox) :
    x = bbox[0][0]
    y = bbox[0][1]
    w = bbox[0][0] + bbox[0][2]
    h = bbox[0][1] + bbox[0][3]
    totalThermal = 0
    cnt = 0
    isValidRect = True

    for i in range(x, h):
        for j in range(y, w):

            celcius = calculate_thermaldata(data[i][j][0])

            if celcius >= 33 and celcius <= 40:
                totalThermal += celcius
                cnt += 1

    if(cnt > 0):
        totalThermal = (totalThermal / cnt )
    else:
        isValidRect = False

    if ( cnt / w * h ) <= 0.5:
        isValidRect = False

    return isValidRect, totalThermal


fb = firebase.FirebaseSupporter()
print("thread start")
thermal_t = threading.Thread(target=get_thermal, args=())
motion_t = threading.Thread(target=motion_detect, args=())
gui_t = threading.Thread(target=messageGUI, args=())

#motion_t.start()
#thermal_t.start()
gui_t.start()
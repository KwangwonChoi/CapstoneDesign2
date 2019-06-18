import RPi.GPIO as gpio
import time
import socket

while True :
    gpio.setmode(gpio.BCM)

    NO_GPIO = 4

    gpio.setmode(gpio.BCM)
    gpio.setup(NO_GPIO, gpio.IN)
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    sock.bind(("192.168.0.18", 8081))
    sock.listen(True)
    conn, addr = sock.accept()
    
    print('connection!')
    
    while True :
        try :
            if gpio.input(NO_GPIO) :
                #conn.send("true")
                #print("-----------detect moving-----------")
                conn.send("false")
                print("NOTHING!!!!!")
            else :
                #conn.send("false")
                #print("don't moving")
                conn.send("true")
                print("----------detect moving----------")
            time.sleep(0.1)
        except :
            gpio.cleanup()
            sock.close()
            break;


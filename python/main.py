from arduino.app_utils import *
import time

numOfModes = 2
mode = 0


def changeMode(num):
    global mode 
    mode = num%numOfModes
    print(mode)

Bridge.provide("ChangeMode", changeMode)

logo = [0,0,0,0,0,0,0,0,0,0,0,0,0,
        0,0,0,0,0,0,0,0,0,0,0,0,0,
        0,0,1,1,0,0,0,0,0,1,1,0,0,
        0,0,1,1,0,0,0,0,0,1,1,0,0,
        0,0,0,0,0,0,0,0,0,0,0,0,0,
        0,0,1,0,0,0,0,0,0,0,1,0,0,
        0,0,0,1,1,1,1,1,1,1,0,0,0,
        0,0,0,0,0,0,0,0,0,0,0,0,0]

logo2 = [0,0,0,0,0,0,0,0,0,0,0,0,0,
        0,0,0,0,0,0,0,0,0,0,0,0,0,
        0,0,1,1,0,0,0,0,0,1,1,0,0,
        0,0,1,1,0,0,0,0,0,1,1,0,0,
        0,0,0,0,0,0,0,0,0,0,0,0,0,
        0,0,0,1,1,1,1,1,1,1,0,0,0,
        0,0,1,0,0,0,0,0,0,0,1,0,0,
        0,0,0,0,0,0,0,0,0,0,0,0,0]

modes = [logo,logo2]

while True:
    encoded = ",".join(str(x) for x in modes[mode])
    Bridge.call("drawMatrix", encoded)
    time.sleep(0.01)

from arduino.app_utils import *
from state_interface import LedController, Smile
import state_interface
import time

numOfModes = 2
mode = 0


def changeMode(num):
    global mode 
    mode = num%numOfModes
    print(mode)

Bridge.provide("ChangeMode", changeMode)

smile = Smile()

name = LedController([smile])
while True:
    name.update(mode)
    time.sleep(0.01)
    

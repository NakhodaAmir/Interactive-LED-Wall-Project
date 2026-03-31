from smiletest import Smile
from Frowntest import Frown
from LedController import LedController
from arduino.app_utils import *

mode = 0
frown = Frown()
smile = Smile()
states =[smile, frown]
numOfModes = len(states)

def changeMode(num):
    global mode
    mode = num%numOfModes
    print(mode)

Bridge.provide("ChangeMode", changeMode)


name = LedController(states)
while True:
    name.update(mode)

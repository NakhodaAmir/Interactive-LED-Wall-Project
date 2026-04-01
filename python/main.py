from states.SmileTest import Smile
from states.FrownTest import Frown
from states.Rotate import Rotate
from classes.LedController import LedController
from arduino.app_utils import *

mode = 0
frown = Frown()
rotate = Rotate()
smile = Smile()
states =[smile, frown, rotate]
numOfModes = len(states)

def changeMode(num):
    global mode
    mode = num%numOfModes
    print(mode)

Bridge.provide("ChangeMode", changeMode)


name = LedController(states)
while True:
    name.update(mode)

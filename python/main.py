from smiletest import Smile
from Frowntest import Frown
from LedController import LedController


def changeMode(num):
    global mode
    mode = num%numOfModes
    print(mode)


frown = Frown()
smile = Smile()
mode = 0
states =[smile, frown]
numOfModes = len(states)

name = LedController(states)
while True:
    name.update(mode)
from arduino.app_utils import *
import numpy as np

numOfModes = 2
mode = 0

class LedState:
    def calculate_array(self, arr):
        a = np.array(arr)
        return a.ravel()

class LedController:      
    def __init__(self, states) -> None:
        self.states = states

    def update(self, mode):
        encoded = ",".join(str(x) for x in self.states[mode].calculate_array())
        Bridge.call("drawMatrix", encoded)


class Smile(LedState):
    def calculate_array():
        arr = [[0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,1,1,0,0,0,0,0,1,1,0,0],
                [0,0,1,1,0,0,0,0,0,1,1,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,1,0,0,0,0,0,0,0,1,0,0],
                [0,0,0,1,1,1,1,1,1,1,0,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0]]
        return super().calculate_array(arr)

def changeMode(num):
    global mode 
    mode = num%numOfModes
    print(mode)

smile = Smile()
states = [smile]
mode = 0
numOfModes = len(states)

name = LedController(states)
while True:
    name.update(mode)

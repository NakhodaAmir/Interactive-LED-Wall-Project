from arduino.app_utils import *
import numpy as np

numOfModes = 2
mode = 0


def changeMode(num):
    global mode 
    mode = num%numOfModes
    print(mode)

class LedState:
    def calculate_array(arr):
        a = np.array(arr)
        return a.ravel()

class LedController:      
    def __init__(self, states) -> None:
        self.states = states

    def update(self):
        encoded = ",".join(str(x) for x in self.states[mode])
        Bridge.call("drawMatrix", encoded)



class Smile(LedState):
    def calculate_array():
        arr = [[0, 0, 0, 0, 0, 0, 0, 0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,1,1,0,0,0,0,0,1,1,0,0],
                [0,0,1,1,0,0,0,0,0,1,1,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,1,0,0,0,0,0,0,0,1,0,0],
                [0,0,0,1,1,1,1,1,1,1,0,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0]]
        return super(arr)


name = LedController()
while True:
    name.update()

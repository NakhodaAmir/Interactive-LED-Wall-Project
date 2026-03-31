from arduino.app_utils import *
import numpy as np

class LedState:
    def calculate_array(self, arr):
        a = np.array(arr)
        return a.ravel()

class LedController:      
    def __init__(self, states) -> None:
        self.states = states

    def update(self, mode):
        mode = mode % len(self.states)  # wrap around safely
        encoded = ",".join(str(x) for x in self.states[mode].calculate_array())
        Bridge.call("drawMatrix", encoded)

class Smile(LedState):
    def calculate_array(self):          # ← add 'self'
        arr = [[0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,1,1,0,0,0,0,0,1,1,0,0],
                [0,0,1,1,0,0,0,0,0,1,1,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,1,0,0,0,0,0,0,0,1,0,0],
                [0,0,0,1,1,1,1,1,1,1,0,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0]]
        return super().calculate_array(arr)  # ← this was already correct

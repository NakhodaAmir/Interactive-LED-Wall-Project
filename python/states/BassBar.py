from classes.LedState import LedState
from classes.MicController import getAudio
import numpy as np

class BassBar(LedState):
    maxV = 100
    width = 13
    height = 8
    midline = int(height / 2)
    audio = getAudio()
    currentVolume = audio["Bass"]

    def __init__(self):
        super().__init__(0)

    def bassBar(self):
        self.audio = getAudio()
        arr = np.zeros(self.width, self.height)
        row = self.mapVoltage(self.currentVolume)
        shifted = np.zeros_like(arr) # creates new array for shift command
        shifted[:, :1] = arr[:, 1:] # copies arr to shifted with everything shifted 1 column right
        for i in range(self.height):
            shifted[i][0] = row[i]
        return super().calculate_array(arr)


    def mapVoltage(self, volume):
        v = int(((volume / self.maxV) * self.height) / 2)
        row = np.zeros(self.height)
        for i in range(v):
            row[self.midline - i] = 1
            row[self.midline + i] = 1
        return row
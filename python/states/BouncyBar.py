from classes.LedState import LedState
from classes.MicController import getAudio
import numpy as np

class BassBar(LedState):
    maxV = 100
    width = 13
    height = 8
    midline = int(height / 2)
    audio = getAudio()
    currentSubBass = audio["Sub Bass"]
    currentBass = audio["Bass"]
    currentMid = audio["Mid"]
    currentTreble = audio["Treble"]
    currentAir = audio["Air"]


    def __init__(self):
        super().__init__(0.01)

    def bouncyBar(self):
        self.audio = getAudio()
        arr = np.zeros(self.height, self.width)

        """ # For testing on 19x14 full LED wall
        arr[0] = self.mapVoltage(self.currentSubBass / 2)
        arr[1] = self.mapVoltage(self.currentSubBass)
        arr[2] = self.mapVoltage(self.currentSubBass)
        arr[3] = self.mapVoltage(self.currentSubBass / 2)
        arr[4] = self.mapVoltage(self.currentBass / 2)
        arr[5] = self.mapVoltage(self.currentBass)
        arr[6] = self.mapVoltage(self.currentBass)
        arr[7] = self.mapVoltage(self.currentBass / 2)
        arr[8] = self.mapVoltage(self.currentMid / 2)
        arr[9] = self.mapVoltage(self.currentMid)
        arr[10] = self.mapVoltage(self.currentMid)
        arr[11] = self.mapVoltage(self.currentMid / 2)
        arr[12] = self.mapVoltage(self.currentTreble / 2)
        arr[13] = self.mapVoltage(self.currentTreble)
        arr[14] = self.mapVoltage(self.currentTreble)
        arr[15] = self.mapVoltage(self.currentTreble / 2)
        arr[16] = self.mapVoltage(self.currentAir / 2)
        arr[17] = self.mapVoltage(self.currentAir)
        arr[18] = self.mapVoltage(self.currentAir / 2)
        """
        # """ For testing on 13x8 Arduino display
        arr[0] = self.mapVoltage(self.currentSubBass)
        arr[1] = self.mapVoltage(self.currentSubBass / 2)
        arr[2] = self.mapVoltage(self.currentBass / 2)
        arr[3] = self.mapVoltage(self.currentBass)
        arr[4] = self.mapVoltage(self.currentBass / 2)
        arr[5] = self.mapVoltage(self.currentMid / 2)
        arr[6] = self.mapVoltage(self.currentMid)
        arr[7] = self.mapVoltage(self.currentMid / 2)
        arr[8] = self.mapVoltage(self.currentTreble / 2)
        arr[9] = self.mapVoltage(self.currentTreble)
        arr[10] = self.mapVoltage(self.currentTreble / 2)
        arr[11] = self.mapVoltage(self.currentAir / 2)
        arr[12] = self.mapVoltage(self.currentAir)
        # """
        np.rot90(arr)
        return super().calculate_array(arr)


    def mapVoltage(self, volume):
        v = int(((volume / self.maxV) * self.height) / 2)
        row = np.zeros(self.height)
        for i in range(v):
            row[self.midline - i] = 1
            row[self.midline + i] = 1
        return row
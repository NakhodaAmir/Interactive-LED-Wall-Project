from classes.MicController import getAudio
from classes.LedState import LedState

class AudioTest(LedState):
    def __init__(self):
        super().__init__(0)

    def calculate_array(self, step): #If you are seeing this I lowk dont know how to do an all 0 array in python so if you know def fix thanks
        arr =  [[0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0]]

        levels = getAudio()
        keys = ["Sub Bass", "Bass", "Mid", "Treble", "Air"]
        for i in range(5):
            num_leds = (levels[keys[i]] / 100) * 8
            for j in range(len(arr)):
                if j >= (8 - num_leds):
                    arr[j][i] = 1
        return super().calculate_array(arr)

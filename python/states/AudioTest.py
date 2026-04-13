from classes.MicController import getAudio
from classes.LedState import LedState
import random

class AudioTest(LedState):
    def __init__(self):
        super().__init__(0)
        self.column_order = list(range(13))
        random.shuffle(self.column_order)

    def calculate_array(self, step):
        arr = [[0] * 13 for _ in range(8)]
        levels = getAudio()
        keys = ["Sub Bass", "Bass", "Mid", "Treble", "Air"]

        for i in range(13):
            col = self.column_order[i] 
            num_leds = (levels[keys[i % 5]] / 100) * 8
            for j in range(len(arr)):
                if j >= (8 - num_leds):
                    arr[j][col] = 1

        return super().calculate_array(arr)

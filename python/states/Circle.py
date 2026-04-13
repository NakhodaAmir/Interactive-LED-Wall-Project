import numpy as np
from python.classes.LedState import LedState
from python.classes.MicController import getAudio

width = 13
height = 8

center_x = width / 2
center_y = (height / 2)-0.5
max_radius = min(width,height)/2

class Circle(LedState):
    def __init__(self):
        super().__init__(0)

    def calculate_array(self, step):
        mid_volume = getAudio()["Mid"]
        y, x = np.indices((height, width))
        distance = np.sqrt((x - center_x)**2 + (y - center_y)**2)
        grid = ((distance <= max_radius*mid_volume) & (distance > max_radius*mid_volume - 1)).astype(int)
        return super().calculate_array(grid)

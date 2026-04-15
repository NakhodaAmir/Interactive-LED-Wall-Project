import numpy as np
from classes.LedState import LedState
from classes.MicController import getVolume

class Circle(LedState):
    def __init__(self):
        super().__init__(0.01)
        self.width = 13
        self.height = 8
        
        self.center_x = self.width / 2
        self.center_y = (self.height / 2)-0.5
        self.max_radius = 4

    def calculate_array(self):
        mid_volume = getVolume()
        y, x = np.indices((self.height, self.width))
        distance = np.sqrt((x - self.center_x)**2 + (y - self.center_y)**2)
        grid = ((self.max_radius*mid_volume) & (distance > self.max_radius*mid_volume - 1)).astype(int)
        return super().calculate_array(grid)

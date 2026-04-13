from classes.MicController import getAudio
import random

from classes.LedState import LedState
import numpy as np

class FallingAstroid(LedState):
    def __init__(self):
        super().__init__(0)
        self.astroids = []

    def calculate_array(self, step):
        min_y_distance = 3
        min_x_distance = 3

        can_spawn = len(self.astroids) < 3 #number of astroids to spawn
        # Vertical Spacing// only spawn astroid when there is a gap of min_y_distance
        if can_spawn and len(self.astroids) > 0:
            highest_astroid_height = self.astroids[len(self.astroids)-1].pos_y
            can_spawn = highest_astroid_height >= (-8//2 + min_y_distance)
        # Horizontal padding// only spawn an astroid when there is a gap of min_x_distance or when the astroid list is empty.
        if can_spawn:
            while True:
                random_x = random.randint(-13//2, 13//2) # randomly spawn between bounds
                if len(self.astroids) == 0 or abs(random_x - self.astroids[len(self.astroids)-1].pos_x) >= min_x_distance:
                    random_min_size = random.randint(1,3)
                    random_max_size = random.randint(4,6)
                    new_astroid = Astroid(random_min_size, random_max_size, random_x, -8 // 2, 8, 13) #spawn astroid with a random size
                    self.astroids.append(new_astroid)
                    break

        arr = np.zeros((8, 13), dtype=int)

        for astroid in self.astroids:
            arr = arr | astroid.get_grid()
            astroid.fall()
            astroid.pulse_size(getAudio()["Bass"])

        self.astroids = [astroid for astroid in self.astroids if astroid.active]

        return super().calculate_array(arr)

class Astroid:
    def __init__(self, min_size: int, max_size: int, pos_x: int, pos_y: int, rows:int, cols:int):
        self.min_size = min_size
        self.max_size = max_size
        self.size = min_size
        self.pos_x = pos_x
        self.pos_y = pos_y
        self.rows = rows
        self.cols = cols
        self.active = True

    def fall(self):
        self.pos_y += 1
        if self.pos_y > self.rows/2 + self.size:
            self.active = False

    # mathematica formula of an astroid : (x-h)**(2/3) + (y-k)**(2/3) = a**(2/3)
    def get_grid(self):
        y, x = np.ogrid[-self.rows/2:self.rows/2, -self.cols/2:self.cols/2] # y is rows, x is column 8 rows, 13 collumns
        asteroid_area = ((x-self.pos_x)**2)**(1/3) + ((y-self.pos_y)**2)**(1/3) <= (self.size**2)**(1/3)
        return asteroid_area.astype(int)

    def pulse_size(self, volume):
        self.size = ((self.max_size - self.min_size) * volume//100) - self.min_size
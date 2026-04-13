from classes.MicController import getAudio
import random
from classes.LedState import LedState
import numpy as np

_ROWS, _COLS = 8, 13
_Y_GRID, _X_GRID = np.ogrid[-_ROWS/2:_ROWS/2, -_COLS/2:_COLS/2]
_Y_GRID = np.ascontiguousarray(_Y_GRID, dtype=np.float32)
_X_GRID = np.ascontiguousarray(_X_GRID, dtype=np.float32)

class FallingAstroid(LedState):
    def __init__(self):
        super().__init__(0)
        self.astroids = []

    def calculate_array(self, step):
        min_y_distance = 3
        min_x_distance = 3
        can_spawn = len(self.astroids) < 3

        if can_spawn and self.astroids:
            can_spawn = self.astroids[-1].pos_y >= (-_ROWS // 2 + min_y_distance)

        if can_spawn:
            for _ in range(10):
                random_x = random.randint(-_COLS // 2, _COLS // 2)
                if not self.astroids or abs(random_x - self.astroids[-1].pos_x) >= min_x_distance:
                    new_astroid = Astroid(
                        random.randint(1, 3),
                        random.randint(4, 6),
                        random_x, -_ROWS // 2
                    )
                    self.astroids.append(new_astroid)
                    break

        arr = np.zeros((_ROWS, _COLS), dtype=np.uint8) 
        mid_volume = getAudio()["Mid"]

        active = []
        for astroid in self.astroids:
            astroid.pulse_size(mid_volume)
            np.logical_or(arr, astroid.get_grid(), out=arr)  
            astroid.fall()
            if astroid.active:
                active.append(astroid)
        self.astroids = active

        return super().calculate_array(arr)


class Astroid:
    def __init__(self, min_size: int, max_size: int, pos_x: int, pos_y: int):
        self.min_size = min_size
        self.max_size = max_size
        self.size = float(min_size)
        self.pos_x = pos_x
        self.pos_y = pos_y
        self.active = True
        self._size_range = max_size - min_size  

    def fall(self):
        self.pos_y += 1
        if self.pos_y - self.size > _ROWS / 2:
            self.active = False

    def get_grid(self):
        asteroid_area = (
            np.abs(_X_GRID - self.pos_x) ** (2/3) +
            np.abs(_Y_GRID - self.pos_y) ** (2/3)
        ) <= self.size ** (2/3)
        return asteroid_area

    def pulse_size(self, volume: float):
        self.size = self.min_size + self._size_range * min(volume, 100) / 100

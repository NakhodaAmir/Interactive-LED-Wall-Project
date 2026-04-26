from time import sleep
#from classes.MicController import getVolume
import numpy as np
from classes.LedState import LedState

_ROWS, _COLS = 8, 13
_Y_GRID, _X_GRID = np.ogrid[-_ROWS/2:_ROWS/2, -_COLS/2:_COLS/2]
_Y_GRID = np.ascontiguousarray(_Y_GRID, dtype=np.float32)
_X_GRID = np.ascontiguousarray(_X_GRID, dtype=np.float32)

class FlappyBird(LedState):
    def __init__(self, column_spacing, column_width):
        super().__init__(0.01)
        self.arr = np.zeros((_ROWS, _COLS), dtype=np.uint8)
        self.column_spacing = column_spacing
        self.column_width = column_width
        self.column_shifted_space = 0
        self.spawning_column = None
        self.bird = Bird(2, 1, 3)

    def calculate_array(self):
        shifted_arr = np.empty_like(self.arr)
        shifted_arr[:, -1] = 0
        shifted_arr[:, :-1] = self.arr[:, 1:]
        self.arr = shifted_arr

        if self.spawning_column is None:
            self.column_shifted_space += 1
            if self.column_shifted_space >= self.column_spacing:
                self.spawning_column = Column(self.column_width, _ROWS, 3)
                self.column_shifted_space = 0

        if self.spawning_column is not None and self.spawning_column.active:
            new_column_arr = self.spawning_column.build_column()
            if len(new_column_arr) != 0:
                self.arr = np.maximum(self.arr, new_column_arr)
            if not self.spawning_column.active:
                self.spawning_column = None

        new_arr = np.maximum(self.arr, self.bird.arr)
        return super().calculate_array(new_arr)


class Bird:
    def __init__(self, size: int, min_flap_strength: int, max_flap_strength: int):
        self.size = size
        self.y = (_ROWS // 2) - 1
        self.x = (_COLS // 4) - 1
        self.velocity = 0
        self.min_flap_strength = min_flap_strength
        self.max_flap_strength = max_flap_strength
        self.arr = np.zeros((_ROWS, _COLS), dtype=int)
        self.update_grid()

    def update_grid(self):
        #reset array
        self.arr.fill(0)
        self.y = max(0, min(int(self.y), _ROWS - self.size))
        self.arr[self.y: self.y + self.size, self.x: self.x + self.size] = 1

    def fall(self):
        self.velocity += 1
        self.y += self.velocity
        self.update_grid()

    def flap(self, volume):
        volume = max(0, min(100, volume))
        volume = volume/100
        flap_strength = int(round(self.min_flap_strength + (self.max_flap_strength - self.min_flap_strength) * volume))
        self.velocity = -flap_strength
        self.y += self.velocity
        self.update_grid()

class Column:
    def __init__(self, width, height, hole_size):
        self.width = width
        self.shifted = 0
        self.height = height
        self.hole_size = hole_size
        self.random_y = np.random.randint(self.hole_size, _ROWS - self.hole_size)
        self.active = True

    def build_column(self):
        if self.shifted < self.width:
            arr = np.zeros((_ROWS, _COLS), dtype=np.uint8)
            arr[:, -1] = 1
            arr[self.random_y - 1:self.random_y + 2, -1] = 0
            self.shifted += 1
            if self.shifted >= self.width:
                self.active = False
            return arr
        return []

flappy_bird = FlappyBird(5, 2)
while True:
    encoded = ",".join(str(x) for x in flappy_bird.calculate_array())
    chunks = [encoded[i:i + 26] for i in range(0, len(encoded), 26)]
    for chunk in chunks:
        print(chunk)
    print("--------------------------")
    sleep(1)

# bird = Bird(2)
# print(bird.arr)
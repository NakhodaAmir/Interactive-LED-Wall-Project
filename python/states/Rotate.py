from classes.LedState import LedState

class Rotate(LedState):
    def __init__(self):
        self.path = []
        self.precompute_path()
        super().__init__(len(self.path))

    def precompute_path(self):
        """Calculates the spiral coordinates once and stores them."""
        r, c = 3, 6
        self.path.append((r, c))

        directions = [(0, 1), (1, 0), (0, -1), (-1, 0)]
        dir_idx = 0
        segment_length = 1

        # An 8x13 grid maxes out at 104 cells. 150 is plenty of padding.
        while len(self.path) < 150:
            for _ in range(2):
                for _ in range(segment_length):
                    r += directions[dir_idx][0]
                    c += directions[dir_idx][1]
                    self.path.append((r, c))
                dir_idx = (dir_idx + 1) % 4
            segment_length += 1

    def calculate_array(self, frame: int):
        # 1. Always start with a fresh blank canvas
        arr = [[0 for _ in range(13)] for _ in range(8)]

        if frame <= 0:
            return super().calculate_array(arr)

        # 2. Cap the step to avoid index out of bounds
        step = min(frame, len(self.path))

        # 3. Draw the path from scratch up to the requested step.
        # This is extremely fast and entirely stateless!
        for i in range(step):
            r, c = self.path[i]
            # Only draw if it's within the 8x13 bounds
            if 0 <= r < 8 and 0 <= c < 13:
                arr[r][c] = 1

        return super().calculate_array(arr)
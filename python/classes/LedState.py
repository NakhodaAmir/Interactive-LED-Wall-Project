import numpy as np

#base state for every led state.
#max_steps is the maximum number of iterations in a loop to cycle through the animation once
#max_steps can be 0, this just makes the behaviour a static design. see SmileTest or FrownTest
class LedState:
    def __init__(self, frame_rate):
        self.frame_rate = frame_rate

    #takes in a 2d array and return a 1d array
    def calculate_array(self, arr):
        a = np.array(arr)
        return a.ravel()

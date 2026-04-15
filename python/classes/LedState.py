import numpy as np

#base state for every led state.
#max_steps is the maximum number of iterations in a loop to cycle through the animation once
#max_steps can be 0, this just makes the behaviour a static design. see SmileTest or FrownTest
class LedState:
    def __init__(self, frame_rate):
        self.frame_rate = frame_rate

    # takes in a 2d array and 2 1d arrays of rgb codes and returns a 3d array of rgb codes
    def coloredArray(self, arr, primary, background):
        coloredArray = np.copy(arr)
        for row in coloredArray:
            for num in coloredArray:
                if(num == 1):
                    num = primary
                else:
                    num = background
        return coloredArray

    #takes in a 2d array and return a 1d array
    def calculate_array(self, arr):
        a = np.array(arr)
        return a.ravel()

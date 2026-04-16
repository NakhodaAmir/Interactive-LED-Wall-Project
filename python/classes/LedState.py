import numpy as np

#base state for every led state.
#max_steps is the maximum number of iterations in a loop to cycle through the animation once
#max_steps can be 0, this just makes the behavior a static design. see SmileTest or FrownTest
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

    def calculateSpiralArray(self, arr): # need to finish grid later but this is the concept
        count = 0
        order = [
            [14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32],
            [13, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 33],
            [12, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 34],
            [11, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 35],
            [10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 36], # etc
            [9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
        ]
        for x in range(len(arr)):
            for y in range(len(arr[x])):
                for rw in order:
                    for spot in rw:
                        if spot == count:
                            spot = arr[x][y]
                            count += 1
        return order

    #takes in a 2d array and return a 1d array
    def calculate_array(self, arr):
        a = np.array(arr)
        #a = self.calculateSpiralArray(np.array(a))
        return a.ravel()

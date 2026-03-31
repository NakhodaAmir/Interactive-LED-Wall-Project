import numpy as np

class LedState:
    def calculate_array(self,arr):
        a = np.array(arr)
        return a.ravel()

from classes.LedState import LedState

class Frown(LedState):
    def __init__(self):
        super().__init__(0)

    def calculate_array(self, step):
        arr =  [[0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,1,1,0,0,0,0,0,1,1,0,0],
                [0,0,1,1,0,0,0,0,0,1,1,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,0,1,1,1,1,1,1,1,0,0,0],
                [0,0,1,0,0,0,0,0,0,0,1,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0]]
        return super().calculate_array(arr)

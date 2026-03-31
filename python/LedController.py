from arduino.app_utils import *

class LedController:
    def __init__(self, states) -> None:
        self.states = states

    def update(self,mode):
        encoded = ",".join(str(x) for x in self.states[mode].calculate_array())
        print(encoded)
        Bridge.call("drawMatrix", encoded)
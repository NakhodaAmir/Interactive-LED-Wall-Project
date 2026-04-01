from time import sleep
from arduino.app_utils import *

# main class that controls all led states. NOT THE MAIN CLASS OF THE WHOLE PYTHON PART!!!! DO ALL STATE CHANGING IN MAIN!!!!
# states is an array of all the led states
# num_of_modes is the length of the states array
# mode is the current mode that the controller is on
class LedController:
    def __init__(self, states):
        self.states = states


    #main loop of the controller
    # runs a for loop for the number of max_steps of the led state
    # to show each iteration of the led state
    def update(self, mode):
        for step in range(self.states[mode].max_steps + 1):
            encoded = ",".join(str(x) for x in self.states[mode].calculate_array(step))
            Bridge.call("drawMatrix", encoded)



    def update_test(self, mode):
        for step in range(self.states[mode].max_steps + 1):
            encoded = ",".join(str(x) for x in self.states[mode].calculate_array(step))
            chunks = [encoded[i:i+26] for i in range(0, len(encoded), 26)]
            for chunk in chunks:
                print(chunk)
            sleep(3)

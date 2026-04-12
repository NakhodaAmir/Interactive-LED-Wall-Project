from arduino.app_utils import *
from time import sleep

# main class that controls all led states.
# states is an array of all the led states
# num_of_modes is the length of the states array
# mode is the current mode that the controller is on
class LedController:
    def __init__(self, states):
        self.states = states
        self.num_of_modes = len(self.states)
        self.mode = 0
        Bridge.provide("ChangeMode", self.changeMode)
        self.current_frame = 0
        self.max_frame = self.states[self.mode].max_frames + 1

    def changeMode(self, num):
        self.mode = num % self.num_of_modes
        self.current_frame = 0
        self.max_frame = self.states[self.mode].max_frames + 1

    # main loop of the controller
    # runs a for loop for the number of max_steps of the led state
    # to show each iteration of the led state
    def update(self):
        while True:
            if self.current_frame >= self.max_frame:
                self.current_frame = 0
            encoded = ",".join(str(x) for x in self.states[self.mode].calculate_array(self.current_frame))
            Bridge.call("drawMatrix", encoded)
            sleep(0.01)
            self.current_frame += 1
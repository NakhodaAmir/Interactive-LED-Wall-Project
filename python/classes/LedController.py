from arduino.app_utils import *
from time import sleep
#from classes.MicController import readAudio

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

    def changeMode(self, num):
        self.mode = num % self.num_of_modes
        print("Changed Mode!")

    
    #main loop of the controller
    # runs a for loop for the number of max_steps of the led state
    # to show each iteration of the led state
    def update(self):
        while True:
            encoded = ",".join(str(x) for x in self.states[self.mode].calculate_array(0))
            Bridge.call("drawMatrix", encoded)
            if(self.mode == 1):
                sleep(0.025)
            else:
                sleep(0.01)
            

    #testing loop that only prints out one cycle of the led state into the terminal prettily
    def update_test(self):
        for step in range(self.states[self.mode].max_frames + 1):
            encoded = ",".join(str(x) for x in self.states[self.mode].calculate_array(step))
            #Pretty Prints the design in the terminal
            chunks = [encoded[i:i+26] for i in range(0, len(encoded), 26)]
            for chunk in chunks:
                print(chunk)
            print("--------------------------")
            sleep(1)

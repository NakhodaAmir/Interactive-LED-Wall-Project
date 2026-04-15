from states.SmileTest import Smile
from states.FrownTest import Frown
from states.AudioTest import AudioTest
from classes.LedController import LedController
from classes.MicController import getAudio
from time import sleep

def main():
#Use for debugging
#    while True:
#        for name, level in getAudio().items():
#            print(f"{name:<10}: {level:.1f}")
#        print("---")
#        sleep(.1)
    controller = LedController([AudioTest(), Smile(), Frown()])
    controller.update()

if __name__ == "__main__":
    main()
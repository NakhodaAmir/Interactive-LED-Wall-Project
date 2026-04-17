from states.BouncyBar import BouncyBar
from states.PercentVolume import PercentVolume
from states.FallingAstroid import FallingAstroid
from states.Circle import Circle
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
    controller = LedController([BouncyBar(), FallingAstroid(), Circle(), PercentVolume()])
    controller.update()

if __name__ == "__main__":
    main()
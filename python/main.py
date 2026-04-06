from states.SmileTest import Smile
from states.FrownTest import Frown
from states.Rotate import Rotate
from classes.LedController import LedController

def main():
    controller = LedController([Rotate(), Smile(), Frown()])
    controller.update()

if __name__ == "__main__":
    main()
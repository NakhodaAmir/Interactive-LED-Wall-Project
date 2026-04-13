from python.states.SmileTest import Smile
from python.states.FrownTest import Frown
from python.states.Rotate import Rotate
from python.classes.LedController import LedController
from states.FallingAstroid import FallingAstroid


def main():
    controller = LedController([FallingAstroid()])
    controller.update_test()

if __name__ == "__main__":
    main()

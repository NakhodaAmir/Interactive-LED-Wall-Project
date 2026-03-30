from arduino.app_utils import *
import time

tick = 0

logo = [0,0,0,0,0,0,0,0,0,0,0,0,0,
        0,0,0,0,0,0,0,0,0,0,0,0,0,
        0,0,1,1,0,0,0,0,0,1,1,0,0,
        0,0,1,1,0,0,0,0,0,1,1,0,0,
        0,0,0,0,0,0,0,0,0,0,0,0,0,
        0,0,1,0,0,0,0,0,0,0,1,0,0,
        0,0,0,1,1,1,1,1,1,1,0,0,0,
        0,0,0,0,0,0,0,0,0,0,0,0,0]

while True:
    encoded = ",".join(str(x) for x in logo)
    Bridge.call("drawMatrix", encoded)
    tick += 1
    time.sleep(0.01)
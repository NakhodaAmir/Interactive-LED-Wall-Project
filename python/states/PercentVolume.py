from classes.LedState import LedState
import numpy as np
from classes.MicController import getAudio

class PercentVolume(LedState):
    def __init__(self):
        super().__init__(0)
        self.digit_partition_dic = {
    "top" : [[0,0,0,0,0,0,0,0,0],
             [0,1,1,1,1,1,1,1,0],
             [0,0,1,1,1,1,1,0,0],
             [0,0,0,0,0,0,0,0,0],
             [0,0,0,0,0,0,0,0,0],
             [0,0,0,0,0,0,0,0,0],
             [0,0,0,0,0,0,0,0,0],
             [0,0,0,0,0,0,0,0,0],
             [0,0,0,0,0,0,0,0,0],
             [0,0,0,0,0,0,0,0,0],
             [0,0,0,0,0,0,0,0,0],
             [0,0,0,0,0,0,0,0,0],
             [0,0,0,0,0,0,0,0,0],
             [0,0,0,0,0,0,0,0,0]],
    "top_left" : [[0,0,0,0,0,0,0,0,0],
                  [0,0,0,0,0,0,0,0,0],
                  [0,1,0,0,0,0,0,0,0],
                  [0,1,1,0,0,0,0,0,0],
                  [0,1,1,0,0,0,0,0,0],
                  [0,1,1,0,0,0,0,0,0],
                  [0,1,1,0,0,0,0,0,0],
                  [0,0,0,0,0,0,0,0,0],
                  [0,0,0,0,0,0,0,0,0],
                  [0,0,0,0,0,0,0,0,0],
                  [0,0,0,0,0,0,0,0,0],
                  [0,0,0,0,0,0,0,0,0],
                  [0,0,0,0,0,0,0,0,0],
                  [0,0,0,0,0,0,0,0,0],],
    "center" : [[0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0],
                [0,0,0,1,1,1,0,0,0],
                [0,0,0,1,1,1,0,0,0],
                [0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0]]
    }
        self.digit_partition_dic["bottom"] = np.flipud(self.digit_partition_dic["top"]);
        self.digit_partition_dic["bottom_left"] = np.flipud(self.digit_partition_dic["top_left"]);
        self.digit_partition_dic["top_right"] = np.fliplr(self.digit_partition_dic["top_left"]);
        self.digit_partition_dic["bottom_right"] = np.flipud(self.digit_partition_dic["top_right"]);
        self.digits_dic = {
            0: self.digit_partition_dic["top"] + self.digit_partition_dic["bottom_left"]
               + self.digit_partition_dic["bottom_right"] + self.digit_partition_dic["bottom"]
               + self.digit_partition_dic["top_left"] + self.digit_partition_dic["top_right"],
            1: self.digit_partition_dic["top_right"] + self.digit_partition_dic["bottom_right"],
            2: self.digit_partition_dic["top"] + self.digit_partition_dic["top_right"]
               + self.digit_partition_dic["center"] + self.digit_partition_dic["bottom_left"]
               + self.digit_partition_dic["bottom"],
            3: self.digit_partition_dic["top"] + self.digit_partition_dic["top_right"]
               + self.digit_partition_dic["center"] + self.digit_partition_dic["bottom_right"]
               + self.digit_partition_dic["bottom"],
            4: self.digit_partition_dic["top_left"] + self.digit_partition_dic["top_right"]
               + self.digit_partition_dic["bottom_right"] + self.digit_partition_dic["center"],
            5: self.digit_partition_dic["top"] + self.digit_partition_dic["bottom"]
               + self.digit_partition_dic["center"] + self.digit_partition_dic["bottom_right"]
               + self.digit_partition_dic["top_left"],
            6: self.digit_partition_dic["top"] + self.digit_partition_dic["bottom_right"]
               + self.digit_partition_dic["top_left"] + self.digit_partition_dic["bottom"]
               + self.digit_partition_dic["bottom_left"] + self.digit_partition_dic["center"],
            7: self.digit_partition_dic["top"] + self.digit_partition_dic["top_right"]
               + self.digit_partition_dic["bottom_right"],
            8: self.digit_partition_dic["top"] + self.digit_partition_dic["bottom_left"]
               + self.digit_partition_dic["bottom_right"] + self.digit_partition_dic["bottom"]
               + self.digit_partition_dic["top_left"] + self.digit_partition_dic["top_right"]
               + self.digit_partition_dic["center"],
            9: self.digit_partition_dic["top"] + self.digit_partition_dic["bottom_right"]
               + self.digit_partition_dic["bottom"] + self.digit_partition_dic["top_left"]
               + self.digit_partition_dic["top_right"] + self.digit_partition_dic["center"],
        }

    def calculate_array(self, step):
        bass = round(getAudio()["Bass"])
        return super().calculate_array(self.display_digit(bass))

    def display_digit(self, digit):
        first_digit = digit % 10
        second_digit = digit // 100 % 10
        first_digit_arr = self.digits_dic[first_digit]
        second_digit_arr = self.digits_dic[second_digit]
        gap = np.zeros([14, 1], int)
        return np.hstack((second_digit_arr, gap, first_digit_arr))









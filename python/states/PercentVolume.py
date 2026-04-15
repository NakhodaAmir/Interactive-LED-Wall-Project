from classes.LedState import LedState
import numpy as np
from classes.MicController import getAudio, getVolume


class PercentVolume(LedState):
    def __init__(self):
        super().__init__(0.01)
        self.digit_partition_dic = {
    # "top" : [[0,0,0,0,0,0,0,0,0],
    #          [0,1,1,1,1,1,1,1,0],
    #          [0,0,1,1,1,1,1,0,0],
    #          [0,0,0,0,0,0,0,0,0],
    #          [0,0,0,0,0,0,0,0,0],
    #          [0,0,0,0,0,0,0,0,0],
    #          [0,0,0,0,0,0,0,0,0],
    #          [0,0,0,0,0,0,0,0,0],
    #          [0,0,0,0,0,0,0,0,0],
    #          [0,0,0,0,0,0,0,0,0],
    #          [0,0,0,0,0,0,0,0,0],
    #          [0,0,0,0,0,0,0,0,0],
    #          [0,0,0,0,0,0,0,0,0],
    #          [0,0,0,0,0,0,0,0,0]],
    # "top_left" : [[0,0,0,0,0,0,0,0,0],
    #               [0,0,0,0,0,0,0,0,0],
    #               [0,1,0,0,0,0,0,0,0],
    #               [0,1,1,0,0,0,0,0,0],
    #               [0,1,1,0,0,0,0,0,0],
    #               [0,1,1,0,0,0,0,0,0],
    #               [0,1,1,0,0,0,0,0,0],
    #               [0,0,0,0,0,0,0,0,0],
    #               [0,0,0,0,0,0,0,0,0],
    #               [0,0,0,0,0,0,0,0,0],
    #               [0,0,0,0,0,0,0,0,0],
    #               [0,0,0,0,0,0,0,0,0],
    #               [0,0,0,0,0,0,0,0,0],
    #               [0,0,0,0,0,0,0,0,0],],
    # "center" : [[0,0,0,0,0,0,0,0,0],
    #             [0,0,0,0,0,0,0,0,0],
    #             [0,0,0,0,0,0,0,0,0],
    #             [0,0,0,0,0,0,0,0,0],
    #             [0,0,0,0,0,0,0,0,0],
    #             [0,0,0,0,0,0,0,0,0],
    #             [0,0,0,1,1,1,0,0,0],
    #             [0,0,0,1,1,1,0,0,0],
    #             [0,0,0,0,0,0,0,0,0],
    #             [0,0,0,0,0,0,0,0,0],
    #             [0,0,0,0,0,0,0,0,0],
    #             [0,0,0,0,0,0,0,0,0],
    #             [0,0,0,0,0,0,0,0,0],
    #             [0,0,0,0,0,0,0,0,0]]

            "top" : [[0,0,0,0,0,0],
                    [0,1,1,1,1,0],
                    [0,0,0,0,0,0],
                    [0,0,0,0,0,0],
                    [0,0,0,0,0,0],
                    [0,0,0,0,0,0],
                    [0,0,0,0,0,0],
                    [0,0,0,0,0,0]],
            "top_left" : [[0,0,0,0,0,0],
                        [0,0,0,0,0,0],
                        [0,1,0,0,0,0],
                        [0,1,0,0,0,0],
                        [0,0,0,0,0,0],
                        [0,0,0,0,0,0],
                        [0,0,0,0,0,0],
                        [0,0,0,0,0,0]],
            "center" : [[0,0,0,0,0,0],
                        [0,0,0,0,0,0],
                        [0,0,0,0,0,0],
                        [0,0,0,1,0,0],
                        [0,0,1,0,0,0],
                        [0,0,0,0,0,0],
                        [0,0,0,0,0,0],
                        [0,0,0,0,0,0]]
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

        self.precomputed_volumes = {}
        gap = np.zeros([8, 1], int)#CHANGE CONSTAMST
        for vol in range(100):
            first_digit = vol % 10
            second_digit = vol // 10 % 10
            self.precomputed_volumes[vol] = np.hstack((
                self.digits_dic[second_digit],
                gap,
                self.digits_dic[first_digit]
            ))

    def calculate_array(self):
        raw_volume = round(getVolume())
        if raw_volume < 1:
            volume = 0
        else:
            volume = max(min(round(getVolume()), 99), 0)
        return super().calculate_array(self.precomputed_volumes[volume])

# percent = PercentVolume()
# encoded = ",".join(str(x) for x in percent.calculate_array())
# chunks = [encoded[i:i + 26] for i in range(0, len(encoded), 26)]
# for chunk in chunks:
#     print(chunk)
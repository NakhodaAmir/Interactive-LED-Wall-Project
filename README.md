# Intereactive LED Wall
> An Arduino Uno Q project which displays a 19x14 LED strip wall interactively with sound detection from a microphone.

## 🏆 Showcase

## ✨ Features
| Features | Description |
| -------- | ----------- |
| [Bouncy Bar](../main/python/states/BouncyBar.py) | Displays a bouncy bar on the LED wall. Features of the bouncy bar are relative to the detected audio's sub bass, bass, mid, treble and air |
| [Circle](../main/python/states/Circle.py) | Draws a circle on the LED wall. Size of the circle is relative to the volume detected |
| [Falling Astroid](../main/python/states/FallingAstroid.py) | Draws astroids falling from the top of the LED wall. Size of the astroids is relative to the volume detected |
| [Percent Volume](../main/python/states/PercentVolume.py) | Displays a percentage(0-99) of how loud the detected audio is on the LED wall |

## 🚀 Quick Start
### Prerequisite
* **Arduino Uno Q**
* **Arduino App Lab**
* **19x14 LED Wall**
### Install
Clone the repo to your local directory and upload the folder to the Arduino Uno Q
```bash
git clone https://github.com/NakhodaAmir/Interactive-LED-Wall-Project.git
```
### Run
Power the Arduino Uno Q

## 📦 API Reference
### [sketch](../main/sketch/sketch.ino)
> **Purpose:** Serves as the primary application controller that orchestrates the program’s lifecycle by initializing hardware in the `setup()` function and executing the core logic continuously within the `loop()`.
* **Key Methods:**
    * `drawMatrixBuiltIn(String)`: Interopable function that takes an inputed `String` which represents the LED wall behavoir. Draws on the LED wall respective to that `String`.
* **State:`currentMode`** Stores the modes of which LED state to display on the LED wall.
* **State:`lastButtonState`** Keeps track of when a button press has been detected to change mode.
---
### [LedController](../main/python/classes/LedController.py)
> **Purpose:** Acts as the state machine; controlling all LED state behaviour; encoding a computed matrix from each state into a `String` to be passed to the `sketch.ino`
* **Key Methods:**
    * `changeMode(int)`: Interopable function that changes the mode of the LED wall to display a different state.
    * `update`: The main loop of the LED wall behaviour. Will continuosly call the respective LED state behavior to be displayed
---
### [LedState](../main/python/classes/LedState.py)
> **Purpose:** Abstarct class of each state LED State. Contains the necessary blooprint of what every LED state needs to extend
* **Key Methods:**
    * `coloredArray(int[int[]], int[], int[])`: Converts a 2D matrix of 1s and 0s by replacing each 1 value with an array representing an RGB value.
    * `calculate_array(int[int[]]) -> int[]`: Converts a 2D matrix into a 1D array.
* **State:`frame_rate`** Stores how long to wait(in seconds) for each call back to `calculate_array(int[int[]]) -> int[]`.
---
### [MicController](../main/python/classes/MicController.py)
> **Purpose:** A static helper class to retrive audio information(sub bass, bass, mid, treble and air).
* **Key Methods:**
    * `getAudio() -> dict{String : float}`: Gets data from the connected microphone and returns the respective audio information in a dictionary.
    * `getVolume() -> float`: Returns the raw audio volume detected .

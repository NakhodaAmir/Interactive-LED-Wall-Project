#include <Arduino_LED_Matrix.h>
#include <Arduino_RouterBridge.h>

Arduino_LED_Matrix matrix;
uint8_t LedMatrix[104];

int currentMode = 0;

void drawMatrixBuiltIn(String encoded) {
  int i = 0;
  while (encoded.length() > 0 && i < 104) {
    int comma = encoded.indexOf(',');
    if (comma == -1) { LedMatrix[i] = encoded.toInt(); break; }
    LedMatrix[i] = encoded.substring(0, comma).toInt();
    encoded = encoded.substring(comma + 1);
    i++;
  }
  matrix.draw(LedMatrix);
}

void setup() {
  matrix.begin();
  matrix.setGrayscaleBits(1);
  Bridge.begin();
  Bridge.provide("drawMatrix", drawMatrixBuiltIn);
}

void loop() {
  Bridge.update();
  Bridge.call("ChangeMode", currentMode); 
  currentMode++;
  delay(10);                   
}

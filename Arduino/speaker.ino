#include <SoftwareSerial.h>
#include <DFPlayer_Mini_Mp3.h>

int state;
SoftwareSerial DFmini(2, 3); // RX/TX setting , generate MP3 module object
SoftwareSerial humtem(10, 11); // RX/TX setting, generate humtem(huminity & temperature object)

void setup() {
    Serial.begin(9600);
    humtem.begin(9600);
    DFmini.begin(9600);
    mp3_set_serial (DFmini); // DFPlayer-mini mp3 module serial setting
    delay(1);
    mp3_set_volume(30); // volume setting
}

void loop() {
    humtem.listen(); // read master's humtem data 
    if(humtem.available() > 0) {
        state = humtem.read();
        Serial.println(state);
        // mp3 output
        if(state == 1) {
            mp3_play(1); // Only when the temperature is high
            delay(4000);
            Serial.println("Temp needs to be resetting");
        } else if(state == 2) {
            mp3_play(2); // Only when the humidity is high
            delay(4000);
            Serial.println("Humidity needs to be resetting");
        } else if(state == 3) {
            mp3_play(3); // when the temperature&huminity is high
            delay(4000);
            Serial.println("Humidity and Temp needs to be resetting");
        }
    }
}
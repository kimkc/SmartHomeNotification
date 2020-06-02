#include <SoftwareSerial.h>
#include <stdlib.h>
#include <string.h>
#define DEBUG true
#include <SimpleTimer.h>
#include <DFPlayer_Mini_Mp3.h>

int infraed = 7; // infraed sensor pin
int sound = 3; // sound sensor pin
int value = 0; // thingspeak value
SimpleTimer timer; // declare that we will use timer
String apiKey = "YOUR_THINGSPEAK_API_KEY"; // my thingspeak channel Write API key
SoftwareSerial esp8266(2, 3); // RX/TX setting, generate esp8266 object
SoftwareSerial DFmini(10, 11); // RX/TX setting, generate MP3 module object

void setup() {
    // set infraed sensor as input
    pinMode(infraed, INPUT);
    
    // setting serial baudrate 9600
    Serial.begin(9600);
    
    // begin softwareserial
    esp8266.begin(9600);
    DFmini.begin(9600);
    mp3_set_serial (DFmini); // set Serial for DFPlayer-mini mp3 module
    delay(1); // wait 1ms for mp3 module to set volume
    mp3_set_volume(30);
}

void loop() {
    esp8266.listen(); // read esp 8266
    
    // state of infraed sensor
    // when it detects person : high, when there are no people : low
    int state = digitalRead(infraed);
    timer.setTimeout(3600000, ResetValue); // reset the value to "0" every hour
    timer.run(); // run timer
    
    // Show thingspeak graph
    if(state == 1) {
        String cmd = "AT+CIPSTART=\"TCP\",\"";
        cmd += "184.106.153.149"; // api.thingspeak.com connecting IP
        cmd += "\",80"; // api.thingspeak.com connecting port, 80
        esp8266.println(cmd);
        if(esp8266.find("Error")) {
            Serial.println("AT+CIPSTART error");
            return;
        }
   
        // to send String and Data setting by using 'GET' way
        String getStr = "GET /update?api_key=";
        getStr += apiKey;
        getStr += "&field1=";
        getStr += String(value);
        getStr += "\r\n\r\n";
        value += 1;
        
        // Send Data
        cmd = "AT+CIPSEND=";
        cmd += String(getStr.length());
        esp8266.println(cmd);
        Serial.println("infraed sensor on!"); // For checking whether data is sent
        if(esp8266.find(">")) {
            esp8266.print(getStr);
        } else {
            esp8266.println("AT+CIPCLOSE");
            // alert uesp8266
            Serial.println("AT+CIPCLOSE");
        }
        
        mp3_play(1); // if anyone comes my private zone , alarm will be go off.
        delay(4000);
        Serial.println("anyone comes my private zone");
        delay(10000);
    } else {
        Serial.println("infraed sensor off");
        delay(2000);
    }
}

void ResetValue() {
    value = 0;
}

// declare function to find out ESP8266's information and set
String sendData(String command, const int timeout, boolean debug) {
    String response = "";
    esp8266.print(command); // send command to ESP8266
    long int time = millis();
    while((time+timeout) > millis()) {
        while(esp8266.available()) {
            // to print out esp data which esp
            char c = esp8266.read(); // read the next character
            response += c;
        }
    }
    if(debug) {
        Serial.print(response);
    }
    return response;
}
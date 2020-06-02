#include <SoftwareSerial.h>
#include <stdlib.h>
#include "DHT.h"          // include DHT.h library(huminity & temperature sensor library)
#define DEBUG true
#define DHTPIN 7          // define DHTpin as 7th pin(DATA pin)
#define DHTTYPE DHT11     //define DHTtype as DHT 11

DHT dht(DHTPIN, DHTTYPE); // DHT setting
String apiKey = "YOUR_THINGSPEAK_API_KEY"; // my thingspeak channel Write API key
String income_wifi = ""; // response value which comes from the server
float temp;
float humi;
String wifi_temp = ""; // only data response value which comes from the server
SoftwareSerial humtem(10, 11); // RX/TX setting, humtem(generate huminity & temperature object)
SoftwareSerial esp8266(2, 3); // RX/TX setting, generate esp8266 object

void setup() {
    // setting serial baudrate 9600
    Serial.begin(9600);
    // begin softwareserial
    esp8266.begin(9600);
    humtem.begin(9600);
}
 
void loop() {
    esp8266.listen(); // read esp 8266
    // TCP connect
    String cmd1 = "AT+CIPSTART=\"TCP\",\"";
    cmd1 += "184.106.153.149"; // api.thingspeak.com connecting IP
    cmd1 += "\",80"; // api.thingspeak.com connecting server, 80
    esp8266.println(cmd1);
    if(esp8266.find("Error")) {
        Serial.println("AT+CIPSTART error");
        return;
    }
    temp = dht.readTemperature(); // read temperature
    humi = dht.readHumidity(); // read humidity
    
    // to send String and Data setting by using 'GET' way
    String getstr1 = "GET /update?api_key=";
    getstr1 += apiKey;
    getstr1 += "&field2=";
    getstr1 += String(temp);
    getstr1 += "&field3=";
    getstr1 += String(humi);
    getstr1 += "\r\n\r\n";
 
    // Send Data
    cmd1 = "AT+CIPSEND=";
    cmd1 += String(getstr1.length());
    esp8266.println(cmd1); 
    if(esp8266.find(">")) {
        esp8266.print(getstr1);
    } else {
        esp8266.println("AT+CIPCLOSE");
        // alert uesp8266
        Serial.println("AT+CIPCLOSE");
    }

    Serial.print("\t");
    Serial.print("온도: ");
    Serial.print(temp);
    Serial.print(" C\t"); // prinnt %
    Serial.print("습도: "); // print Humidity: by string
    Serial.print(humi); // print value humidity " by string
    Serial.println(" %");
    
    // to meet the 10 second upload interval of Thingspeak
    delay(10000);

    // send our server
    String cmd2 = "AT+CIPSTART=\"TCP\",\"";
    cmd2 += "106.10.35.183"; // server connecting IP
    cmd2 += "\",8080"; // server connecting port, 8080
    esp8266.println(cmd2);
    if(esp8266.find("Error")) {
        Serial.println("AT+CIPSTART error");
        return;
    }
    String getstr2 = "GET /tempNhumi?temp=";
    getstr2 += String(temp);
    getstr2 += "&humi=";
    getstr2 += String(humi);
    getstr2 += "\r\n\r\n";
    
    // Send Data
    cmd2 = "AT+CIPSEND=";
    cmd2 += String(getstr2.length());
    esp8266.println(cmd2);
    if(esp8266.find(">")) {
        esp8266.print(getstr2);
    } else {
        esp8266.println("AT+CIPCLOSE");
        // alert uesp8266
        Serial.println("AT+CIPCLOSE");
    }
    
    // extract only data values sent from the server
    if(esp8266.available()) {
        if(esp8266.find("+IPD,")) {
            income_wifi = esp8266.readStringUntil('\r');
            wifi_temp = income_wifi.substring(income_wifi.indexOf("1:")+2, income_wifi.indexOf("")-1);
            Serial.println(wifi_temp);
        }
    }
    
    // send data to dependent Arduino
    if(wifi_temp == "1") {
        humtem.write(1);
    } else if(wifi_temp == "2") {
        humtem.write(2);
    } else if(wifi_temp == "3") {
        humtem.write(3);
    } else if(wifi_temp == "0") {
        Serial.println("temp & hum are good now");
    }
    delay(3000);
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
#include "Arduino.h"
#include <Ethernet.h>
#include <SPI.h>
#include <LiquidCrystal.h>
#include <WebSocketClient.h>

LiquidCrystal lcd(8, 9, 4, 5, 6, 7);

#define btnRIGHT  0                                            //déclare les broches des boutons
#define btnUP     1
#define btnDOWN   2
#define btnLEFT   3
#define btnSELECT 4
#define btnNONE   5

byte mac[] = {0x98, 0x4F, 0xEE, 0x05, 0x37, 0xE8};
IPAddress ip(192, 168, 1, 234);
//192.168.1.10
//char server[] = "echo.websocket.org";
int readKey = 0; 
int Pin = 9;
int key = 0;

int readButtons(){                                              //Structuration des boutons
 readKey = analogRead(0);
 if(readKey < 50)   return btnRIGHT;                          
 if(readKey < 250)  return btnUP; 
 if(readKey < 450)  return btnDOWN;
 if(readKey < 650)  return btnLEFT;
 if(readKey < 850)  return btnSELECT;
 
 return btnNONE;
}

WebSocketClient client;

void setup() {
  pinMode(Pin, OUTPUT);
  lcd.begin(16, 2);
  Serial.begin(9600);
  Serial.println("connecting...");
  Serial.println("EXAMPLE: setup()");
  Ethernet.begin(mac, ip);
  client.connect("192.168.1.50",2009);
  client.onOpen(onOpen);
  client.onMessage(onMessage);
  client.onError(onError);
  client.send("Hello World!");
}

void loop() {
  client.monitor();
  
  key = readButtons();
  
   switch(key){                                                   //lecture des boutons
    case btnUP:
      lcd.clear();
      Serial.write("U");       
      delay(300);
    break;
    
    case btnDOWN:
      lcd.clear();
      Serial.write("D");  
      delay(300);
    break;
    
    case btnRIGHT:
      lcd.clear(); 
      Serial.write("R");
      delay(300);
    break;

    case btnLEFT:
      lcd.clear();
     Serial.write("L"); 
      delay(300);
    break;
    
    case btnSELECT:
      lcd.clear();
     Serial.write("S"); 
      delay(300);
    break;
  }	
}

void onOpen(WebSocketClient client) {
  Serial.println("EXAMPLE: onOpen()");
}

void onMessage(WebSocketClient client, char* message) {
  Serial.println("EXAMPLE: onMessage()");
  Serial.print("Received: "); Serial.println(message);
}

void onError(WebSocketClient client, char* message) {
  Serial.println("EXAMPLE: onError()");
  Serial.print("ERROR: "); Serial.println(message);
}

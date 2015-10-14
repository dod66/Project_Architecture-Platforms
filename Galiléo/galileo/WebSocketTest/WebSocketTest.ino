#include <WebSocketClient.h>
#include <Ethernet.h>
#include <SPI.h>
#include <LiquidCrystal.h>


LiquidCrystal lcd(8, 9, 4, 5, 6, 7);

#define btnRIGHT  0                                            //déclare les broches des boutons
#define btnUP     1
#define btnDOWN   2
#define btnLEFT   3
#define btnSELECT 4
#define btnNONE   5

byte mac[] = {0x98, 0x4F, 0xEE, 0x05, 0x37, 0xE8};
IPAddress ip(192, 168, 1, 211);
byte server[] = {192, 168, 1, 50};

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

char *json = "{\"path\":\"/api/v1/actor/perform/device/lighting\",\"requestID\":\"2\",\"perform\":\"off\",\"parameter\":\"\"}";

EthernetClient client;
WebSocketClient webSocketClient;

void setup() { 
  pinMode(Pin, OUTPUT);
  lcd.begin(16, 2);
  
  Serial.begin(9600);
  while(!Serial) {  }

  if (Ethernet.begin(mac) == 0) {
    Serial.println("Error: Failed to configure Ethernet using DHCP");
    while(1) {  }
  } 
  
//  Serial.println("connecting...");
//  int erreur = client.connect("192.168.1.50", 800);
  
//  if (client.connected()) {
//    Serial.println("connected");
//    client.println("GET /diviceConnect HTTP/1.0");
//    client.println();
//  } else {
//    Serial.println("connection failed");
//  }
}

void loop() {
  String data;
  
  int erreur = client.connect("192.168.1.50", 800);
  if(client.connected()) {
    Serial.println("Connected");
    webSocketClient.path = "/manage";
    webSocketClient.host = "dastardly.local";
    if (webSocketClient.handshake(client)) {
        Serial.println("Handshake successful");
    } else {
        Serial.println("Handshake failed.");
        while(1) {
          // Hang on failure
        }  
    }
    webSocketClient.sendData(json);
  } 
   if (client.connected()) {
    data = webSocketClient.get();
    if (data.length() > 0) {
      Serial.print("Received data: ");
      Serial.println(data);
      client.stop();
    }
  }
  
  
  
}

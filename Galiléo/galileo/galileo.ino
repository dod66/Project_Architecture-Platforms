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

EthernetClient client;
WebSocketClient webSocketClient;

void setup() {
  
  Ethernet.begin(mac, ip);
  
  Serial.begin(9600); 
  pinMode(Pin, OUTPUT);
  lcd.begin(16, 2);
  
  Serial.println("connecting...");
  int erreur = client.connect("192.168.1.50", 800);
  
  if (client.connected()) {
    Serial.println("connected");
    client.println("GET /diviceConnect HTTP/1.0");
    client.println();
  } else {
    Serial.println("connection failed");
  }
  
}

void loop() {
  
  if (client.available()) {
    char c = client.read();
    Serial.print(c);
  }

  if (!client.connected()) {
    Serial.println();
    Serial.println("disconnecting.");
    client.stop();
    for(;;)
      ;
  }
  
  webSocketClient.monitor();
  
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

void dataArrived(WebSocketClient webSocketClient, String data) {
  Serial.println("Data Arrived: " + data);
}

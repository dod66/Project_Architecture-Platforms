#include <aJSON.h>

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

char* parseJson(char *jsonString) ;

// Json string to parse
char jsonString[] = "{\"query\":{\"count\":1,\"created\":\"2012-08-04T14:46:03Z\",\"lang\":\"en-US\",\"results\":{\"item\":{\"title\":\"Handling FTP usernames with @ in them\"}}}}";

void setup() {
  pinMode(Pin, OUTPUT);
  lcd.begin(16, 2);
  Serial.begin(9600);
  Serial.println("Connexion...");
  
  Ethernet.begin(mac, ip);
  client.connect("192.168.1.10",6000);
  client.onOpen(onOpen);
  client.onMessage(onMessage);
  client.onError(onError);
  
  Serial.println(jsonString);
  
  char* value = parseJson(jsonString);

    if (value) {
        Serial.println("Successfully Parsed: ");
        Serial.println(value);
    } else {
        Serial.println("There was some problem in parsing the JSON");
    }
}

char* parseJson(char *jsonString) 
{
    char* value;

    aJsonObject* root = aJson.parse(jsonString);

    if (root != NULL) {
        //Serial.println("Parsed successfully 1 " );
        aJsonObject* query = aJson.getObjectItem(root, "query"); 

        if (query != NULL) {
            //Serial.println("Parsed successfully 2 " );
            aJsonObject* results = aJson.getObjectItem(query, "results"); 

            if (results != NULL) {
                //Serial.println("Parsed successfully 3 " );
                aJsonObject* item = aJson.getObjectItem(results, "item"); 

                if (item != NULL) {
                    //Serial.println("Parsed successfully 4 " );
                    aJsonObject* title = aJson.getObjectItem(item, "title"); 
                    
                    if (title != NULL) {
                        //Serial.println("Parsed successfully 5 " );
                        value = title->valuestring;
                    }
                }
            }
        }
    }

    if (value) {
        return value;
    } else {
        return NULL;
    }
}

void loop() {
  client.monitor();
  
  serialEvent();
  key = readButtons();
  
   switch(key){                                                   //lecture des boutons
    case btnUP:
      lcd.clear();
      Serial.write("U");
      client.send("Salut");      
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

void serialEvent(){
  if(Serial.available()){                                  //Tant que le port serie parle
    String string=Serial.readStringUntil('\n');            //Stoque ce qui est lu sur le port série dans string
    lcd.clear();                                           //nettoie l'écran lcd
    lcd.print(string);                                     //écrit sur l'écran lcd
  }
}

void onOpen(WebSocketClient client) {
  Serial.println("Connection etablie...");
}

void onMessage(WebSocketClient client, char* message) {
  Serial.println("EXAMPLE: onMessage()");
  Serial.print("Received: "); Serial.println(message);
}

void onError(WebSocketClient client, char* message) {
  //Serial.println("EXAMPLE: onError()");
  Serial.print("ERROR: "); Serial.println(message);
}

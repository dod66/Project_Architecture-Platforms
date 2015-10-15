#include <aJSON.h>
#include "Arduino.h"
#include <Ethernet.h>
#include <SPI.h>
#include <LiquidCrystal.h>
#include <WebSocketClient.h>
//initialisation des broches de l'écran lcd
LiquidCrystal lcd(8, 9, 4, 5, 6, 7);
//déclare les broches des boutons
#define btnRIGHT  0                                            
#define btnUP     1
#define btnDOWN   2
#define btnLEFT   3
#define btnSELECT 4
#define btnNONE   5
// adresse mac du port ethernet du Galileo
byte mac[] = {0x98, 0x4F, 0xEE, 0x05, 0x37, 0xE8};
IPAddress ip(192, 168, 1, 215);
//192.168.1.10

int readKey = 0; 
int Pin = 9;
int key = 0;
//initialisation des boutons
int readButtons(){                                              //Structuration des boutons
 readKey = analogRead(0);
 if(readKey < 50)   return btnRIGHT;                          
 if(readKey < 250)  return btnUP; 
 if(readKey < 450)  return btnDOWN;
 if(readKey < 650)  return btnLEFT;
 if(readKey < 850)  return btnSELECT;
 
 return btnNONE;
}
//declaration du client http
EthernetClient httpClient;
//declaration du client websocket
WebSocketClient client;
//definition de la fonction
char* parseJson(char *jsonString) ;
//exemple trame json pour la distance
char jsonString[] = "{\"rootObject\":{\"cabInfo\":{\"odometer\":\"20\",\"destination\":\"null\",\"loc_now\":\"test\",\"loc_prior\":\"test\"}}}";

void setup() {
  //initialisation de la broche de sortie
  pinMode(Pin, OUTPUT);
  //definition de la taille du lcd 16 caracteres sur 2 lignes
  lcd.begin(16, 2);
  lcd.print("Take passenger ?");
  lcd.setCursor(4,2);
  lcd.print("YES");
  lcd.setCursor(10,2);
  lcd.print("NO");
  lcd.setCursor(3,2);
  lcd.print(">");
  //definition de la vitesse de la trame en 9600 bodes
  Serial.begin(9600);
  Serial.println("Connexion...");
  //initialise le port ethernet avec l adresse mac et une ip
  Ethernet.begin(mac, ip);
  //connection au serveur http 
  int erreur = httpClient.connect("192.168.1.10",5000);
  //si la connection est reussite
  if (httpClient.connected()) {
    Serial.println("Connected");
    //on envoie un get sur le serveur
    httpClient.println("GET /deviceConnect HTTP/1.0");
    httpClient.println();
  } else {
    Serial.println("Connection failed");
  }
  
  if (httpClient.available()) {
    char c = httpClient.read();
    Serial.print(c);
  }
  //connection au serveur websocket
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
//parse du json
char* parseJson(char *jsonString) 
{
    char* value;

    aJsonObject* trame = aJson.parse(jsonString);

    if (trame != NULL) {
        //Serial.println("Parsed successfully 1 " );
        aJsonObject* rootObject = aJson.getObjectItem(trame, "rootObject"); 

        if (rootObject != NULL) {
            //Serial.println("Parsed successfully 2 " );
            aJsonObject* cabInfo = aJson.getObjectItem(rootObject, "cabInfo"); 

            if (cabInfo != NULL) {
                //Serial.println("Parsed successfully 3 " );
                aJsonObject* odometer = aJson.getObjectItem(cabInfo, "odometer");
                    
                if (odometer != NULL) {
                    //Serial.println("Parsed successfully 5 " );
                    value = odometer->valuestring;
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
      client.send("test");  
      delay(300);
    break;
    
    case btnRIGHT:
      lcd.clear(); 
      Serial.write("R");
      lcd.clear();
      lcd.print("Take passenger ?");
      lcd.setCursor(4,2);
      lcd.print("YES");
      lcd.setCursor(10,2);
      lcd.print("NO");
      lcd.setCursor(9,2);
      lcd.print(">"); 
      delay(300);
    break;

    case btnLEFT:
      lcd.clear();
      Serial.write("L");
      lcd.clear();
      lcd.print("Take passenger ?");
      lcd.setCursor(4,2);
      lcd.print("YES");
      lcd.setCursor(10,2);
      lcd.print("NO");
      lcd.setCursor(3,2);
      lcd.print(">"); 
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
//fonction detection de connection sur websocket
void onOpen(WebSocketClient client) {
  Serial.println("Connection etablie...");
}
//fonction reception de messages par websocket
void onMessage(WebSocketClient client, char* message) {
  Serial.println("EXAMPLE: onMessage()");
  Serial.print("Received: "); Serial.println(message);
}
//fonction detection d erreurs connection websocket
void onError(WebSocketClient client, char* message) {
  Serial.println("EXAMPLE: onError()");
  Serial.print("ERROR: "); Serial.println(message);
}

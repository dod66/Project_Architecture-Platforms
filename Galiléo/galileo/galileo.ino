#include "Arduino.h"
#include <Ethernet.h>
#include <SPI.h>
#include <LiquidCrystal.h>
//bibliothèques externes
#include <aJSON.h>
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

const int Pin = 9;
// adresse mac du port ethernet du Galileo
byte mac[] = {0x98, 0x4F, 0xEE, 0x05, 0x37, 0xE8};
IPAddress ip(192, 168, 1, 215);
//192.168.1.10

int readKey = 0; 
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


void setup() {
  //initialisation de la broche de sortie
  pinMode(Pin, OUTPUT);
  //definition de la taille du lcd 16 caracteres sur 2 lignes
  lcd.begin(16, 2);
  takePassengerYesLCD();
  //definition de la vitesse de la trame en 9600 bodes
  Serial.begin(9600);
  Serial.println("Connexion...");
  //initialise le port ethernet avec l adresse mac et une ip
  Ethernet.begin(mac, ip);
  
  //connexion au serveur http 
  int erreur = httpClient.connect("192.168.1.10",5000);
  //si la connexion est reussite
  if (httpClient.connected()) {
    Serial.println("Connected");
    //on envoie un get sur le serveur
    httpClient.println("GET /deviceConnect HTTP/1.0");
    httpClient.println();
  } 
  else {
    Serial.println("Connection failed");
  }

  if (httpClient.available()) {
    char c = httpClient.read();
    Serial.print(c);
  }
  //connexion au serveur websocket
  client.connect("192.168.1.10", 5000);
  //ouverture du socket et connexion
  client.onOpen(onOpen);
  //écoute des messages du serveur
  client.onMessage(onMessage);
  //détection d'échec de connexion
  client.onError(onError);
  
}


void loop() {
  int yesOrNo;

  client.monitor();
  //client.onMessage(onMessage);
  
  serialEvent();
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
    takePassengerNoLCD();
    delay(1000);
    signalTakePassenger(2);
    delay(300);
    break;

  case btnLEFT:
    lcd.clear();
    Serial.write("L");
    takePassengerYesLCD();
    delay(1000);
    signalTakePassenger(1);
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
  if(Serial.available()){
    //Stocke ce qui est lu sur le port série dans string    
    String string=Serial.readStringUntil('\n');            
    lcd.clear();                                          
    lcd.print(string);                                     
  }
}
//fonction detection yes ou no
String signalTakePassenger(int yesOrNo) {
  int i;
  //création d'un objet json
  aJsonObject* root = aJson.createObject();
  
  //si on decide de prendre la course
  if (yesOrNo == 1) {
    //création d'une trame json pour valider la course au serveur
    aJson.addItemToObject(root, "takePassenger", aJson.createItem("Yes"));
    char* string = aJson.print(root);
    if (string != NULL) {
      Serial.println(string);
      //envoie de la trame json
      client.send(string);
    }
  }
  //si on refuse de prendre la course
  if (yesOrNo == 2) {
    //création d'une trame json pour annuler la course au serveur
    aJson.addItemToObject(root, "takePassenger", aJson.createItem("No"));
    char* string = aJson.print(root);
    if (string != NULL) {
      Serial.println(string);
      //envoie de la trame json
      client.send(string);
    }
  }
}

//fonction d'affichage du Yes à l'écran
void takePassengerYesLCD() {
  lcd.clear();
  lcd.print("Take passenger ?");
  lcd.setCursor(4,2);
  lcd.print("YES");
  lcd.setCursor(10,2);
  lcd.print("NO");
  lcd.setCursor(3,2);
  lcd.print(">");
}

//fonction d'affichage du No à l'écran
void takePassengerNoLCD() {
  lcd.clear();
  lcd.print("Take passenger ?");
  lcd.setCursor(4,2);
  lcd.print("YES");
  lcd.setCursor(10,2);
  lcd.print("NO");
  lcd.setCursor(9,2);
  lcd.print(">");  
}

//fonction detection de connection sur websocket
void onOpen(WebSocketClient client) {
  Serial.println("Connection etablie...");
}


//fonction reception de messages par websocket
void onMessage(WebSocketClient client, char* message) {
  char* TargetPos;
  Serial.println("EXAMPLE: onMessage()");
  Serial.print("Received: ");
  
  TargetPos = parseJsonTargetPos(message);
  Serial.println(TargetPos);
  if (TargetPos != "None") {
    distanceLCD(parseJsonOdometer(message));
  }
}

String distanceLCD(char* distance) {
  String mess;
  lcd.clear();
  mess = String(distance);
  lcd.print("Distance :" + mess + " km" );
  
  return mess;
}

//fonction detection d erreurs connection websocket
void onError(WebSocketClient client, char* message) {
  Serial.println("EXAMPLE: onError()");
  Serial.print("ERROR: "); 
  Serial.println(message);
}

//fonction json parse avec récupération de la distance 
char* parseJsonOdometer(char *jsonString) {
  char* value;
  Serial.println(jsonString);
  aJsonObject* trame = aJson.parse(jsonString); 

    if (trame != NULL) {
      aJsonObject* cabInfo = aJson.getObjectItem(trame, "cabInfo"); 

      if (cabInfo != NULL) {
        aJsonObject* odometer = aJson.getObjectItem(cabInfo, "odometer");

        if (odometer != NULL) {
          value = odometer->valuestring;
        }

      }
    }

  if (value) {
    return value;
  } 
  else {
    return NULL;
  }
}

//fonction json parse avec récupération de loc_now
char* parseJsonTaxiPos(char *jsonString) 
{
  char* value;

  aJsonObject* trame = aJson.parse(jsonString);

    if (trame != NULL) {
      aJsonObject* cabInfo = aJson.getObjectItem(trame, "cabInfo"); 

      if (cabInfo != NULL) {
        aJsonObject* loc_now = aJson.getObjectItem(cabInfo, "loc_now");

        if (loc_now != NULL) {
          value = loc_now->valuestring;
        }

      }
    }

  if (value) {
    return value;
  } 
  else {
    return NULL;
  }
}

//fonction json parse avec récupération de loc_prior
char* parseJsonTargetPos(char *jsonString) 
{
  char* value;

  aJsonObject* trame = aJson.parse(jsonString);
  
    if (trame != NULL) {
      aJsonObject* cabInfo = aJson.getObjectItem(trame, "cabInfo"); 

      if (cabInfo != NULL) {
        aJsonObject* loc_prior = aJson.getObjectItem(cabInfo, "loc_prior");

        if (loc_prior != NULL) {
          value = loc_prior->valuestring;
        }

      }
    }

  if (value) {
    return value;
  } 
  else {
    return NULL;
  }
}




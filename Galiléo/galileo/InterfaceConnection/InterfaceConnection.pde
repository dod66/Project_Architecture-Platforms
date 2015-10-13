import http.requests.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import processing.serial.*;
import java.lang.*;


JSONObject json;

Serial myPort; 

//////////////////////////////////////////////// ///Initialisation /////////////////////////////////////////////////////////////
void setup(){                                 
 //création de l'objet interface connection
 log = new interfaceConnexion();
 //rendre visible l'objet interface connection  
 log.setVisible(true);                                                            
 //Detecte si un galielo est branché au démarrage
 if(galileoConnected()== true){            
    //si galielo branché, initialisation port série   
    myPort = new Serial(this, log.port, 9600);                                      
    myPort.clear(); 
    println("Connexion sur le port: "+log.port);
   
 }   
}

/////////////////////////////////////////////////// Boucle infini /////////////////////////////////////////////////////////////

void draw(){
                                                                                
}                                                                               


//////////////////////////////////////////////// INTERFACE CONNEXION ///////////////////////////////////////////////////////
/*  Class qui permet d'initialiser tous les objets, et de créer une interface de connexion pour créer des partie ou les rejoindres   */

public class interfaceConnexion extends JFrame {
  public String seqPlayer;                                                       //n° du joueur (ex 1 pour J1)
  public String authPlayer;                                                      //identifiant random du joueur (auth)
  public boolean mode = false;                                                   // true = mode déplacement && false = mode marquage  
  public String port;                                                            //port série de connexion
  public int nb_Bombe;

  JTextField portChoice = new JTextField();                                      
    
  private JPanel contentPane;
  private JTextArea commandArea;

/*-------------------------------------------------- Partie graphique -----------------------------------------------------*/

  public interfaceConnexion(){  
    
    setTitle("Connexion");                                                          //initialisation du titre
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 276, 200);
    contentPane = new JPanel();
    setContentPane(contentPane);
    contentPane.setLayout(null);
    
    commandArea = new JTextArea();                                                  //zone de texte de l'interface
    commandArea.setEditable(false);
    commandArea.setBounds(6, 73, 263, 101);
    contentPane.add(commandArea);
    commandArea.setColumns(10);
    
    portChoice.setText("/dev/cu.usbmodem1421");                                     //valeur de base du port série
    portChoice.setBounds(44, 12, 225, 28);
    contentPane.add(portChoice);
    portChoice.setColumns(10);
    
    JLabel lblPort = new JLabel("Port :");                                    
    lblPort.setBounds(6, 18, 61, 16);
    contentPane.add(lblPort);
      
      repaint();
  }
}

interfaceConnexion log = null;                                                     //initialisation de l'objet


/*----------------------------------------------------- Fonctions -----------------------------------------------------*/


/********************************************* Détection connexion Galileo *********************************************/

boolean galileoConnected(){
  //récupération port
  log.port = log.portChoice.getText();                                            
  
   for(String name: Serial.list()){ 
      //recherche si ce port est connecté     
      if(name.equals(log.port))
          return true;
   } 
     return false;
}

/****************************************** Dialogue : Galileo --> Processing ******************************************/

void serialEvent (Serial mess) {
 //recupere le contenu du message du Galileo 
 String recepSerial = mess.readString();  
 //affichage du message reçu par le Galileo 
 println(recepSerial); 
//     if(!(recepSerial.contains("S"))){                        
//       println(recepSerial);                                                                     
//     }
}

/******************************************* POST : rejoindre une partie *****************************************/

void joinRequestsPost(String pseudo){
 
json = new JSONObject();                                                          //création de l'objet JSON
 
json.setString("name", pseudo);                                                   //arguments du JSON 

    println(json);
    String line = null;
    URL url = null;
    HttpURLConnection urlConnection = null;        
    try{                                                                          //vérifie l'envoi de la requete 
      url = new URL("http://perso.imerir.com/bbenoit/game/join.py");              //URL d'envoi de la requete
      urlConnection = (HttpURLConnection) url.openConnection();
    }
    catch(Exception e){}                              

      char c = 'P'; //(P : POST)
  
      String reponse = "";
      BufferedReader reader = null;
      
      String post = json.toString();                                              //contenu de la requete
      
      urlConnection.setDoOutput(true);
      try{
        OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
        writer.write(post);
        writer.flush();
        

        reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

        while ((line = reader.readLine()) != null) {
          reponse+= line.trim()+"\n";
        }
      }
      catch(IOException e){System.out.println("erreur d'IO");                   //renvoi une erreur d'exception en cas d'erreur
          e.printStackTrace();
       } 
      System.out.print(reponse);
      
      String numPlayer;
      String numAuthPlayer;
      json = json.parse(reponse);                                               //transforme la réponse de la requete : texte --> JSON
      
      if(reponse.contains("joined")){                                           //si partie rejointe
        
        log.seqPlayer = (str(json.getInt("seq")));                              //récupération du n° du joueur de la réponse JSON
        log.authPlayer = (json.getString("auth"));                              //récupération de l'identifiant de la réponse JSON
        numPlayer = log.seqPlayer;                                              //variable global reçoit le n° du joueur
        numAuthPlayer = log.authPlayer;                                         //et l'identifiant correspondant
        
        log.commandArea.setText("J"+numPlayer+" rejoint la partie..\n");        //écriture dans la zone de texte de l'interface de connexion 
        
      }
      if(reponse.contains("full")){                                             //si partie pleine
        log.commandArea.setText("");
        log.commandArea.setText("Partie complete..\n");
      }
      if(reponse.contains("none")){                                             //si aucune partie existante
        
        log.commandArea.setText("");
        log.commandArea.setText("Aucune partie..\n");
        
      }
      
}

///**************************************** POST : créer partie avec map enregistrée ****************************************/
//
//void createGameChoiceRequestsPost(int numGrille, int nbPlayers){
// 
//json = new JSONObject();
//
//  json.setInt("gridChoice", numGrille);                                        //arguments du JSON
//  json.setInt("numberOfPlayers", nbPlayers);
// 
//    println(json);
//    String line = null;
//    URL url = null;
//    HttpURLConnection urlConnection = null;
//    try{
//      url = new URL("http://perso.imerir.com/bbenoit/game/create.py");
//      urlConnection = (HttpURLConnection) url.openConnection();                //connexion
//    }
//    catch(Exception e){}
//
//    char c = 'P';                                                                 //(P : POST)
//  
//      String reponse = "";
//      BufferedReader reader = null;
//      
//      String post = json.toString();                                             //contenu du POST
//      
//      urlConnection.setDoOutput(true);
//      try{
//        OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
//        writer.write(post);
//        writer.flush();
//
//        reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
//
//        while ((line = reader.readLine()) != null) {
//          reponse+= line.trim()+"\n";
//        }
//      }
//      catch(IOException e){System.out.println("erreur d'IO"); e.printStackTrace();}
//      System.out.print(reponse);
//      
//      if(reponse.contains("started new game")){                                  //si partie créer
//              
//              log.commandArea.setText("");
//              log.commandArea.setText("Partie créer...\n");
//              
////          if(arduinoConnected()){                                                //si arduino connecté
////                 wnd.setVisible(false);                                          //desactive simulateur
////          } else { 
////                 wnd.setVisible(true);                                           //sinon active simulateur de mannette
////          }        
//      }
//}
//
///******************************************** POST : créer partie aléatoire ********************************************/
//
//void createGameAleaRequestsPost(int nbMines, int nbTreasures, int largeur , int hauteur, int nbPlayers){
// 
//json = new JSONObject();
//
//String resultat = "{\"mines\":"+nbMines+",\"treasures\":"+nbTreasures+",\"size\":{\"width\":"+largeur+",\"height\":"+hauteur+"},\"numberOfPlayers\":"+nbPlayers+"}";
//                                                                                //formation du JSON sans l'objet JSON
//    String line = null;
//    URL url = null;
//    HttpURLConnection urlConnection = null;
//    try{
//      url = new URL("http://perso.imerir.com/bbenoit/game/create.py");
//      urlConnection = (HttpURLConnection) url.openConnection();                //connexion
//    }
//    catch(Exception e){}
//
//    char c = 'P';                                                               //choix de la requete : (P : POST)
//  
//      String reponse = "";
//      BufferedReader reader = null;
//      
//      println(resultat);
//      
//      urlConnection.setDoOutput(true);
//      try{
//        OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
//        writer.write(resultat);
//        writer.flush();
//
//        reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
//
//        while ((line = reader.readLine()) != null) {
//          reponse+= line.trim()+"\n";
//        }
//      }
//      catch(IOException e){System.out.println("erreur d'IO"); e.printStackTrace();}
//      System.out.print(reponse);
//      
//      if(reponse.contains("starting new game")){                                  //si partie créer
//              
//              log.commandArea.setText("");
//              log.commandArea.setText("Partie créer...\n");
//              
////          if(arduinoConnected()){                                                //si arduino connecté
////                 wnd.setVisible(false);                                          //desactive simulateur
////          } else { 
////                 wnd.setVisible(true);                                           //sinon active simulateur de mannette
////          }        
//      }
//}
//
///*************************************** GET : requete initiale de création de partie *************************************/
//
//void createGameRequestsGet(String theUrl){
// 
//json = new JSONObject();
//
//    String line = null;
//    URL url = null;
//    HttpURLConnection urlConnection = null;
//    try{
//      url = new URL(theUrl);
//      urlConnection = (HttpURLConnection) url.openConnection();
//    }
//    catch(Exception e){}                                                          
//
//    char c = 'G';
//
//    if(c=='G'){                                                                   // On teste un GET
//       String reponse="";
//       
//      try {
//        InputStreamReader in = new InputStreamReader(urlConnection.getInputStream());
//        BufferedReader br = new BufferedReader(in);
//
//        while ((line = br.readLine()) != null) {
//          reponse = reponse.concat(line);
//        }
//
//      } 
//      catch(IOException e){
//        System.out.println("erreur d'IO");                                        //renvoi une erreur d'exception en cas d'erreur
//      }
//      finally {
//        urlConnection.disconnect();
//      }
//      println(reponse);
//      
//      if(reponse.contains("on going")){                                           //si partie en cours
//              log.commandArea.setText("");
//              log.commandArea.setText("Partie en cours...\n");
//              
//       }else frame.setVisible(true);
//      
//    } 
//}
//
///************************************ GET : envoi requete de marquage ou de déplacement **************************************/
//
//void playerMarkGameRequestsGet(String auth, String direction){
//                                                     // choice : (mark || move) suivant le marquage ou le déplacement du personnage
//json = new JSONObject();
//
//String choice = markOrMoveMode(log.mode);                                        //récupère le mode de jeu actuel
//
//    String theUrl =  "http://perso.imerir.com/bbenoit/game/player.py?auth="+auth+"&"+choice+"="+direction;
//    String line = null;
//    //println(theUrl);
//    URL url = null;
//    HttpURLConnection urlConnection = null;
//    try{
//      url = new URL(theUrl);
//      urlConnection = (HttpURLConnection) url.openConnection();
//    }
//    catch(Exception e){}
//
//    char c = 'G';
//
//    if(c=='G'){
//      
//      try {
//        InputStreamReader in = new InputStreamReader(urlConnection.getInputStream());
//        BufferedReader br = new BufferedReader(in);
//
//        
//        while ((line = br.readLine()) != null) {
//          System.out.println(line);
//        }
//
//      } 
//      catch(IOException e){System.out.println("erreur d'IO");}
//      finally {
//        urlConnection.disconnect();
//      }
//    }
//    
//}
//
///*************************************** GET : récupère toutes les infos de la partie  **************************************/
//
//void statusRequestGet(){        
// 
//json = new JSONObject();
//
//    
//    String theUrl = "http://perso.imerir.com/bbenoit/game/status.py";
//    String line = null;
//    URL url = null;
//    HttpURLConnection urlConnection = null;
//    try{
//      url = new URL(theUrl);
//      urlConnection = (HttpURLConnection) url.openConnection();
//    }
//    catch(Exception e){}
//
//   
//      
//      String reponse="";                                    
//      int nbJoueur=0;
//      try {
//        InputStreamReader in = new InputStreamReader(urlConnection.getInputStream());
//        BufferedReader br = new BufferedReader(in);
//
//        while ((line = br.readLine()) != null) {
//          reponse = reponse.concat(line);
//        }
//
//      } 
//      catch(IOException e){
//        System.out.println("erreur d'IO");
//        e.printStackTrace();
//      }
//      finally {
//        urlConnection.disconnect();
//      }
//      println(reponse);
//            
//      String needle = "name";
//      int index = -needle.length();
//      String rep="";
//       
//      while ((index = reponse.indexOf(needle, index + needle.length())) != -1){   //calcul du nb de joueurs connectés grace au JSON
//        nbJoueur++;
//      }
//      
//      json = json.parse(reponse);
//      
//      JSONArray values = json.getJSONArray("players");
//      int size = values.size(); 
//           
//         int i = 1;  
//         
//         if(arduinoConnected()){
//           myPort.clear();
//       }else wnd.texteArea.setText("");
//         
//   for (int j = size - nbJoueur; j < size; j++) { 
//          
//         JSONObject playerList = values.getJSONObject(j); 
//                         
//          int nbBomb = playerList.getInt("mineInVincinity");            //récupération de l'indicateur du joueur entrain de jouer
//          int playerTurn = playerList.getInt("isPlaying");      //récupération du nb de mine autour des joueurs dans le JSON
//
//         log.nb_Bombe = nbBomb; 
//         
//         if(arduinoConnected()){       
//                 myPort.write("J"+i+"="+log.nb_Bombe);
//                 i++;
//          } else {               
//                 wnd.texteArea.append("       J"+i+"="+log.nb_Bombe);  
//                 println("       J"+i+"="+log.nb_Bombe); 
//                 i++;   
//          }     
//    }   
//}
//
//
///************************************************ fonction d'attente, timer  ********************************************/
//
//void sleep(int refreshTime){
//   try{
//      Thread.sleep(refreshTime);
//   } catch(InterruptedException e){
//    e.printStackTrace(); 
//   }
//}
//
///*************************************** POST : suppression de la partie en cours **************************************/
//
//void suppRequestsPost(String supp){
// 
//json = new JSONObject();
// 
//json.setString("supp", supp);                                      
//
//    println(json);
//    String line = null;
//    URL url = null;
//    HttpURLConnection urlConnection = null;
//    try{
//      url = new URL("http://perso.imerir.com/bbenoit/game/create.py");
//      urlConnection = (HttpURLConnection) url.openConnection();
//    }
//    catch(Exception e){}
//
//      char c = 'P';
//  
//      String reponse = "";
//      BufferedReader reader = null;
//      
//      String post = json.toString(); 
//      
//      urlConnection.setDoOutput(true);
//      try{
//        OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
//        writer.write(post);
//        writer.flush();
//        
//
//        reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
//
//        while ((line = reader.readLine()) != null) {
//          reponse+= line.trim()+"\n";
//        }
//      }
//      catch(IOException e){System.out.println("erreur d'IO"); e.printStackTrace();}
//      System.out.print(reponse);      
//         
//         if(reponse.contains("delete")){                                        //si suppression réussite
//              log.commandArea.setText("");
//              log.commandArea.setText("Partie supprimée..\n");
//         }    
//}



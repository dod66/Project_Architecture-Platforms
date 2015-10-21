//Thank's to Olivier Leger to help me in this part of the project
//Jetty Library used in this part of the project : http://www.eclipse.org/jetty/

package ConnectionClient;

import java.io.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.simple.parser.ParseException;

import frameTreatment.Treatment;

@WebSocket(maxTextMessageSize = 64 * 1024)

public class ClientSocket {
	
		private String message; //Var message initialization
		private Treatment traitement = new Treatment(); //Create new object Treatement for JSON trame
		
		//Initialize a session
		private Session session; //Create a new session
		CountDownLatch latch = new CountDownLatch(1); //Synchronization

		//Init message received
		public void messageReception(String message) throws IOException, ParseException{ 
			System.out.println("Message received from server : \n" + message); //Print in console
			this.setMessage(message); //set the message received
			traitement.testMapper(message); //message treatment
		}
		

		public void serverConnection(Session session){ //socket connection
				System.out.println("Connected to server: ");//Print on console
				this.session = session; //Establish session
				sendMessage("test"); //send message to server which permit to send us the frame from it
		}

		public void sendMessage(String str){ //function to send message to the server
			try {
				session.getRemote().sendString(str);
			} catch (IOException e){
				e.printStackTrace(); //Exception
			}
		}

		public boolean awaitClose(int i, TimeUnit hours) throws InterruptedException{ //This function permit to stay awake during connections
			return this.latch.await(i, hours);
		}

		public String getMessage() { //function to get the message
			return message;
		}

		public void setMessage(String message) { //function to set the message
			this.message = message;
		}
}
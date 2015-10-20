package ClientWebSocket;

import java.io.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.simple.parser.ParseException;

import frameTreatment.TraitementJSon;

@WebSocket(maxTextMessageSize = 64 * 1024)

public class ClientSocket {
	
		private String message;
		private TraitementJSon traitement = new TraitementJSon();
		
		//Init a session
		private Session session; //Optional
		CountDownLatch latch = new CountDownLatch(1);
		
		@OnWebSocketMessage
		//Init message received
		public void onMessage(String message) throws IOException, ParseException{ 
			System.out.println("Message received from server : \n" + message); //Print in console
			this.message=message;
			traitement.testMapper(message);
		}
		
		/* Socket connection */
		@OnWebSocketConnect
		public void onConnect(Session session){
				System.out.println("Connected to server: ");//Print on console
				this.session = session;
				//send message to server which permit to send us the frame from it
				sendMessage("test");
		}

		public void sendMessage(String str){ //function to send message to the server
			try {
				session.getRemote().sendString(str);
			} catch (IOException e){
				e.printStackTrace(); //Exception
				System.out.println("test");//Print on console
			}
		}
		//Fix a time to stay awake
		public boolean awaitClose(int i, TimeUnit hours) throws InterruptedException{
			return this.latch.await(i, hours);
			
		}
}
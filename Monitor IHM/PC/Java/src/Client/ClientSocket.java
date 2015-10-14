package Client;

import java.io.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket(maxTextMessageSize = 64 * 1024)

public class ClientSocket {
		
		//Init a session
		private Session session; //Optionnal
		CountDownLatch latch = new CountDownLatch(1);
		
		@OnWebSocketMessage
		//Init message received
		public void onMessage(String message) throws IOException{ 
			System.out.println("Message received from server : \n" + message); //Print in console
		}
		
		/* Socket connection */
		@OnWebSocketConnect
		public void onConnect(Session session){
				System.out.println("Connected to server: ");//Print on console
				this.session = session;
				//send message to server which permit to send the trame from it
				sendMessage("test");
		}

		public void sendMessage(String str){ //function to send message to the server
			try {
				session.getRemote().sendString(str);
			} catch (IOException e){
				e.printStackTrace(); //Exception
			}
		}
		//Fix a time to stay awake
		public boolean awaitClose(int i, TimeUnit hours) throws InterruptedException{
			return this.latch.await(i, hours);
			
		}
}
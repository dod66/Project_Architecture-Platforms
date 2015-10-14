package Client;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.client.WebSocketClient;

public class ConnectionWebSocket extends Thread{
	private String dest = "ws://172.30.0.190:6000"; //server adress Oli : ws://172.30.0.184:2009
	public void run(){ //main thread
		WebSocketClient client = new WebSocketClient(); //WebSocket initialisation
		try {
			ClientSocket socket = new ClientSocket(); //create client socket
			
			client.start(); //Start socket
			URI echoURI = new URI(dest);	
			
			client.connect(socket, echoURI);
			socket.awaitClose(1, TimeUnit.HOURS);
			
		}catch (Throwable t){
			t.printStackTrace();//debugging for Exception
		}finally {
			try {
				client.stop();
			} catch (Exception e) { //Exception
				e.printStackTrace(); 
			}
		}
	}
}

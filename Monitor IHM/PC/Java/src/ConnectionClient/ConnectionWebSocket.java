package ConnectionClient;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.client.WebSocketClient;

public class ConnectionWebSocket extends Thread{
	
	private String dest = null; //Initialize dest var
	
	public ConnectionWebSocket(String address) { //Constructor
		dest = address;
	}
	
	public void run(){ //main thread not mandatory but
		WebSocketClient client = new WebSocketClient(); //WebSocket initialization with jetty
		try {
			ClientSocket socket = new ClientSocket(); //create client socket with jetty
			
			client.start(); //Start socket with jetty
			URI echoURI = new URI(dest); //Cast as URI format
			
			client.connect(socket, echoURI); //Client connection
			socket.awaitClose(1, TimeUnit.HOURS); //Stay awake : Call from ClientSocket
			
		}catch (Throwable t){
			t.printStackTrace();//debugging for Exception
		}finally { //!! Executed instead of Exception
			try {
				client.stop(); //Stop Client execution
			} catch (Exception e) { //Exception
				e.printStackTrace(); 
			}
		}
	}
}

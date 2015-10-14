package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientHttp extends Thread {
	private String ip_address = "172.30.0.190";
	private static final int port = 5000;
	private BufferedReader buff = null;
	private Socket sock = null;
	
	public ClientHttp(String ip_address){
		this.ip_address = ip_address;
		try{
			this.sock=new Socket(ip_address, port);
			this.buff = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
			System.out.println("Connected to : "+this.sock.getInetAddress() + " on port :"+this.sock.getPort());
		}catch(IOException e){
			System.out.println("Echec de la connexion");
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ConnectionWebSocket connect = new ConnectionWebSocket();
		connect.run(); //Run server connection
	}
}

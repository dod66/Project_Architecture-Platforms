package ClientHttp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import Client.ConnectionWebSocket;

public class ClientHttp extends Thread {
	//Connection data 
	private String ip_address = "172.30.0.190"; //address
	private static final int port = 5000; //port
	private BufferedReader buff = null; //buffer
	private Socket sock = null; //socket
	
	public void run(){
			this.ip_address = ip_address;
			try{
				this.sock=new Socket(ip_address, port); //Socket creation
				this.buff = new BufferedReader(new InputStreamReader(this.sock.getInputStream())); //set buffer
				System.out.println("Connected to : "+this.sock.getInetAddress() + " on port :"+this.sock.getPort()); //check if we are connected and return data connection
			}catch(IOException e){ 
				System.out.println("Connexion Failed"); //Exception if connection aborted
			}
			String message = null; //init message
			try{
				while(true){ //listening loop
					message = this.buff.readLine(); //push message received in buffer
					if(message == null){ //when message is null connection is stopped
						System.out.println("Connexion finished");
						buff.close(); //close buffer
						sock.close(); //close socket
					}
					System.out.println(message);//print message in console
					buff.close();
					break; //exit loop
				}
			}catch(IOException e){
				System.out.println("Error : "+e+"\n");//Exception when an error appear in receiving message process
			}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClientHttp connect = new ClientHttp();
		connect.run(); //Run server connection
	}
}

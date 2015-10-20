package ClientHttp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;

public class ClientHttp extends Thread {
	//Connection data 
	private String ip_address = "172.30.0.190"; //address
	private static final int port = 5000; //port
	private BufferedReader buff = null; //buffer
	private Socket sock = null; //socket
	private final String USER_AGENT = "Mozilla/5.0";
	
	private void sendGet() throws Exception {

		String url = "http://172.30.0.190:5000";
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());

	}
	
	public void run(){
		
			/*TRANSITION*/
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
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		ClientHttp connect = new ClientHttp();
		connect.sendGet();
		connect.run(); //Run server connection		
	}
}

package ConnectionClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ClientHttp {
	
	public static String url = "http://172.30.0.190:4000"; //Set URL address
	public static String received_message; // Set message
	public static String ip_address_http; //Set Var to send to WSServer
	
	public String getURL(String url) throws Exception{

			URL obj = new URL(url); //Set URL Object
			HttpURLConnection con = (HttpURLConnection) obj.openConnection(); //HTTP Connection
			String inputLine; //set var for input
			StringBuffer response = new StringBuffer(); //Set response as StringBuffer Object
			String received_frame = null; //Initialize var to get server response
			
			con.setRequestMethod("GET"); //Get Request to send to the server

			System.out.println("\nSending 'GET' request to URL : " + url);//Debug print
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream())); //create new buffer
	
			while ((inputLine = in.readLine()) != null) { //while there is something on the buffer
				response.append(inputLine);
			}
			in.close(); //Closing buffer
	
			received_frame = response.toString(); //convert to string the JSON frame received from server
			System.out.println(received_frame); //Debug print
			
			//this.received_message = received_frame;
			
			return received_frame; //return string
	}
	
	public String JSONParser(String received_frame) throws ParseException{ //Parsing JSON response received
		JSONParser parser = new JSONParser(); //Create JSON Parser
		JSONObject jsonobj = null; //Initialize JSON Object
		
		//String received_message = null;
		
		try{ 
			jsonobj = (JSONObject)parser.parse(received_frame);//Cast parser into JSONObject
			
			String ip = jsonobj.get("IP").toString(); //Extraction of the IP part
			String port = jsonobj.get("PORT").toString(); //Extraction of the PORT part
			System.out.println(ip+":"+port); //Debug print

			ip_address_http = "ws://"+ip+":"+port; //Creation of the address
			System.out.println(ip_address_http); //Debug print
		}catch(Exception e){
			
		}
		return ip_address_http; //return string
	}
}
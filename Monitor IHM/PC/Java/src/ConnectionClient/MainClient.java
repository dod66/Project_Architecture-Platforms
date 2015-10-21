package ConnectionClient;

public class MainClient {

	public static void main(String[] args) throws Exception {
		ClientHttp connectHttp = new ClientHttp(); //Create a new HTTP client
		connectHttp.getURL(ClientHttp.url); //Execute and get address from server
		String address = connectHttp.JSONParser(ClientHttp.received_message);
		System.out.println(address);//Debug print
		ConnectionWebSocket connect = new ConnectionWebSocket(address);//Connect client WS with address get from HTTP server
		connect.run(); //Run server connection
	}
}
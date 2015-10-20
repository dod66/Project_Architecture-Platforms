package frameTreatment;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import ClientWebSocket.ClientSocket;

public class TraitementJSon {
	
	ClientSocket client = new ClientSocket();
	
	public void testMapper() throws JsonParseException, JsonMappingException, IOException, ParseException{
			JSONParser parser = new JSONParser();
			JSONObject jsonObj = null;
			ObjectMapper mapper = new ObjectMapper();
			
			jsonObj = (JSONObject)parser.parse(client.getMessage());
			Area area = mapper.readValue(jsonObj.toString(), Area.class);
			
			System.out.println(area.toString());
	}
	
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException, ParseException{
		TraitementJSon traitement = new TraitementJSon();
		traitement.testMapper();
	}
}
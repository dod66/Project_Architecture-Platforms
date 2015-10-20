package frameTreatment;


import java.io.IOException;
import java.util.ArrayList;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TraitementJSon {

	public void testMapper(String message){
			JSONParser parser = new JSONParser();
			JSONObject jsonObj = null;
			ObjectMapper mapper = new ObjectMapper();
			try{
				jsonObj = (JSONObject)parser.parse(message);
				for(JSONObject object: (ArrayList<JSONObject>) jsonObj.get("areas")){
					Area area = mapper.readValue(object.toString(), Area.class);
					System.out.println(area);
				}
			}catch(IOException e){
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
//Jackson Library is used in this project : https://github.com/FasterXML/jackson

package frameTreatment;


import java.io.IOException;
import java.util.ArrayList;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Treatment {

	public void testMapper(String message){
			JSONParser parser = new JSONParser(); //Parser Initialization
			JSONObject jsonObj = null; //JSONObject initialization
			ObjectMapper mapper = new ObjectMapper(); //Mapper initialization
			try{
				jsonObj = (JSONObject)parser.parse(message);
				for(JSONObject object: (ArrayList<JSONObject>) jsonObj.get("areas")){
					Area area = mapper.readValue(object.toString(), Area.class); //get JSON Object
					System.out.println(area); //Debug print
				}
			}catch(IOException e){
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
	}
}
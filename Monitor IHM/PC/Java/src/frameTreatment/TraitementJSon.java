package frameTreatment;

import java.io.File;
import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class TraitementJSon {
	
		public void testMapper(File file) throws JsonParseException, JsonMappingException, IOException{
			JSONParser parser = new JSONParser();
			JSONObject jsonObj = null;
			ObjectMapper mapper = new ObjectMapper();
			
			Street street = mapper.readValue(file, Street.class);
			
			System.out.println(street.toString());
	
		}
	   public static void main(String args[]) throws JsonParseException, JsonMappingException, IOException{
		   
		   TraitementJSon t = new TraitementJSon();
		   t.testMapper(new File("/Users/Remi/Desktop/street.json"));

//	      ObjectMapper mapper = new ObjectMapper();
//
//	      String jsonString = "{\"odometer\":\"16166\",\"destination\":\"None\",\"loc_now\":\"None\",\"loc_prior\":\"oki\"}}}";
//	      
//	      //map json to frame
//			
//	      try{
//	         Frame frame = mapper.readValue(jsonString, Frame.class);
//	         
//	         System.out.println(frame);
//	         
//	         mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
//	         jsonString = mapper.writeValueAsString(frame);
//	         
//	         System.out.println(jsonString);
//	      }
//	      catch (JsonParseException e) { e.printStackTrace();}
//	      catch (JsonMappingException e) { e.printStackTrace(); }
//	      catch (IOException e) { e.printStackTrace(); }
//	   }
	}
}
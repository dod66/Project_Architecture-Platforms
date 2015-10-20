package TraitementJSon;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONObject;

public class MapArea {
	private String name;
	private float width;
	private float height;
	private HashMap<String, vertices> vertices;
	private HashMap<String, streets> streets;
	private ArrayList<bridges> bridges;
	
	public void mapArea(JSONObject AreaJSon){
		this.name = (String)AreaJSon.get("name");
		this.width = Float.parseFloat(((JSONObject) ((JSONObject)AreaJSon.get("map")).get("weight")).get("w").toString());
		this.height = Float.parseFloat(((JSONObject) ((JSONObject)AreaJSon.get("map")).get("height")).get("h").toString());
		generateVertices((ArrayList<JSONObject>) ((JSONObject)AreaJSon.get("map")).get("vertices"));
		generateStreets((ArrayList<JSONObject>) ((JSONObject)AreaJSon.get("map")).get("streets"));
		generateBridges((ArrayList<JSONObject>) ((JSONObject)AreaJSon.get("map")).get("bridges"));
	}
	
	private void generateVertices(ArrayList<JSONObject> verticesList){
		this.vertices = new HashMap<>();
		for(JSONObject vertice : verticesList){
			float x = Float.parseFloat(vertice.get("x").toString());
			float y = Float.parseFloat(vertice.get("y").toString());
			this.vertices.put((String)vertice.get("name"),new vertices((String)vertice.get("name"),x,y));
		}
	}
	
	private void generateStreets(ArrayList<JSONObject> streetsList){
		this.streets = new HashMap<>();
		for(JSONObject street : streetsList){
			ArrayList<vertices> path = new ArrayList<>();
			for (String p: ((ArrayList<String>)street.get("path")))
				path.add(getVerticesByName(p));
			this.streets.put((String)street.get("name"),new streets((String)street.get("name"),path,(boolean)street.get("oneway")));
		}
	}

		private void generateBridges(ArrayList<JSONObject> bridgesList){
	
	}
		
		public vertices getVerticesByName(String name){
			return this.vertices.get(name);
		}
}

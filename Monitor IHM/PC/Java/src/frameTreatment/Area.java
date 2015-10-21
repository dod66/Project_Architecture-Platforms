package frameTreatment;

public class Area {
	private String name;
	private Map map;
	//private ArrayList<Vertex> vertex;
	
	public Area(){ //Default constructor
		
	}
	
	public Area(String name, Map map){ //Constructor
		this.name = name;
		this.map = map;
	}

	@Override
	public String toString() { //Return structure
		return "Area [name=" + name + ", map=" + map + "]";
	}
	
	//Below : getters and setters

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}
	

}

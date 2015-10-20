package frameTreatment;

public class Area {
	private String name;
	private Map map;
	//private ArrayList<Vertex> vertex;
	
	public Area(){
		
	}
	
	public Area(String name, Map map){
		this.name = name;
		this.map = map;
	}

	@Override
	public String toString() {
		return "Area [name=" + name + ", map=" + map + "]";
	}

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

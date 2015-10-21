package frameTreatment;

public class BridgeTo {
	private String area;
	private String vertex;
	private int weight;
	
	public BridgeTo(){ //Default constructor
		
	}
	
	public BridgeTo(String area, String vertex, int weight){ //Constructor
		this.area = area;
		this.vertex = vertex;
		this.weight = weight;
	}
	
	@Override
	public String toString() { //return structure
		return "BridgeTo [area=" + area + ", vertex=" + vertex + ", weight=" + weight + "]";
	}

	//Below : getters and setters
	
	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getVertex() {
		return vertex;
	}

	public void setVertex(String vertex) {
		this.vertex = vertex;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

}

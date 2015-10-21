package frameTreatment;

import java.util.ArrayList;

public class Map {
	private Weight weight;
	private ArrayList<Vertex> vertices;
	private ArrayList<Streets> streets;
	private ArrayList<Bridges> bridges;
	
	public Map(){ //Default Constructor
		
	}
	
	public Map(ArrayList<Vertex> vertices, ArrayList<Streets> streets, ArrayList<Bridges> bridges){ //constructor
		this.vertices = vertices;
		this.streets = streets;
		this.bridges = bridges;
	}

	@Override
	public String toString() { //return structure
		return "Map [weight=" + weight + ", vertices=" + vertices + ", streets=" + streets + ", bridges=" + bridges
				+ "]";
	}
	
	//Below : getters and setters

	public Weight getWeight() {
		return weight;
	}

	public void setWeight(Weight weight) {
		this.weight = weight;
	}

	public ArrayList<Vertex> getVertices() {
		return vertices;
	}

	public void setVertices(ArrayList<Vertex> vertices) {
		this.vertices = vertices;
	}

	public ArrayList<Streets> getStreets() {
		return streets;
	}

	public void setStreets(ArrayList<Streets> streets) {
		this.streets = streets;
	}

	public ArrayList<Bridges> getBridges() {
		return bridges;
	}

	public void setBridges(ArrayList<Bridges> bridges) {
		this.bridges = bridges;
	}
}

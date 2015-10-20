package frameTreatment;

import java.util.ArrayList;

public class Streets {
	private String name;
	private ArrayList<String> path;
	private boolean oneway;
	
	public Streets(){
		
	}
	
	public Streets(String name, ArrayList<String> path, boolean oneway){
		this.name = name;
		this.path = path;
		this.oneway = oneway;
	}

	@Override
	public String toString() {
		return "Street [name=" + name + ", path=" + path + ", oneway=" + oneway + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public ArrayList<String> getPath() {
		return path;
	}

	public void setPath(ArrayList<String> path) {
		this.path = path;
	}

	public boolean isOneway() {
		return oneway;
	}

	public void setOneway(boolean oneway) {
		this.oneway = oneway;
	}
}

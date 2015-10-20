package TraitementJSon;

import java.util.ArrayList;

public class streets {
	String name = "";
	ArrayList<vertices> path;
	Boolean oneway;
	public streets(String name, ArrayList<vertices> path, Boolean oneway){
		this.name = name;
		this.path = path;
		this.oneway = oneway;
	}
}

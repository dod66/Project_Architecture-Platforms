package frameTreatment;

public class Vertex {
	
	private String name;
	private float x;
	private float y;
	
	public Vertex(){ //Default constructor
		
	}
	
	public Vertex(String name, float x, float y){ //Constructor
		this.setName(name);
		this.setX(x);
		this.setY(y);
	}

	@Override
	public String toString() {
		return "Vertex [name=" + name + ", x=" + x + ", y=" + y + "]"; //return structure
	}
	
	//Below : getters and setters

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
}
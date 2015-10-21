package frameTreatment;

public class Bridges {
	private String from;
	private BridgeTo to;
	private int weight;
	
	public Bridges(){ //Default Constructor
		
	}
	
	public Bridges(String from, BridgeTo to, int weight){ //Constructor
		this.from = from;
		this.to = to;
		this.weight = weight;
	}

	@Override
	public String toString() { //Return Structure
		return "Bridge [from=" + from + ", to=" + to + ", weight=" + weight + "]";
	}

	//Below : getters and setters
	
	public String getFrom() { 
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public BridgeTo getTo() {
		return to;
	}

	public void setTo(BridgeTo to) {
		this.to = to;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}
}

package frameTreatment;

public class Bridges {
	private String from;
	private BridgeTo to;
	private int weight;
	
	public Bridges(){
		
	}
	
	public Bridges(String from, BridgeTo to, int weight){
		this.from = from;
		this.to = to;
		this.weight = weight;
	}

	@Override
	public String toString() {
		return "Bridge [from=" + from + ", to=" + to + ", weight=" + weight + "]";
	}

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

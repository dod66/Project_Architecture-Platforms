package frameTreatment;

public class Weight {
	private String w;
	private String h;
	
	public Weight(){ //Default constructor
		
	}
	
	public Weight(String w, String h){ //Constructor
		this.w = w;
		this.h = h;
	}

	@Override
	public String toString() {
		return "Weight [w=" + w + ", h=" + h + "]"; //return structure
	}
	
	//Below : getters and setters

	public String getW() {
		return w;
	}

	public void setW(String w) {
		this.w = w;
	}

	public String getH() {
		return h;
	}

	public void setH(String h) {
		this.h = h;
	}
}

package frameTreatment;

public class Weight {
	private String w;
	private String h;
	
	public Weight(){
		
	}
	
	public Weight(String w, String h){
		this.w = w;
		this.h = h;
	}

	@Override
	public String toString() {
		return "Weight [w=" + w + ", h=" + h + "]";
	}

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

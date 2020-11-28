package engine;

public class CoordFive extends CoordFour{

	boolean color;
	
	public CoordFive(int x, int y, boolean color) {
		super(x, y);
		this.color = color;
	}
	
	public CoordFive(CoordFour c, boolean color) {
		super(c.x,c.y,c.T,c.L);
		this.color = color;
		
	}
	
	public CoordFive(int x, int y, int T, int L, boolean color) {
		super(x,y,T,L);
		this.color = color;
	}
	
	public CoordFive clone() {
		return new CoordFive(this.clone(),this.color);
	}
}

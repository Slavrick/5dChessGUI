package engine;

public class CoordFive extends CoordFour{

	public boolean color;
	
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
	
	public String toString() {
		char colorch;
		if(color) {
			colorch = 'w';
		}else {
			colorch = 'b';
		}
		return "(" + colorch + "." + L + "L." + "T" + T + "." + intTofile(x) + "" + (y + 1) + ")";
	}
}

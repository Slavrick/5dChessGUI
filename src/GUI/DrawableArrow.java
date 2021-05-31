package GUI;


import engine.CoordFive;
import engine.CoordFour;
import engine.Move;

public class DrawableArrow {
	int startX;
	int endX;
	int startY;
	int endY;
	int turnnum;
	CoordFive origin;
	CoordFive dest;
	
	public DrawableArrow(Move m, boolean color, int w, int h, int turnnum) {
		this.origin = new CoordFive(m.origin, color);
		this.dest = new CoordFive(m.dest, color);
		if(m.type == Move.SPATIALMOVE) {
			if(!origin.color){
				origin.T++;
				dest.T++;
			}
			origin.color = !origin.color;
			dest.color = !dest.color;
		}
		this.turnnum = turnnum;
		startX = ChessDrawer.coordToX(m.origin, color, w, h);
		endX = ChessDrawer.coordToX(m.dest, color, w, h);
		startY = ChessDrawer.coordToY(m.origin, w, h);
		endY = ChessDrawer.coordToY(m.dest, w, h);
		if(m.type == Move.SPATIALMOVE) {
			startX += ChessDrawer.padding + (ChessDrawer.squarewidth * w);
			endX += ChessDrawer.padding + (ChessDrawer.squarewidth * w);
		}
	}
	
	public DrawableArrow(Move m, boolean color, int w, int h) {
		this.origin = new CoordFive(m.origin, color);
		this.dest = new CoordFive(m.dest, color);
		if(m.type == Move.SPATIALMOVE) {
			if(!origin.color){
				origin.T++;
				dest.T++;
			}
			origin.color = !origin.color;
			dest.color = !dest.color;
		}
		this.turnnum = 0;
	}
	
	public DrawableArrow(CoordFive origin, CoordFive dest, int w, int h) {
		this.origin = origin;
		this.dest = dest;
	}
	
	public DrawableArrow(CoordFour origin, CoordFour dest, boolean color) {
		
	}
}

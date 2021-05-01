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
	
	public DrawableArrow(Move m, boolean color, int w, int h, int turnnum) {
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
	
	public DrawableArrow(CoordFive origin, CoordFive dest, int w, int h) {
		startX = ChessDrawer.coordToX(origin, w, h);
		endX = ChessDrawer.coordToX(dest, w, h);
		startY = ChessDrawer.coordToY(origin, w, h);
		endY = ChessDrawer.coordToY(dest, w, h);
	}
	
	public DrawableArrow(CoordFour origin, CoordFour dest, boolean color) {
		
	}
}

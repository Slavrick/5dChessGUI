package GUI;

import engine.CoordFive;
import engine.CoordFour;

public class DrawableArrow {
	int startX;
	int endX;
	int startY;
	int endY;
	
	public DrawableArrow(CoordFive origin, CoordFive dest, int w, int h) {
		startX = ChessDrawer.coordToX(origin, w, h);
		endX = ChessDrawer.coordToX(dest, w, h);
		startY = ChessDrawer.coordToY(origin, w, h);
		endY = ChessDrawer.coordToY(dest, w, h);
	}
	
	public DrawableArrow(CoordFour origin, CoordFour dest, boolean color) {
		
	}
}

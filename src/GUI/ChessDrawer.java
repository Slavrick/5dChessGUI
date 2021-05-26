package GUI;

import javafx.scene.paint.*;
import javafx.scene.shape.ArcType;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import engine.Board;
import engine.CoordFive;
import engine.CoordFour;
import engine.GameState;
import engine.Move;
import engine.Timeline;
import javafx.scene.canvas.GraphicsContext;

public class ChessDrawer {

	public static Image piecesprites;
	public static Color darkColor = Color.rgb(118,150,86);//.DARKGOLDENROD; 
	public static Color lightColor = Color.rgb(238,238,210);
	public static Color multiverseLight = Color.rgb(248,248,220);
	public static Color multiverseDark = Color.rgb(148,180,116);
	public static Color arrowColor = Color.rgb(147,112,219);

	private static final int SPRITEWIDTH = 128;
	private static final int SPRITESHEETWIDTH = 10;
	//Width of the squares on the chess board, plus the padding that is between the boards.
	public static int squarewidth = 32;
	public static int halfSquare = squarewidth / 2;
	public static int padding = 50;
	public static int halfPadding = padding / 2;
	//Dimensions of the chess board
	public static int width = 8;
	public static int height = 8;
	
	public static void setSquareWidth(int sqwdth) {
		squarewidth = sqwdth;
		 halfSquare = squarewidth / 2;
	}
	
	public static void changeSquareWidth(int change) {
		squarewidth += change;
		halfSquare = squarewidth / 2;
	}
	
	public static void drawMultiverseGrid(GraphicsContext gc, int screenx, int screeny, GameState game) {
		int squareWidth = (squarewidth * game.width) * 2 + (2 * padding);
		int squareHeight = squarewidth * game.height + padding;
		for(int L = (screeny / squareHeight) - 1; L <= (screeny / squareHeight) + 10; L++) {
				for(int T = (screenx / squareWidth) - 1; T <= (screenx / squareWidth) + 10; T++) {
				if((T + L) % 2 != 0) {
					gc.setFill(multiverseLight);
				}
				else{
					gc.setFill(multiverseDark);
				}
				gc.fillRect((T * squareWidth) - screenx + halfPadding,(L * squareHeight) - screeny + halfPadding, squareWidth, squareHeight);
				gc.setFill(Color.BLACK);
				gc.fillText( (L) + "L, " + (T+1) + "T", (T * squareWidth) - screenx + halfPadding, (L * squareHeight) - screeny + halfPadding + 10);
			}
		}
	}
	
	public static void drawMultiverseGridHalf(GraphicsContext gc, int screenx, int screeny, GameState game) {
		int squareWidth = squarewidth * game.width + padding;
		int squareHeight = squarewidth * game.height + padding;
		for(int L = (screeny / squareHeight) - 1; L <= (screeny / squareHeight) + 10; L++) {
				for(int T = (screenx / squareWidth) - 1; T <= (screenx / squareWidth) + 10; T++) {
				if((T + L) % 2 != 0) {
					gc.setFill(multiverseLight);
				}
				else{
					gc.setFill(multiverseDark);
				}
				gc.fillRect((T * squareWidth) - screenx + halfPadding,(L * squareHeight) - screeny + halfPadding, squareWidth, squareHeight);
				gc.setFill(Color.BLACK);
				gc.fillText( (L) + "L, " + (T+1) + "T", (T * squareWidth) - screenx + halfPadding, (L * squareHeight) - screeny + halfPadding + 10);
			}
		}
	}
	
	public static void drawMultiverseV(GraphicsContext gc, int screenx, int screeny, GameState game) {
		drawMultiverseGrid(gc,screenx,screeny,game);
		int layerCTR = 0;
		int boardwidth = squarewidth * game.width;
		int boardHeight = squarewidth * game.height;
		for (int i = game.minTL; i <= game.maxTL; i++) {
			Timeline t = game.getTimeline(i);
			int offsetNum = (t.Tstart - 1) * 2;
			if (!t.colorStart) {
				offsetNum++;
			}
			int xoffset = offsetNum * (boardwidth + padding);
			int yoffset = (i * (boardHeight + padding));
			if(yoffset - screeny < 0 - (boardHeight + padding + 20)) {
				//Excludes Items too high to see.
			}
			else if(game.layerIsActive(i)) {
				drawTimelineV(gc, padding + xoffset, padding + yoffset, screenx, screeny, game.width, t, true);				
			}else {
				drawTimelineV(gc, padding + xoffset, padding + yoffset, screenx, screeny, game.width, t, false);
			}
			layerCTR++;
		}
		//gc.fillRect( -screenx , -screeny, 10, 10);
	}
	
	public static void drawColoredMultiverse(GraphicsContext gc, int screenx, int screeny, GameState game, boolean color) {
		drawMultiverseGridHalf(gc,screenx,screeny,game);
		int layerCTR = 0;
		int boardwidth = squarewidth * game.width;
		int boardHeight = squarewidth * game.height;
		for (int i = game.minTL; i <= game.maxTL; i++) {
			Timeline t = game.getTimeline(i);
			int offsetNum = (t.Tstart - 1);
			int xoffset = offsetNum * (boardwidth + padding);
			int yoffset = (i * (boardHeight + padding));
			if(yoffset - screeny < 0 - (boardHeight + padding + 20)) {
				//Excludes Items too high to see.
			}
			else if(game.layerIsActive(i)) {
				drawColoredTimeline(gc, padding + xoffset, padding + yoffset, screenx, screeny, game.width, t, true, color);				
			}else {
				drawColoredTimeline(gc, padding + xoffset, padding + yoffset, screenx, screeny, game.width, t, false, color);
			}
			layerCTR++;
		}
	}
	
	
	public static void drawTimelineV(GraphicsContext gc, int x, int y, int screenx, int screeny, int width, Timeline t, boolean active) {
		int lastWindex = t.wboards.size();
		int lastBindex = t.bboards.size();
		int boardwidth = squarewidth * width;
		int boardOffset = boardwidth + padding;
		if(active) {			
			drawTimelineArrow(gc, (lastWindex + lastBindex) * (boardOffset), x - 10, y + (boardwidth / 2), screenx, screeny);
		}
		else{
			drawTimelineArrowColored(gc, Color.GREY, (lastWindex + lastBindex) * (boardOffset), x - 10, y + (boardwidth / 2), screenx, screeny);
		}
		//Draw white
		int offset = 0;
		if(!t.colorStart) {
			offset = 1;			
		}
		for(int i = 0; i < t.wboards.size(); i++) {
			int xoffset = ((i * 2) + offset) * boardOffset;
			boolean playable = false;
			if( i == t.wboards.size() - 1 && t.colorPlayable) {
				playable = true;
			}
			ChessDrawer.drawFullBoardV(gc, t.wboards.get(i), true, playable, xoffset + x, y, screenx, screeny);
		}
		//Draw black
		offset = 0;
		if(t.colorStart) {
			offset = 1;
		}
		for(int i = 0; i < t.bboards.size(); i++) {
			int xoffset = ((i * 2) + offset) * boardOffset;
			boolean playable = false;
			if( i == t.bboards.size() - 1 && !t.colorPlayable) {
				playable = true;
			}
			ChessDrawer.drawFullBoardV(gc, t.bboards.get(i), false, playable, xoffset + x , y, screenx, screeny);
		}
	}
	
	public static void drawColoredTimeline(GraphicsContext gc, int x, int y, int screenx, int screeny, int width, Timeline t, boolean active, boolean color) {
		int arrowSize;
		if(color) {
			arrowSize = t.wboards.size();
		}
		else {
			arrowSize = t.bboards.size();
		}
		int boardwidth = squarewidth * width;
		int boardOffset = boardwidth + padding;
		if(active) {			
			drawTimelineArrow(gc, arrowSize * (boardOffset), x - 10, y + (boardwidth / 2), screenx, screeny);
		}
		else{
			drawTimelineArrowColored(gc, Color.GREY, arrowSize * (boardOffset), x - 10, y + (boardwidth / 2), screenx, screeny);
		}
		//Draw white
		if(color) {
			int offset = 0;
			if(!t.colorStart) {
				offset++;
			}
			for(int i = 0; i < t.wboards.size(); i++) {
				int xoffset = ((i) + offset) * boardOffset;
				boolean playable = false;
				if( i == t.wboards.size() - 1 && t.colorPlayable) {
					playable = true;
				}
				ChessDrawer.drawFullBoardV(gc, t.wboards.get(i), true, playable, xoffset + x, y, screenx, screeny);
			}
		}
		//Draw black
		else {
			int offset = 0;
			for(int i = 0; i < t.bboards.size(); i++) {
				int xoffset = ((i) + offset) * boardOffset;
				boolean playable = false;
				if( i == t.bboards.size() - 1 && !t.colorPlayable) {
					playable = true;
				}
				ChessDrawer.drawFullBoardV(gc, t.bboards.get(i), false, playable, xoffset + x , y, screenx, screeny);
			}
		}
	}
	
	public static void drawFullBoardV(GraphicsContext gc, Board b, boolean color, boolean playable, int x, int y, int screenx, int screeny) {
		if (piecesprites == null) {
			try {
				piecesprites = new Image(new FileInputStream("res/Pieces-hirez.png"));
			} catch (FileNotFoundException e) {
				System.out.println("Cannot find the Image :l");
				e.printStackTrace();
			}
		}
		drawChessBoard(gc, b.width, b.height, color, playable, x, y, screenx, screeny);
		for (int squarex = 0; squarex < b.width; squarex++) {
			for (int squarey = 0; squarey < b.height; squarey++) {
				int piecenum = b.getSquare(squarex,squarey);
				piecenum = piecenum < 0 ? piecenum * -1 : piecenum;
				int xoffset = (squarewidth * squarex);
				int yoffset = (squarewidth * (b.height - squarey - 1));
				gc.drawImage(piecesprites, (piecenum % SPRITESHEETWIDTH) * SPRITEWIDTH, (piecenum / SPRITESHEETWIDTH) * SPRITEWIDTH, SPRITEWIDTH,
						SPRITEWIDTH, x + xoffset - screenx, y + yoffset - screeny, squarewidth, squarewidth);
			}
		}
	}
	
	public static void drawChessBoard(GraphicsContext gc,  int width, int height, boolean color, boolean playable, int x, int y, int screenx,
			int screeny) {
		if (color) {
			gc.setFill(Color.LIGHTGRAY);
		} else {
			gc.setFill(Color.BLACK);
		}
		if (playable) {
			gc.fillRoundRect(x - 15 - screenx, y - 15 - screeny, squarewidth * width + 30, squarewidth * height + 30, 20, 20);
		} else {
			gc.fillRoundRect(x - 5 - screenx, y - 5 - screeny, squarewidth * width + 10, squarewidth * height + 10, 15, 15);
		}
		for (int squarex = 0; squarex < width; squarex++) {
			for (int squarey = 0; squarey < height; squarey++) {
				if ((squarex + squarey) % 2 == 1) {
					gc.setFill(ChessDrawer.darkColor);
				} else {
					gc.setFill(ChessDrawer.lightColor);
				}
				gc.fillRect(x + squarex * squarewidth - screenx, y + squarey * squarewidth - screeny, squarewidth, squarewidth);
			}
		}
	}
	
	public static void drawTimelineArrow(GraphicsContext gc, int len, int x, int y, int screenx, int screeny) {
		gc.setFill(ChessDrawer.arrowColor);
		gc.fillArc(x - 20 - screenx, y - 20 - screeny, 40, 40, 0, 360, ArcType.ROUND);
		gc.fillRect(x - screenx, y - 20 - screeny, len, 40);
		double[] arrowx = { (double) x + len - screenx, (double) x + len - screenx, (double) x + len + 50 - screenx };
		double[] arrowy = { (double) y - 40 - screeny, (double) y + 40 - screeny, (double) y - screeny};
		gc.fillPolygon(arrowx, arrowy, 3);
	}
	
	public static void drawTimelineArrowColored(GraphicsContext gc, Color c, int len, int x, int y, int screenx, int screeny) {
		gc.setFill(c);
		gc.fillArc(x - 20 - screenx, y - 20 - screeny, 40, 40, 0, 360, ArcType.ROUND);
		gc.fillRect(x - screenx, y - 20 - screeny, len, 40);
		double[] arrowx = { (double) x + len - screenx, (double) x + len - screenx, (double) x + len + 30 - screenx };
		double[] arrowy = { (double) y - 40 - screeny, (double) y + 40 - screeny, (double) y - screeny};
		gc.fillPolygon(arrowx, arrowy, 3);
	}
	
	public static void drawSquareV(GraphicsContext gc, Color c, int width, int height, int minLayer , CoordFive squareLoc,  int screenx, int screeny ) {
		gc.setFill(c);
		int xboardOffset = (width * squarewidth) + padding;
		int yboardOffset = (height * squarewidth) + padding;
		int xOffset;
		if(Controller.viewType == Controller.FULL_VIEW) {
			xOffset = (2 * (squareLoc.T-1) * (xboardOffset) + (squareLoc.x * squarewidth) + padding);
			if(!squareLoc.color) {
				xOffset += xboardOffset;
			}
		}else {
			xOffset = ((squareLoc.T-1) * (xboardOffset) + (squareLoc.x * squarewidth) + padding);				
		}
		
		int yOffset = ((squareLoc.L + 1) * (yboardOffset)) - ((squareLoc.y+1) * squarewidth);
		gc.fillRect(xOffset - screenx, yOffset - screeny, squarewidth, squarewidth);
	}
	
	public static void drawAllSquaresV(GraphicsContext gc, Color c, ArrayList<CoordFour> list, boolean color, int width , int height, int minLayer,  int screenx, int screeny) {
		for(CoordFour c4 : list) {
			ChessDrawer.drawSquareV(gc, c, width, height, minLayer, new CoordFive(c4,color), screenx, screeny);
		}
	}
	
	public static void drawMoveLine(GraphicsContext gc, DrawableArrow da, int screenx, int screeny) {
		gc.setLineWidth(3);
		gc.setStroke(Color.CORNFLOWERBLUE);
		//gc.strokeLine(da.startX - screenx + halfSquare, da.startY - screeny + halfSquare, da.endX - screenx + halfSquare, da.endY - screeny + halfSquare);
		gc.strokeLine(coordToX(da.origin,width,height) + halfSquare - screenx , coordToY(da.origin,width,height)  + halfSquare - screeny, coordToX(da.dest,width,height) + halfSquare - screenx, coordToY(da.dest,width,height) + halfSquare - screeny);
	}
	
	public static void drawLine(GraphicsContext gc, DrawableArrow da, int screenx, int screeny) {
		gc.setLineWidth(3);
		gc.setStroke(Color.CORNFLOWERBLUE);
		gc.strokeLine(da.startX - screenx + halfSquare, da.startY - screeny + halfSquare, da.endX - screenx + halfSquare, da.endY - screeny + halfSquare);
	}
	
	public static void drawMove(Move m) {
		
	}
	
	public static int coordToX(CoordFour square, boolean color, int width, int height) {
		int xboardOffset = (width * squarewidth) + padding;
		int xOffset = (2 * (square.T-1) * (xboardOffset) + (square.x * squarewidth) + padding);
		if(!color) {
			xOffset += xboardOffset;
		}
		return xOffset;
	}
	
	public static int coordToX(CoordFive square, int width, int height) {
		if(Controller.viewType == Controller.BLACK_VIEW || Controller.viewType == Controller.WHITE_VIEW) {
			int xboardOffset = (width * squarewidth) + padding;
			int xOffset = ((square.T-1) * (xboardOffset) + (square.x * squarewidth) + padding);
			return xOffset;
		}
		int xboardOffset = (width * squarewidth) + padding;
		int xOffset = (2 * (square.T-1) * (xboardOffset) + (square.x * squarewidth) + padding);
		if(!square.color) {
			xOffset += xboardOffset;
		}
		return xOffset;
	}
	
	public static int coordToY(CoordFour square, int width, int height) {
		int yboardOffset = (height * squarewidth) + padding;
		int yOffset = ((square.L + 1) * (yboardOffset)) - ((square.y+1) * squarewidth);
		return yOffset;
	}
	
}

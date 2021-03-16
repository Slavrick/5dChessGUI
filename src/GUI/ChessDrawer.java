package GUI;

import javafx.scene.paint.*;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import engine.Board;
import engine.CoordFive;
import engine.CoordFour;
import engine.GameState;
import engine.Timeline;
import javafx.scene.canvas.GraphicsContext;

public class ChessDrawer {

	public static Image piecesprites;
	public static Color darkColor = Color.DARKGOLDENROD;
	public static Color lightColor = Color.TAN;
	public static Color multiverseLight = Color.ANTIQUEWHITE;
	public static Color multiverseDark = Color.SILVER;

	private static final int SPRITEWIDTH = 32;
	private static final int SPRITESHEETWIDTH = 10;
	public static int squarewidth = 32;
	public static int padding = 50;
	public static int halfPadding = padding / 2;
		
	public static void drawPromotionPrompt() {
		
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
				gc.fillText( (L + game.minTL) + "L, " + (T+1) + "T", (T * squareWidth) - screenx + halfPadding, (L * squareHeight) - screeny + halfPadding + 10);
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
			int yoffset = (layerCTR * (boardHeight + padding));
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
	}
	
	public static void drawTimelineV(GraphicsContext gc, int x, int y, int screenx, int screeny, int width, Timeline t, boolean active) {
		int lastWindex = t.wboards.size();
		int lastBindex = t.bboards.size();;
		int boardwidth = squarewidth * width;
		int boardOffset = boardwidth + padding;
		if(active) {			
			drawArrowV(gc, (lastWindex + lastBindex) * (boardOffset), x - 10, y + (boardwidth / 2), screenx, screeny);
		}
		else{
			drawArrowV(gc, Color.GREY, (lastWindex + lastBindex) * (boardOffset), x - 10, y + (boardwidth / 2), screenx, screeny);
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
	
	public static void drawFullBoardV(GraphicsContext gc, Board b, boolean color, boolean playable, int x, int y, int screenx, int screeny) {
		if (piecesprites == null) {
			try {
				piecesprites = new Image(new FileInputStream("res/PieceSprites.png"));
			} catch (FileNotFoundException e) {
				System.out.println("Cannot find the Image :l");
				e.printStackTrace();
			}
		}
		drawChessBoardColoredV(gc, b.width, b.height, color, playable, x, y, screenx, screeny);
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
	
	public static void drawChessBoardColoredV(GraphicsContext gc,  int width, int height, boolean color, boolean playable, int x, int y, int screenx,
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
					gc.setFill(Color.DARKGOLDENROD);
				} else {
					gc.setFill(Color.TAN);
				}
				gc.fillRect(x + squarex * squarewidth - screenx, y + squarey * squarewidth - screeny, squarewidth, squarewidth);
			}
		}
	}
	
	public static void drawArrowV(GraphicsContext gc, int len, int x, int y, int screenx, int screeny) {
		gc.setFill(Color.PURPLE);
		gc.fillRect(x - screenx, y - 20 - screeny, len, 40);
		double[] arrowx = { (double) x + len - screenx, (double) x + len - screenx, (double) x + len + 30 - screenx };
		double[] arrowy = { (double) y - 40 - screeny, (double) y + 40 - screeny, (double) y - screeny};
		gc.fillPolygon(arrowx, arrowy, 3);
	}
	
	public static void drawArrowV(GraphicsContext gc, Color c, int len, int x, int y, int screenx, int screeny) {
		gc.setFill(c);
		gc.fillRect(x - screenx, y - 20 - screeny, len, 40);
		double[] arrowx = { (double) x + len - screenx, (double) x + len - screenx, (double) x + len + 30 - screenx };
		double[] arrowy = { (double) y - 40 - screeny, (double) y + 40 - screeny, (double) y - screeny};
		gc.fillPolygon(arrowx, arrowy, 3);
	}
	
	public static void drawSquareV(GraphicsContext gc, Color c, int width, int height, int minLayer , CoordFive squareLoc,  int screenx, int screeny ) {
		gc.setFill(c);
		int xboardOffset = (width * squarewidth) + padding;
		int yboardOffset = (height * squarewidth) + padding;
		int xOffset = (2 * (squareLoc.T-1) * (xboardOffset) + (squareLoc.x * squarewidth) + padding);
		if(!squareLoc.color) {
			xOffset += xboardOffset;
		}
		int yOffset = ((squareLoc.L - minLayer + 1) * (yboardOffset)) - ((squareLoc.y+1) * squarewidth);
		gc.fillRect(xOffset - screenx, yOffset - screeny, squarewidth, squarewidth);
	}
	
	public static void drawAllSquaresV(GraphicsContext gc, Color c, ArrayList<CoordFour> list, boolean color, int width , int height, int minLayer,  int screenx, int screeny) {
		for(CoordFour c4 : list) {
			ChessDrawer.drawSquareV(gc, c, width, height, minLayer, new CoordFive(c4,color), screenx, screeny);
		}
	}
	
	
}

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
	private static final int SPRITEWIDTH = 32;
	private static final int SPRITESHEETWIDTH = 10;
	public static Color lightColor = Color.TAN;
	public static Color darkColor = Color.DARKGOLDENROD;
	public static int squarewidth = 32;
	public static int padding = 50;
	
	
	// TODO fix this because it needs to clear the area.
	
	public static void drawMultiverse(GraphicsContext gc, int x, int y, GameState game) {
		int SquareWidth = 32;
		int layerCTR = 0;
		int boardwidth = SquareWidth * game.width;
		int boardHeight = SquareWidth * game.height;
		int padding = 50;
		for (Timeline t : game.multiverse) {
			int offsetNum = (t.Tstart - 1) * 2;
			if (!t.colorStart) {
				offsetNum++;
			}
			int xoffset = offsetNum * (boardwidth + padding);
			int yoffset = (layerCTR * (boardHeight + padding));
			drawTimeline(gc, padding + xoffset, padding + yoffset, game.width, t);
			layerCTR++;
		}

	}

	public static void drawTimeline(GraphicsContext gc, int x, int y, int width, Timeline t) {
		int lastWindex = t.wboards.size();
		int lastBindex = t.bboards.size();
		int SquareWidth = 32;
		int boardwidth = SquareWidth * width;
		int padding = 50;
		drawArrow(gc, x, y + (boardwidth / 2), (lastWindex + 2) * (boardwidth + padding));
		if (t.colorStart) {
			int offsetCTR = 0;
			for (int i = 0; i < lastBindex || i < lastWindex; i++) {
				if (i < lastWindex) {
					Board b = t.wboards.get(i);
					if (lastWindex - 1 == i && lastBindex == i) {
						drawFullBoard(gc, x + (offsetCTR * (boardwidth + padding)), y, true, b, true);
					} else {
						drawFullBoard(gc, x + (offsetCTR * (boardwidth + padding)), y, true, b, false);
					}
					offsetCTR++;
				}
				if (i < lastBindex) {
					Board b = t.bboards.get(i);
					if (lastBindex - 1 == i && lastWindex - 1 == i ) {
						drawFullBoard(gc, x + (offsetCTR * (boardwidth + padding)), y, false, b, true);
					} else {
						drawFullBoard(gc, x + (offsetCTR * (boardwidth + padding)), y, false, b, false);
					}
					offsetCTR++;
				}
			}
		} else {
			int offsetCTR = 0;
			for (int i = 0; i < lastBindex || i < lastWindex; i++) {
				if (i < lastBindex) {
					Board b = t.bboards.get(i);
					if (lastBindex - 1 == i && lastWindex == i) {
						drawFullBoard(gc, x + (offsetCTR * (boardwidth + padding)), y, false, b, true);
					} else {
						drawFullBoard(gc, x + (offsetCTR * (boardwidth + padding)), y, false, b, false);
					}
					offsetCTR++;
				}
				if (i < lastWindex) {
					Board b = t.wboards.get(i);
					if (lastWindex - 1 == i && lastBindex - 1 == i) {
						drawFullBoard(gc, x + (offsetCTR * (boardwidth + padding)), y, true, b, true);
					} else {
						drawFullBoard(gc, x + (offsetCTR * (boardwidth + padding)), y, true, b, false);
					}
					offsetCTR++;
				}
			}
		}
	}

	//this func depricated. ill delete it soon TODO delete func
	public static void drawFullBoard(GraphicsContext gc, int x, int y, boolean color, Board b) {
		int SquareWidth = 32;
		if (piecesprites == null) {
			try {
				piecesprites = new Image(new FileInputStream("res/PieceSprites.png"));
			} catch (FileNotFoundException e) {
				System.out.println("Cannot find the Image :l");
				e.printStackTrace();
			}
		}
		drawChessBoardColored(gc, x, y, b.width, b.height, color, false);
		for (int squarex = 0; squarex < b.width; squarex++) {
			for (int squarey = 0; squarey < b.height; squarey++) {
				int piecenum = b.getSquare(squarex,squarey);
				int xoffset = (SquareWidth * squarex);
				int yoffset = (SquareWidth * (b.height - squarey - 1));
				gc.drawImage(piecesprites, (piecenum % SPRITESHEETWIDTH) * SPRITEWIDTH, (piecenum / SPRITESHEETWIDTH) * SPRITEWIDTH, SPRITEWIDTH,
						SPRITEWIDTH, x + xoffset, y + yoffset, SquareWidth, SquareWidth);
			}
		}
	}

	// TODO fix this, load piecesprites somewhere else, so that it condenses the
	// code.
	public static void drawFullBoard(GraphicsContext gc, int x, int y, boolean color, Board b, boolean playable) {
		int SquareWidth = 32;
		if (piecesprites == null) {
			try {
				piecesprites = new Image(new FileInputStream("res/PieceSprites.png"));
			} catch (FileNotFoundException e) {
				System.out.println("Cannot find the Image :l");
				e.printStackTrace();
			}
		}
		drawChessBoardColored(gc, x, y, b.width, b.height, color, playable);
		for (int squarex = 0; squarex < b.width; squarex++) {
			for (int squarey = 0; squarey < b.height; squarey++) {
				int piecenum = b.getSquare(squarex,squarey);
				int xoffset = (SquareWidth * squarex);
				int yoffset = (SquareWidth * (b.height - squarey - 1));
				gc.drawImage(piecesprites, (piecenum % SPRITESHEETWIDTH) * SPRITEWIDTH, (piecenum / SPRITESHEETWIDTH) * SPRITEWIDTH, SPRITEWIDTH,
						SPRITEWIDTH, x + xoffset, y + yoffset, SquareWidth, SquareWidth);
			}
		}
	}

	// this one, OK
	public static void drawChessBoardColored(GraphicsContext gc, int x, int y, int width, int height, boolean color,
			boolean playable) {
		int SquareWidth = 32;
		if (color) {
			gc.setFill(Color.LIGHTGRAY);
		} else {
			gc.setFill(Color.BLACK);
		}
		if (playable) {
			gc.fillRoundRect(x - 10, y - 10, SquareWidth * width + 20, SquareWidth * height + 20, 15, 15);
		} else {
			gc.fillRoundRect(x - 5, y - 5, SquareWidth * width + 10, SquareWidth * height + 10, 15, 15);
		}
		for (int squarex = 0; squarex < width; squarex++) {
			for (int squarey = 0; squarey < height; squarey++) {
				if ((squarex + squarey) % 2 == 1) {
					gc.setFill(Color.TAN);
				} else {
					gc.setFill(Color.DARKGOLDENROD);
				}
				gc.fillRect(x + squarex * SquareWidth, y + squarey * SquareWidth, SquareWidth, SquareWidth);
			}
		}
	}

	public static void drawArrow(GraphicsContext gc, int x, int y, int len) {
		gc.setFill(Color.PURPLE);
		gc.fillRect(x, y - 20, len, 40);
		double[] arrowx = { (double) x + len, (double) x + len, (double) x + len + 30 };
		double[] arrowy = { (double) y - 40, (double) y + 40, (double) y };
		gc.fillPolygon(arrowx, arrowy, 3);

	}
	
	public static void drawSquare(GraphicsContext gc, int width, int height, int minLayer, CoordFive squareLoc , Color c ) {
		gc.setFill(c);
		int xboardOffset = (width * squarewidth) + padding;
		int yboardOffset = (height * squarewidth) + padding;
		int xOffset = (2 * (squareLoc.T-1) * (xboardOffset) + (squareLoc.x * squarewidth) + padding);
		if(!squareLoc.color) {
			xOffset += xboardOffset;
		}
		int yOffset = ((squareLoc.L - minLayer + 1) * (yboardOffset)) - ((squareLoc.y+1) * squarewidth);
		gc.fillRect(xOffset, yOffset, squarewidth, squarewidth);
	}
	
	public static void drawAllSquares(GraphicsContext gc, int width, int height, int minLayer, Color c , ArrayList<CoordFour> list, boolean color) {
		for(CoordFour c4 : list) {
			ChessDrawer.drawSquare(gc, width, height, minLayer, new CoordFive(c4,color), c);
		}
	}
}

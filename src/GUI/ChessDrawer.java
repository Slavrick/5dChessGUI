package GUI;

import javafx.scene.paint.*;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import engine.Board;
import engine.Timeline;
import javafx.scene.canvas.GraphicsContext;

public class ChessDrawer {

	public static Image piecesprites;
	private static final int SPRITESHEETWIDTH = 10;

	public static void drawTimeline(GraphicsContext gc, int x, int y, Timeline t) {
		int lastWindex = t.wboards.size();
		int lastBindex = t.bboards.size();
		int boardwidth = 32 * 8;
		int padding = 50;
		drawArrow(gc, x, y + (boardwidth / 2), (lastWindex + 2) * (boardwidth + padding));
		if (t.colorStart) {
			int offsetCTR = 0;
			for (int i = 0; i < lastBindex || i < lastWindex; i++) {
				if (i < lastWindex) {
					System.out.println("W" + (i + t.Tstart));
					Board b = t.wboards.get(i);
					System.out.println(b);
					drawFullBoard(gc, x + (offsetCTR * (boardwidth + padding)), y, true, b);
					offsetCTR++;
				}
				if (i < lastBindex) {
					System.out.println("B" + (i + t.Tstart));
					Board b = t.bboards.get(i);
					System.out.println(b);
					drawFullBoard(gc, x + (offsetCTR * (boardwidth + padding)), y, false, b);
					offsetCTR++;
				}
			}
		} else {
			for (int i = 0; i < lastBindex || i < lastWindex; i++) {
				if (i < lastBindex) {
					System.out.println("B");
					System.out.println(t.bboards.get(i));
				}
				if (i < lastWindex) {
					System.out.println("W");
					System.out.println(t.wboards.get(i));
				}
			}
		}
	}

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
		drawChessBoardColored(gc, x, y, color);
		for (int squarex = 0; squarex < 8; squarex++) {
			for (int squarey = 0; squarey < 8; squarey++) {
				int piecenum = b.brd[squarey][squarex];
				gc.drawImage(piecesprites, (piecenum % SPRITESHEETWIDTH) * 32, (piecenum / SPRITESHEETWIDTH) * 32, 32,
						32, x + (32 * squarex), y + (32 * squarey), 32, 32);
			}
		}
	}

	public static void drawChessBoard(GraphicsContext gc, int x, int y) {
		int SquareWidth = 32;
		gc.setFill(Color.TAN);
		for (int squarey = 0; squarey < 8; squarey++) {
			for (int squarex = 0; squarex < 4; squarex++) {
				int xOffset = (squarex * 2 * SquareWidth);
				if (squarey % 2 == 1) {
					xOffset += SquareWidth;
				}
				gc.fillRect(x + xOffset, y + (squarey * SquareWidth), SquareWidth, SquareWidth);
			}
		}
		gc.setFill(Color.DARKGOLDENROD);
		for (int squarey = 0; squarey < 8; squarey++) {
			for (int squarex = 0; squarex < 4; squarex++) {
				int xOffset = (squarex * 2 * SquareWidth);
				if (squarey % 2 == 0) {
					xOffset += SquareWidth;
				}
				gc.fillRect(x + xOffset, y + (squarey * SquareWidth), SquareWidth, SquareWidth);
			}
		}
	}

	public static void drawChessBoardColored(GraphicsContext gc, int x, int y, boolean color) {
		int SquareWidth = 32;

		if (color) {
			gc.setFill(Color.LIGHTGRAY);
		} else {
			gc.setFill(Color.BLACK);
		}
		gc.fillRoundRect(x - 10, y - 10, SquareWidth * 8 + 20, SquareWidth * 8 + 20, 15, 15);
		gc.setFill(Color.TAN);
		for (int squarey = 0; squarey < 8; squarey++) {
			for (int squarex = 0; squarex < 4; squarex++) {
				int xOffset = (squarex * 2 * SquareWidth);
				if (squarey % 2 == 1) {
					xOffset += SquareWidth;
				}
				gc.fillRect(x + xOffset, y + (squarey * SquareWidth), SquareWidth, SquareWidth);
			}
		}
		gc.setFill(Color.DARKGOLDENROD);
		for (int squarey = 0; squarey < 8; squarey++) {
			for (int squarex = 0; squarex < 4; squarex++) {
				int xOffset = (squarex * 2 * SquareWidth);
				if (squarey % 2 == 0) {
					xOffset += SquareWidth;
				}
				gc.fillRect(x + xOffset, y + (squarey * SquareWidth), SquareWidth, SquareWidth);
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
}

package fileIO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import engine.Board;
import engine.CoordFour;
import engine.GameState;
import engine.Move;
import engine.Timeline;

public class FENParser {
	private String FileInput;

	public final static String STANDARDBOARD = "8;8;rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR;w;KQkq;-;0;1";
	public final static String PRINCESSBOARD = "8;8;rnbskbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBSKBNR;w;KQkq;-;0;1";
	public final static String PROTECTEDPAWN = "8;8;rqbnkbnr/pppppppp/8/8/8/8/PPPPPPPP/RQBNKBNR;w;KQkq;-;0;1";
	public final static String HALFREFLECTED = "8;8;rnbkqbnr/pppppppp/8/8/8/8/PPPPPPPP/RQBNKBNR;w;KQkq;-;0;1";

	public static Board getBoardFromString(String FENFormat) {
		// Splits string delineated by semicolon
		String[] splitString = FENFormat.split(";");

		/*
		 * for(String s : splitString) { System.out.println(s); }
		 */
		// get the height and width
		int height = Integer.parseInt(splitString[0]);
		int width = Integer.parseInt(splitString[1]);
		Board b = new Board(height, width);
		// gets the raw pieces, @todo add checks for proper FEN
		String[] rows = splitString[2].split("/");
		int row = 0;
		int col = 0;
		try {
			for (String s : rows) {
				for (char c : s.toCharArray()) {
					if (c >= 'A') {
						b.brd[height - row - 1][col] = indexOfElement(Board.pieceChars, c);
						col++;
					} else if (c <= '9' && c >= '1') {
						for (int i = 0; i < (int) (c) - 48; i++) {
							b.brd[row][col] = indexOfElement(Board.pieceChars, '_');
							col++;
						}
					} else {
						System.out.println("There was an error reading the FEN provided");
						col++;
					}
				}
				row++;
				col = 0;
			}
		} catch (ArrayIndexOutOfBoundsException e) { // this means that the board was out of bounds, which means the
														// corrilating numbers are wrong
			System.out.println(
					"There was an indexOutOfBounds -- this probably means the height or width was incorrectly provided");
			System.out.println("Height of: " + (height - row - 1) + "Width of: " + (col - height - 1));
			return null;
		}
		if (splitString[3].charAt(0) == 'w') {
			b.color = Board.pieceColor.BLACK.ordinal();
		}
		if (splitString[3].charAt(0) == 'w') {
			b.color = Board.pieceColor.BLACK.ordinal();
		}
		return b;
	}

	/*
	 * reference string as follows: >4;4;2;0;b >
	 * >r2k/4/4/P2K;;-;w1;3;(1,1,1,0)(1,2,1,0);(1,4,1,0)(3,4,1,0);(4,1,2,0)(3,2,1,0)
	 * > >r2k/4/2K1/P2K;;-;b1;2;(1,4,1,1)(1,2,1,1);(3,2,2,1)(2,3,2,1) 0 -- fen
	 * pieces 1 -- casling rights 2 -- en passent 3 -- time start 4 -- number of
	 * moves 5+ -- moves
	 */

	/**
	 * Takes in a file location string and returns a gamestate to match the FEN. The
	 * formatting is described in the markdown doc files.
	 * 
	 * @param fileLocation the location of the file you are loading
	 * @return return a gamestate to match the FEN passed in
	 */
	public static GameState FENtoGS(String fileLocation) {
		File file = new File(fileLocation);
		ArrayList<String> lines = new ArrayList<String>();
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
			br.close();
		} catch (IOException e) {
			System.out.println("File Cound not be opened for reading: " + fileLocation);
			return null;
		}
		if (lines.size() < 2) {
			return null;
		}
		// Parse First Line;
		String[] firstLine = lines.get(0).split(";");
		int width = Integer.parseInt(firstLine[0]);
		int height = Integer.parseInt(firstLine[1]);
		int numLayers = Integer.parseInt(firstLine[2]);
		int startingTimeline = Integer.parseInt(firstLine[3]);
		boolean color = firstLine[4].charAt(0) == 'w';
		// Parse Rest of game;
		if (numLayers != lines.size() - 1) {
			return null;
		}
		ArrayList<Timeline> starters = new ArrayList<Timeline>();
		for(int i = 1; i < lines.size(); i++) {
			String line = lines.get(i);
			starters.add(FENtoTL(line,width,height,startingTimeline + i - 1));
			
		}
		GameState gs = new GameState(starters, startingTimeline, (startingTimeline + starters.size() - 1));
		gs.color = color;
		return gs;
	}

	//this doesnt work if the move is from another TL but jumps to the created one. @TODO
	public static Timeline FENtoTL(String fen, int width, int height, int layer) {
		String[] splitString = fen.split(";");
		// parses board pieces
		String[] rows = splitString[0].split("/");
		Board b = new Board(width, height);
		int row = 0;
		int col = 0;
		try {
			for (String s : rows) {
				for (char c : s.toCharArray()) {
					if (c >= 'A') {
						b.brd[height - row - 1][col] = indexOfElement(Board.pieceChars, c);
						col++;
					} else if (c <= '9' && c >= '1') {
						for (int i = 0; i < (int) (c) - 48; i++) {
							b.brd[row][col] = indexOfElement(Board.pieceChars, '_');
							col++;
						}
					} else {
						System.out.println("There was an error reading the FEN provided");
						col++;
					}
				}
				row++;
				col = 0;
			}
		} catch (ArrayIndexOutOfBoundsException e) { // this means that the board was out of bounds, which means the
														// corrilating numbers are wrong
			System.out.println(
					"There was an indexOutOfBounds -- this probably means the height or width was incorrectly provided");
			System.out.println("Height of: " + (height - row - 1) + "Width of: " + (col - height - 1));
			return null;
		}
		// Castling rights
		populateCastlingRights(b, splitString[1]);
		// en passent @TODO not implemented yet
		// splitString[2]
		// find start time
		boolean color;
		if (splitString[3].charAt(0) == 'w') {
			color = true;
		} else {
			color = false;
		}
		int timeStart = Integer.parseInt(splitString[3].substring(1, splitString[3].length()));
		Timeline t = new Timeline(b, color, timeStart, layer);
		// If there are moves get them..
		if (splitString.length <= 5) {
			return t;
		}
		int numMoves = Integer.parseInt(splitString[4]);
		for (int i = 5; i < 5 + numMoves; i++) {
			Move m = stringToMove(splitString[i]);
			if (m.type == 1) {
				t.addSpatialMove(m, color);
				color = !color;
			} else {
				t.addJumpingMove(m.origin, color);
				color = !color;
			}
		}
		return t;
	}

	public static void populateCastlingRights(Board b, String castlingrights) {

	}// @TODO

	// Recive string (a,b,c,d) return int[] {a,b,c,d} or coordFIVE
	public static CoordFour stringtoCoord(String coord) {
		coord = coord.substring(1, coord.length() - 1);
		String[] coords = coord.split(",");
		int x = Integer.parseInt(coords[0]);
		int y = Integer.parseInt(coords[1]);
		int t = Integer.parseInt(coords[2]);
		int l = Integer.parseInt(coords[3]);
		CoordFour cd = new CoordFour(x, y, t, l);
		return cd;
	}

	// Recive string (x0,y0,t0,l0)(x1,y1,t1,l1) --> move data struct
	public static Move stringToMove(String move) {
		CoordFour c1 = stringtoCoord(move.substring(0, move.indexOf(")") + 1));
		CoordFour c2 = stringtoCoord(move.substring(move.indexOf(")") + 1, move.length()));
		return new Move(c1, c2);
	}

	public static Move stringToMove(String coord1, String coord2) {
		CoordFour c1 = stringtoCoord(coord1);
		CoordFour c2 = stringtoCoord(coord2);
		return new Move(c1, c2);
	}

	public static int indexOfElement(int[] arr, int target) {
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == target) {
				return i;
			}
		}
		return 0;
	}

	public static int indexOfElement(char[] arr, char target) {
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == target) {
				return i;
			}
		}
		return 0;
	}
}

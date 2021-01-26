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
import engine.GameStateManager;
import engine.Move;
import engine.Timeline;

public class FENParser {

	public final String STDBOARDFEN = "r*nbqk*bnr*/p*p*p*p*p*p*p*p*/8/8/8/8/P*P*P*P*P*P*P*P*/R*NBQK*BNR*:0:1:W";
	public final String STD_PRINCESS_BOARDFEN = "r*nbsk*bnr*/p*p*p*p*p*p*p*p*/8/8/8/8/P*P*P*P*P*P*P*P*/R*NBSK*BNR*:0:1:W";
	public final String STD_DEFENDEDPAWN_BOARDFEN = "r*qbnk*bnr*/p*p*p*p*p*p*p*p*/8/8/8/8/P*P*P*P*P*P*P*P*/R*QBNK*BNR*:0:1:W";
	
	
	// TODO this stuff doesnt work for - or + 0
	public static GameState FENToGS(String fileLoc) {
		File file = new File(fileLoc);
		return FENToGS(file);
	}

	public static GameState FENToGS(File file) {
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
			System.out.println("File Cound not be opened for reading: " + file.getAbsolutePath());
			return null;
		}
		if (lines.size() < 2) {
			return null;
		}
		String[] firstLine = lines.get(0).split(";");
		int width = Integer.parseInt(firstLine[0]);
		int height = Integer.parseInt(firstLine[1]);
		boolean evenTimelines = firstLine[2].charAt(0) == '1';
		int numOrigins = Integer.parseInt(firstLine[3]);
		int minTL = Integer.parseInt(firstLine[4]);
		boolean color = firstLine[5].charAt(0) == 'w';
		Timeline[] origins = new Timeline[numOrigins];
		for (int i = 1; i <= numOrigins; i++) {
			String tlString = lines.get(i);
			origins[i - 1] = getTimelineFromString(tlString, i - minTL, width, height);
		}
		Move[] moves;
		if (lines.size() == 1 + numOrigins) {
			moves = null;
		} else {
			String[] movestr = lines.get(lines.size() - 1).split(";");
			moves = new Move[movestr.length];
			for (int i = 0; i < movestr.length; i++) {
				moves[i] = FENParser.stringToMove(movestr[i]);
			}
		}
		GameState game = new GameState(origins, width, height, evenTimelines, color, minTL, moves);
		return game;
	}

	public static GameStateManager FENtoGSM(String fileLoc) {
		File file = new File(fileLoc);
		return FENtoGSM(file);
	}

	public static GameStateManager FENtoGSM(File file) {
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
			System.out.println("File Cound not be opened for reading: " + file.getAbsolutePath());
			return null;
		}
		if (lines.size() < 2) {
			return null;
		}
		String[] firstLine = lines.get(0).split(";");
		int width = Integer.parseInt(firstLine[0]);
		int height = Integer.parseInt(firstLine[1]);
		boolean evenTimelines = firstLine[2].charAt(0) == '1';
		int numOrigins = Integer.parseInt(firstLine[3]);
		int minTL = Integer.parseInt(firstLine[4]);
		boolean color = firstLine[5].charAt(0) == 'w';
		Timeline[] origins = new Timeline[numOrigins];
		for (int i = 1; i <= numOrigins; i++) {
			String tlString = lines.get(i);
			origins[i - 1] = getTimelineFromString(tlString, i - minTL, width, height);
		}
		Move[] moves;
		if (lines.size() == 1 + numOrigins) {
			moves = null;
		} else {
			String[] movestr = lines.get(lines.size() - 1).split(";");
			moves = new Move[movestr.length];
			for (int i = 0; i < movestr.length; i++) {
				moves[i] = FENParser.stringToMove(movestr[i]);
			}
		}
		GameStateManager game = new GameStateManager(origins, width, height, evenTimelines, color, minTL, moves);
		return game;
	}

	public static GameStateManager shadSTDGSM(File file) {
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
			System.out.println("File Cound not be opened for reading: " + file.getAbsolutePath());
			return null;
		}
		// Parse Header
		ArrayList<String> headers = new ArrayList<String>();
		ArrayList<String> fenBoards = new ArrayList<String>();
		ArrayList<String> moves = new ArrayList<String>();
		for(String line: lines) {
			if(line.charAt(0) == '[') {
				headers.add(line);
				if(line.contains(":") && !line.contains("\"")) {
					fenBoards.add(line);
				}
			}
			else {
				moves.add(line);
			}
		}
		int width = -1;
		int height = -1;
		// Parse Headers
		
		// Parse Moves
		// GameStateManager game = new
		// GameStateManager(origins,width,height,evenTimelines,color,minTL,moves);
		// return game;
		return null;
	}

	public static Timeline getTimelineFromString(String Timeline, int layer, int width, int height) {
		String[] fields = Timeline.split(";");
		String[] rows = fields[0].split("/");
		Board b = new Board(width, height);
		int row = 0;
		int col = 0;
		try {
			for (String s : rows) {
				for (int charat = 0; charat < s.length(); charat++) {
					char c = s.charAt(charat);
					if (c >= 'A') {
						int piece = indexOfElement(Board.pieceChars, c);
						if(charat < s.length() - 1 && s.charAt(charat+1) == '*') {
							piece *= -1;
							charat++;
						}
						b.brd[height - row - 1][col] = piece;
						col++;
					} else if (c <= '9' && c >= '1') {
						for (int i = 0; i < (int) (c) - 48; i++) {
							b.brd[height - row - 1][col] = indexOfElement(Board.pieceChars, '_');
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
		FENParser.populateCastlingRights(b, fields[1]);
		if (fields[2].equals("-")) {
			b.enPassentSquare = null;
		} else {
			int file = (int) fields[2].charAt(0) - 97;
			int rank = (int) fields[2].charAt(1) - 49;
			b.enPassentSquare = new CoordFour(file, rank, 0, 0);
		}
		boolean color = fields[3].charAt(0) == 'w';
		int timeStart = Integer.parseInt(fields[3].substring(1, fields[3].length()));
		Timeline t = new Timeline(b, color, timeStart, layer);
		return t;
	}

	// recieve String in form such as KQkq for full castling rights.
	public static void populateCastlingRights(Board b, String castlingrights) {
		b.wkingSideCastle = castlingrights.contains("K");
		b.wqueenSideCastle = castlingrights.contains("Q");
		b.bkingSideCastle = castlingrights.contains("k");
		b.bqueenSideCastle = castlingrights.contains("q");
	}

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
	
	//Recieve a gamestate and a shad move and add it to the gamestate. This is expected to be correct due to
	//color ambiguity which may result in an illegal or incorrect gamestate.
	public static boolean addShadMove(GameState g, Move m) {
		
		return true;
	}
}

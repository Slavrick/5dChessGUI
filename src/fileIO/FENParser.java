package fileIO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

import engine.Board;
import engine.CoordFive;
import engine.CoordFour;
import engine.GameState;
import engine.GameStateManager;
import engine.Move;
import engine.MoveGenerator;
import engine.Timeline;
import engine.Turn;

public class FENParser {

	public final static String STDBOARDFEN = "[r*nbqk*bnr*/p*p*p*p*p*p*p*p*/8/8/8/8/P*P*P*P*P*P*P*P*/R*NBQK*BNR*:0:1:w]";
	public final static String STD_PRINCESS_BOARDFEN = "[r*nbsk*bnr*/p*p*p*p*p*p*p*p*/8/8/8/8/P*P*P*P*P*P*P*P*/R*NBSK*BNR*:0:1:w]";
	public final static String STD_DEFENDEDPAWN_BOARDFEN = "[r*qbnk*bnr*/p*p*p*p*p*p*p*p*/8/8/8/8/P*P*P*P*P*P*P*P*/R*QBNK*BNR*:0:1:w]";

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
		boolean color = firstLine[5].charAt(0) == 'w' || firstLine[5].charAt(0) == 'W';
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

	public static GameStateManager shadSTDGSM(String fileLocation) {
		File file = new File(fileLocation);
		return shadSTDGSM(file);
	}

	public static GameStateManager shadSTDGSM(File file) {
		ArrayList<String> lines = new ArrayList<String>();
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				if (line.length() != 0) {
					lines.add(line);
				}
			}
			br.close();
		} catch (IOException e) {
			System.out.println("File Cound not be opened for reading: " + file.getAbsolutePath());
			return null;
		}
		// Parse Header
		String size = null;
		String variant = null;
		String Puzzle;
		ArrayList<String> fenBoards = new ArrayList<String>();
		ArrayList<String> moves = new ArrayList<String>();
		for (String line : lines) {
			if (line.charAt(0) == '[') {
				if (line.contains("\"")) {
					line = line.toLowerCase();
				}
				if (line.contains("size")) {
					size = line;
				}
				if (line.contains("variant") || line.contains("board")) {
					variant = line;
				}
				if (line.contains(":") && !line.contains("\"")) {
					fenBoards.add(line);
				}
			} else {
				moves.add(line);
			}
		}
		// Parse Headers
		boolean evenStarters = false;// FIXME add this to parser.
		Timeline[] starters;
		GameStateManager gsm = null;
		// Parse FEN
		if (variant != null) {
			String boardChosen = variant.substring(variant.indexOf("\"") + 1, variant.lastIndexOf("\""));
			if (boardChosen.equalsIgnoreCase("custom")) {
				if (size == null) {
					return null;
				}
				int width = Integer.parseInt(size.substring(size.indexOf('\"') + 1, size.indexOf('x')));
				int height = Integer.parseInt(size.substring(size.indexOf('x') + 1, size.lastIndexOf('\"')));
				int count = 0;
				starters = new Timeline[fenBoards.size()];
				for (String FEN : fenBoards) {
					starters[count++] = getTimelineFromString(FEN, width, height);
				}

				Arrays.sort(starters);
				gsm = new GameStateManager(starters, width, height, evenStarters, true, starters[0].layer, null);
			}
			// Detect other boards / variants by string.
			else if (boardChosen.equalsIgnoreCase("standard")) {
				starters = new Timeline[1];
				starters[0] = getTimelineFromString(STDBOARDFEN, 8, 8);
				gsm = new GameStateManager(starters, 8, 8, false, true, starters[0].layer, null);
			} else if (boardChosen.equalsIgnoreCase("standard-princess")) {
				starters = new Timeline[1];
				starters[0] = getTimelineFromString(STD_PRINCESS_BOARDFEN, 8, 8);
				gsm = new GameStateManager(starters, 8, 8, false, true, starters[0].layer, null);
			}
			// XXX add more variants, or a better variant loader in the future.(Such as
			// loading from some sort of dictionary).
		} else {
			return null;
		}
		// Parse Moves
		ArrayList<String> turns = new ArrayList<String>();
		for (String s : moves) {
			if (s.contains("/")) {
				turns.addAll(Arrays.asList(s.split("/")));
			} else {
				turns.add(s);
			}
		}
		// apply moves
		for (String strturn : turns) {
			gsm.makeTurn(stringToTurn(gsm, strturn));
		}
		return gsm;
	}

	public static Timeline getTimelineFromString(String board, int width, int height) {
		board = board.substring(1, board.length() - 1);
		Board b = new Board(width, height);
		String[] fields = board.split(":");
		String[] rows = fields[0].split("/");
		int row = 0;
		int col = 0;
		try {
			for (String s : rows) {
				for (int charat = 0; charat < s.length(); charat++) {
					char c = s.charAt(charat);
					if (c >= 'A') {
						int piece = indexOfElement(Board.pieceChars, c);
						if (charat < s.length() - 1 && s.charAt(charat + 1) == '*') {
							piece *= -1;
							charat++;
						}
						b.setSquare(col, height - row - 1, piece);
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
		Timeline t = new Timeline(b, fields[3].charAt(0) == 'w', Integer.parseInt(fields[2]),
				Integer.parseInt(fields[1]));
		return t;
	}

	public static Timeline getTimelineFromString(String timelinestr, int layer, int width, int height) {
		String[] fields = timelinestr.split(";");
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
						if (charat < s.length() - 1 && s.charAt(charat + 1) == '*') {
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

	public static Turn stringToTurn(GameState g, String turnstr) {
		if (turnstr.contains(".")) {
			// Remove the turn prefix. ie. 1.Nf3 -> Nf3
			turnstr = turnstr.substring(turnstr.indexOf('.') + 1);
		}
		turnstr = turnstr.trim();
		if (turnstr.length() <= 1) {
			return null;
		}
		String[] movesStr = turnstr.split("\\s+");
		Move[] moves = new Move[movesStr.length];
		for (int i = 0; i < movesStr.length; i++) {
			String token = movesStr[i];
			if (shouldIgnoreToken(token)) {
				moves[i] = null;
				continue;
			}
			if (isSpecialMove(token)) {
				if (token.contains("O-O")) {
					moves[i] = findCastleMove(g, token);
				}
				// handle special cases
			} else {
				moves[i] = getShadMove(g, movesStr[i]);
			}
		}
		return new Turn(moves);
	}

	// Find Castling move XXX finalize this with notation changess
	private static Move findCastleMove(GameState g, String token) {
		boolean side = true;
		CoordFive temporalOrigin;
		if(token.contains("O-O-O")) {
			side = false;
		}
		else if(token.contains("O-O")) {
			side = true;
		}
		if(token.contains("(")) {
			temporalOrigin = new CoordFive(temporalToCoord(token.substring(token.indexOf('('), token.indexOf(')')+1)) , g.color);
		}
		else {
			temporalOrigin = new CoordFive(0,0,g.getTimeline(0).Tend,0,g.color);
		}
		Board origin = g.getBoard(temporalOrigin);
		int target = g.color ? -1 * Board.piece.WKING.ordinal() : -1 * Board.piece.BKING.ordinal();
		CoordFour spatialOrigin = MoveGenerator.findPiece(origin, target);
		temporalOrigin.add(spatialOrigin);
		Move castle = new Move(temporalOrigin, MoveGenerator.kingCanCastle(origin, temporalOrigin, side));
		return castle;
	}

	// Handle Castling, Promotion
	private static boolean isSpecialMove(String string) {
		if (string.contains("O-O") || string.contains("=")) {
			return true;
		}
		return false;
	}

	// Remove Ignorable Tokens such as (L0)
	private static boolean shouldIgnoreToken(String string) {
		if (string.charAt(0) == '(' && string.charAt(string.length() - 1) == ')') {
			return true;
		}
		return false;
	}

	// Recieve a gamestate and a shad move and add it to the gamestate.
	// The notation for the move should be (<L>T<T>)<Piece><SAN> for spatial,
	// (<L>T<T>)(Piece)(SAN)>(<L>T<T>)<SAN> for spatial/branching.
	// XXX make sure this works for +0 and -0
	public static Move getShadMove(GameState g, String move) {
		Move temp;// Fit this to +-0 later (increment posative and make it so +0 is 1
		if (move.contains("(") && move.indexOf("(") != move.lastIndexOf("(")) { // TODO not sure if something like
																				// Qc2(0T1)f7 is possible (origin of L0
																				// T5
			return fullStringToCoord(move);
		}
		return ambiguousStringToMove(g, move);
	}

	// This takes a full move (Such as a branching or jumping move, and turns it
	// into a move
	public static Move fullStringToCoord(String move) {
		String coord1;
		if (move.contains(">")) {
			coord1 = move.substring(0, move.indexOf('>'));
		} else {
			coord1 = move.substring(0, move.lastIndexOf('('));
		}
		String coord2 = move.substring(move.lastIndexOf('('));
		return new Move(halfStringToCoord(coord1), halfStringToCoord(coord2));
	}

	// This is for when the move is ambiguous, such as a Nf3 -> need the gamestate
	// to firgure out the source.
	public static Move ambiguousStringToMove(GameState g, String move) {
		CoordFive dest = new CoordFive(halfStringToCoord(move), g.color);
		if (dest.T == -1) {
			dest.T = g.getTimeline(0).Tend;
		}
		// Get Piece.
		char piecechar;
		int piece;
		if (move.contains(")")) {
			piecechar = move.charAt(move.indexOf(')') + 1);
		} else {
			piecechar = move.charAt(0);
		}
		if (piecechar <= 'Z') {
			piece = indexOfElement(Board.pieceChars, piecechar);
		} else {
			piece = Board.piece.WPAWN.ordinal();
		}
		// Makes sure we get the right piece color.
		piece = g.color ? piece : piece + Board.numTypes;
		CoordFour ambiguity = getAmbiguityInfo(move);
		int file = ambiguity.x;
		int rank = ambiguity.y;
		CoordFour origin = MoveGenerator.reverseLookup(g, dest, piece, rank, file);
		if (origin == null) {
			System.out.println("Could not find reverseLookup for this move :l now im gonna crash" + move);
			return null;
		}
		return new Move(origin, dest);
	}

	// Recieve a half move (<L>T<T>)(?PIECE)(?AMBIGUITY)(SAN) and gives ambiguity
	// info
	public static CoordFour getAmbiguityInfo(String move) {
		// make sure to get axb5 --> as the right thing
		CoordFour temp = new CoordFour(-1, -1, -1, -1);
		move = move.trim();
		// Trim temporal
		if (move.contains(")")) {
			move = move.substring(move.indexOf(")") + 1);
		}
		// Trim possible Piece.
		if (move.charAt(0) > 'A' && move.charAt(0) < 'a') {
			move = move.substring(1);
		}
		// trim takes symbol
		if (move.charAt(0) == 'x') {
			move = move.substring(1);
		}
		// trim san
		if (move.length() >= 3 && move.charAt(move.length() - 3) == 'x') {
			move = move.substring(0, move.length() - 3);
		} else {
			move = move.substring(0, move.length() - 2);
		}
		if (move.length() == 2) {
			temp = SANtoCoord(move);
			temp.T = -1;
			temp.L = -1;
		} else if (move.length() == 1) {
			if (move.charAt(0) >= 'a') {
				temp.x = (int) move.charAt(0) - 97;
			} else {
				temp.y = Integer.parseInt(move) - 1;
			}
		}
		return temp;
		// XXX instead of constantly changing the String, just simply get the index.
	}

	// Recieve a string in format (<L>T<T>)(?PIECE)(SAN) ; or (?PIECE)(SAN)
	// XXX make this work for +-0
	public static CoordFour halfStringToCoord(String halfmove) {
		String temporalCoord;
		// This next line should hold true, but may be wrong(hopefully not, im not
		// planning on supporting boards of 9 or greater length
		// not sure if exits (0T1)Na1x(0T1)c3 <-- must account for 'x' "takes" symbol.
		String sancoord;
		if (halfmove.charAt(halfmove.length() - 1) == 'x') {
			sancoord = halfmove.substring(halfmove.length() - 3, halfmove.length() - 1);
		} else {
			sancoord = halfmove.substring(halfmove.length() - 2);
		}
		if (halfmove.contains("(")) {
			temporalCoord = halfmove.substring(0, halfmove.indexOf(")") + 1);
		} else {
			temporalCoord = null;
		}
		CoordFour coord = SANtoCoord(sancoord);
		if (temporalCoord != null) {
			coord.add(temporalToCoord(halfmove.substring(halfmove.indexOf('('), halfmove.indexOf(')') + 1)));
			// String layerstr = halfmove.substring(halfmove.indexOf('(') + 1,
			// halfmove.indexOf('T'));
			// String timestr = halfmove.substring(halfmove.indexOf('T') + 1,
			// halfmove.indexOf(')'));
			// coord.L = Integer.parseInt(layerstr);
			// coord.T = Integer.parseInt(timestr);
		} else { // Not Sure but this kinda sucks lol.
			coord.L = 0;
			coord.T = -1;// Indicate no give Time.
		}
		return coord;
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

	// Recieve 2 strings each of one coordinate style string.
	public static Move stringToMove(String coord1, String coord2) {
		CoordFour c1 = stringtoCoord(coord1);
		CoordFour c2 = stringtoCoord(coord2);
		return new Move(c1, c2);
	}

	// Recieve a string form (<L>T<T>) return CoordFour
	public static CoordFour temporalToCoord(String temporal) {
		int T = Integer.parseInt(temporal.substring(temporal.indexOf("T") + 1, temporal.indexOf(")")));
		int L = Integer.parseInt(temporal.substring(1, temporal.indexOf("T")));
		return new CoordFour(0, 0, T, L);
	}

	// Recieve a String such as a8, assumes the san is correct
	public static CoordFour SANtoCoord(String san) {
		char file = san.charAt(0);
		String rank = san.substring(1);
		return new CoordFour((int) file - 97, Integer.parseInt(rank) - 1, 0, 0);
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

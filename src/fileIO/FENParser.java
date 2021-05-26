package fileIO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
		String color = null;
		//String Puzzle; used for mate in x puzzles, which could be a thing but never was....
		ArrayList<String> fenBoards = new ArrayList<String>();
		ArrayList<String> moves = new ArrayList<String>();
		boolean evenStarters = false;
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
				if(line.contains("color")) {
					color = line;
				}
				if (line.contains(":") && !line.contains("\"")) {
					fenBoards.add(line);
					if(line.contains("-0")) {
						evenStarters = true;
					}
				}
			} else {
				moves.add(line);
			}
		}
		// Parse Headers
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
					starters[count++] = getTimelineFromString(FEN, width, height, evenStarters);
				}
				Arrays.sort(starters);
				gsm = new GameStateManager(starters, width, height, evenStarters, true, starters[0].layer, null);
			}
			// Detect other boards / variants by string.
			else if (boardChosen.equalsIgnoreCase("standard")) {
				starters = new Timeline[1];
				starters[0] = getTimelineFromString(STDBOARDFEN, 8, 8, false);
				gsm = new GameStateManager(starters, 8, 8, false, true, starters[0].layer, null);
			} else if (boardChosen.equalsIgnoreCase("standard-princess")) {
				starters = new Timeline[1];
				starters[0] = getTimelineFromString(STD_PRINCESS_BOARDFEN, 8, 8, false);
				gsm = new GameStateManager(starters, 8, 8, false, true, starters[0].layer, null);
			}
			// XXX add more variants, or a better variant loader in the future.(Such as
			// loading from some sort of dictionary).
		} else {
			return null;
		}
		if(color != null) {
			gsm.color = color.contains("white");			
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
			gsm.makeTurn(stringToTurn(gsm, strturn, evenStarters));
		}
		return gsm;
	}

	public static Timeline getTimelineFromString(String board, int width, int height, boolean evenStarters) {
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
				parseLayer(fields[1],evenStarters));
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

	public static Turn stringToTurn(GameState g, String turnstr, boolean evenStarters) {
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
			//Ignores tokens meant for readers, not parsers.
			if (shouldIgnoreToken(token)) {
				moves[i] = null;
				continue;
			}
			// handle special cases
			if (isSpecialMove(token)) {
				if (token.contains("O-O")) {
					moves[i] = findCastleMove(g, token, evenStarters);
				}
				else if (token.contains("=")) {
					int promotion = Board.pieceCharToInt(token.charAt(token.length() - 1));
					if(!g.color) {
						promotion += Board.numTypes;
					}
					moves[i] = getShadMove(g, token.substring(0, token.length() - 2), evenStarters);
					moves[i].specialType = promotion;
					
				}
				else if(token.contains("0000")){
					CoordFour boardOrigin = new CoordFour(0,0,0,0);
					if(token.contains("(")) {
						boardOrigin.add(temporalToCoord(token.substring(token.indexOf('('), token.indexOf(')') + 1), evenStarters));
					}else {
						boardOrigin.T = g.getTimeline(0).Tend;
					}
					moves[i] = new Move(boardOrigin);
				}
			} else {
				moves[i] = getShadMove(g, token, evenStarters);
			}
		}
		return new Turn(moves);
	}

	// Find Castling move XXX finalize this with notation changess
	// TODO parsing castling needs to change, ie. it should also detect Kg1 as O-O
	private static Move findCastleMove(GameState g, String token, boolean evenStarters) {
		boolean side = true;
		CoordFive temporalOrigin;
		if(token.contains("O-O-O")) {
			side = false;
		}
		else if(token.contains("O-O")) {
			side = true;
		}
		if(token.contains("(")) {
			temporalOrigin = new CoordFive(temporalToCoord(token.substring(token.indexOf('('), token.indexOf(')')+1),evenStarters) , g.color);
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
		if (string.contains("O-O") || string.contains("=") || string.contains("0000")) {
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
	public static Move getShadMove(GameState g, String move, boolean evenStarters) {
		Move temp;// Fit this to +-0 later (increment posative and make it so +0 is 1
		//Replace Characters we dont need to look at, uses regex.
		move = move.replaceAll("[~!#]", "");
		if (move.contains("(") && move.indexOf("(") != move.lastIndexOf("(")) {
			return fullStringToCoord(move, evenStarters);
		}
		return ambiguousStringToMove(g, move, evenStarters);
	}

	// This takes a full move (Such as a branching or jumping move, and turns it
	// into a move
	public static Move fullStringToCoord(String move, boolean evenStarters) {
		String coord1;
		if (move.contains(">")) {
			coord1 = move.substring(0, move.indexOf('>'));
		} else {
			coord1 = move.substring(0, move.lastIndexOf('('));
		}
		String coord2 = move.substring(move.lastIndexOf('('));
		return new Move(halfStringToCoord(coord1, evenStarters), halfStringToCoord(coord2, evenStarters));
	}

	// This is for when the move is ambiguous, such as a Nf3 -> need the gamestate
	// to firgure out the source.
	public static Move ambiguousStringToMove(GameState g, String move, boolean evenStarters) {
		CoordFive dest = new CoordFive(halfStringToCoord(move, evenStarters), g.color);
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

	// Recieve a half move (<L>T<T>)(?PIECE)(?AMBIGUITY)(SAN). gives ambiguity info
	public static CoordFour getAmbiguityInfo(String move) {
		// make sure to get axb5 --> as the right thing
		CoordFour temp = new CoordFour(-1, -1, -1, -1);
		move = move.trim();
		int index = 0;
		int rindex = move.length();
		// Trim temporal
		if (move.contains(")")) {
			index = move.indexOf(")") + 1;
		}
		// Trim possible Piece.
		if(move.charAt(index) > 'A' && move.charAt(index) < 'a') {
			index++;
		}
		// trim takes symbol
		if(move.charAt(index) == 'x') {
			index++;
		}
		// trim san
		if (rindex - index >= 3 && move.charAt(rindex - 3) == 'x') {
			rindex -= 3;
		} else {
			rindex -= 2;
		}
		move = move.substring(index, rindex);
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
	}

	// Recieve a string in format (<L>T<T>)(?PIECE)(SAN) ; or (?PIECE)(SAN)
	public static CoordFour halfStringToCoord(String halfmove, boolean evenStarters) {
		// This next line should hold true, but may be wrong(hopefully not, im not
		// planning on supporting boards of 9 or greater length
		// not sure if exits (0T1)Na1x(0T1)c3 <-- must account for 'x' "takes" symbol.
		String sancoord;
		if (halfmove.charAt(halfmove.length() - 1) == 'x') {
			sancoord = halfmove.substring(halfmove.length() - 3, halfmove.length() - 1);
		} else {
			sancoord = halfmove.substring(halfmove.length() - 2);
		}
		CoordFour coord = SANtoCoord(sancoord);
		if (halfmove.contains("(")) {
			coord.add(temporalToCoord(halfmove.substring(halfmove.indexOf('('), halfmove.indexOf(')') + 1), evenStarters));
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
	public static CoordFour temporalToCoord(String temporal, boolean evenStarters) {
		int T = Integer.parseInt(temporal.substring(temporal.indexOf("T") + 1, temporal.indexOf(")")));
		int L = parseLayer(temporal.substring(1, temporal.indexOf("T")), evenStarters);
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
	
	public static int parseLayer(String layer, boolean evenStarters) {
		if(!evenStarters) {
			return Integer.parseInt(layer);
		}
		if(layer.contains("-0")) {
			return 0;
		}
		else if(layer.contains("+0")){
			return 1;
		}
		int layerint = Integer.parseInt(layer);
		if(layerint >= 1) {
			layerint++;
		}
		return layerint;
		
	}

}

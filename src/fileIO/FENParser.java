package fileIO;

import engine.Board;

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
		//get the height and width
		int height = Integer.parseInt(splitString[0]);
		int width = Integer.parseInt(splitString[1]);
		Board b = new Board(height, width);
		//gets the raw pieces, @todo add checks for proper FEN
		String[] rows = splitString[2].split("/");
		int row = 0;
		int col = 0;
		for (String s : rows) {
			for (char c : s.toCharArray()) {
				if (c >= 'A') {
					b.brd[row][col] = indexOfElement(Board.pieceChars, c);
					col++;
				} else if (c <= '8' && c >= '1') {
					for (int i = 0; i < (int) (c) - 48; i++) {
						b.brd[row][col] = indexOfElement(Board.pieceChars, '_');
						col++;
					}
				} else {
					System.out.println("There was an error");
					col++;
				}
			}
			row++;
			col = 0;
		}
		if(splitString[3].charAt(0) == 'w') {
			b.color = Board.pieceColor.BLACK.ordinal();
		}
		if(splitString[3].charAt(0) == 'w') {
			b.color = Board.pieceColor.BLACK.ordinal();
		}
		return b;
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

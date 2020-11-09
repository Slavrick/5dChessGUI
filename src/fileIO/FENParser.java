package fileIO;

import engine.Board;

public class FENParser {
	private String FileInput;
	
	
	
	public static Board getBoardFromString(String FENFormat) {
		String[] splitString = FENFormat.split(";");
		for(String s : splitString) {
			System.out.println(s);
		}
		
		int height = Integer.parseInt(splitString[0]);
		int width = Integer.parseInt(splitString[1]);
		Board b = new Board(height,width);
		String[] rows = splitString[2].split("/");
		int row = 0;
		int col = 0;
		for(String s: rows) {
			for(char c: s.toCharArray()) {
				if(c >= 'A') {
					b.brd[row][col] = indexOfElement(Board.pieceChars,c);
					col++;
				}
				else if(c <= '8' && c >= '1') {
					for(int i = 0; i < (int) (c) - 48; i++ ) {
						b.brd[row][col] = indexOfElement(Board.pieceChars,'_');
						col++;
					}
				}
				else {
					System.out.println("There was an error");
					col++;
				}
			}
			row++;
			col = 0;
		}
		System.out.println(b);
		return null;
	}
	
	public static int indexOfElement(int[] arr , int target) {
		for(int i = 0; i < arr.length; i++) {
			if(arr[i] == target) {
				return i;
			}
		}
		return 0;
	}
	
	public static int indexOfElement(char[] arr , char target) {
		for(int i = 0; i < arr.length; i++) {
			if(arr[i] == target) {
				return i;
			}
		}
		return 0;
	}
}

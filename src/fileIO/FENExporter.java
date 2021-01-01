package fileIO;

import engine.Board;
import engine.GameState;
import engine.GameStateManager;
import engine.Move;
import engine.Turn;

public class FENExporter {
	public static String GameStateToFEN(GameStateManager game) {
		String header = "";
		header +=  game.width + ";" + game.height + ";" + game.tlHandicap + ";" + game.originBoards.length + ";" + game.startminTL + ";";
		if(game.color) {
			header += 'w';
		}else {
			header += 'b';
		}
		String origins = "";
		for( Board b : game.originBoards ) {
			origins += BoardToString(b, false, 0);//TODO figure out how to get start time into this better.
			origins += '\n';
		}
		String moves = "";
		for(Turn t: game.turns) {
			for(Move m : t.getMoves()) {
				moves += m.rawMoveNotation() + ";";
			}
		}
		String FEN = header + '\n' + origins + moves; 
		return FEN;
	}
	
	public static String BoardToString(Board b, boolean color, int tStart) {
		String FEN = "";
		int count = 0;
		for(int y = 0; y < b.height ; y++) {
			for(int x = 0; x < b.width ; x++) {
				int piece = b.getSquare(x,b.height - y - 1);
				if(piece == 0) {
					count++;
				}else {
					if(count > 0) {
						FEN += count;
						count = 0;
					}
					FEN += Board.pieceChars[piece];
				}
			}
			if(count > 0) {
				FEN += count;
				count = 0;
			}
			if(y < b.height - 1) {				
				FEN += "/";
			}
		}
		FEN += ";";
		if(b.wkingSideCastle) {
			FEN += "K";
		}
		if(b.wqueenSideCastle) {
			FEN += "Q";
		}
		if(b.bkingSideCastle) {
			FEN += "k";
		}
		if(b.bqueenSideCastle) {
			FEN += "q";
		}
		FEN += ";";
		if(b.enPassentSquare == null) {
			FEN += "-";
		}
		else {
			FEN += b.enPassentSquare.SANString();
		}
		FEN += ";";
		if(color) {
			FEN += 'w';
		}else {
			FEN += 'b';
		}
		FEN += tStart;
		
		return FEN;
	}
	
}

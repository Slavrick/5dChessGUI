package fileIO;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import engine.Board;
import engine.GameState;
import engine.GameStateManager;
import engine.Move;
import engine.Timeline;
import engine.Turn;

public class FENExporter {
	
	public static void exportString(File saveFile, String export) {
		try {
		      FileWriter myWriter = new FileWriter(saveFile);
		      myWriter.write(export);
		      myWriter.close();
		      //System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
		      System.out.println("An error occurred while saving file.");
		      e.printStackTrace();
		}
	}
	
	public static String GameStateToFEN(GameStateManager game) {
		String header = "";
		header +=  game.width + ";" + game.height + ";" + game.tlHandicap + ";" + game.originsTL.length + ";" + game.startminTL + ";";
		if(game.color) {
			header += 'w';
		}else {
			header += 'b';
		}
		String origins = "";
		for( Timeline t : game.originsTL) {
			if(t.colorStart) {
				origins += BoardToString(t.wboards.get(0), t.colorStart, t.Tstart);				
			}else {
				origins += BoardToString(t.bboards.get(0), t.colorStart, t.Tstart);		
			}
			origins += '\n';
		}
		String moves = "";
		for(Move m: game.preMoves) {
			moves += m.rawMoveNotation() + ";";
		}
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
					if(piece < 0) {
						FEN += Board.pieceChars[piece * -1] + '*';
						
					}else {
						FEN += Board.pieceChars[piece];						
					}
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

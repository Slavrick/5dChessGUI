package fileIO;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import engine.Board;
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
		String header = getGameStateHeader(game);
		String origins = "";
		for( Timeline t : game.originsTL) {
			if(t.colorStart) {
				origins += "[" + BoardToString(t.wboards.get(0), t.colorStart, t.Tstart, t.layer) + "]";				
			}else {
				origins += "[" + BoardToString(t.bboards.get(0), t.colorStart, t.Tstart, t.layer) + "]";		
			}
			origins += '\n';
		}
		String moves = "";
		boolean oddTurn = true;
		for(Turn t: game.turns) {
			Turn.mode = Turn.notationMode.SHADRAW;
			if(oddTurn) {
				Turn.pre = Turn.prefixMode.TURN;
			}else {
				Turn.pre = Turn.prefixMode.NONE;	
			}
			moves += t.toString();
			if(oddTurn) {
				moves += " / ";
			}else {
				moves += " \n";
			}
			oddTurn = !oddTurn;
		}
		Turn.mode = Turn.notationMode.SHAD;
		Turn.pre = Turn.prefixMode.TURN;
		String FEN = header + '\n' + origins + '\n' +  moves; 
		return FEN;
	}
	
	private static String BoardToString(Board b, boolean color, int tStart, int layer) {
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
						FEN += Board.pieceChars[piece * -1] + "*";
						
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
		FEN += ":" + layer + ":" + tStart + ":";
		if(color) {
			FEN += 'w';
		}else {
			FEN += 'b';
		}
		return FEN;
	}
	
	private static String getGameStateHeader(GameStateManager gsm) {
		String headers = "";
		headers += "[board \"custom\"]\n";
		headers += "[size \"" + gsm.width + "x" + gsm.height + "\"]";	
		return headers;
	}
	
}

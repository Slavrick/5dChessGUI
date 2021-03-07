package engine;

import java.util.ArrayList;

import GUI.MessageEvent;

public class GameStateManager extends GameState{
	
	public Move[] preMoves;
	public Timeline[] originsTL;
	public int startminTL;
	public int turnNum;

	//TODO make this into a tree, that way we can have diff lines.
	public ArrayList<Turn> turns;
	public int currTurn;
	
	
	public GameStateManager(Timeline[] origins, int width, int height, boolean evenStart, boolean color, int minTL, Move[] moves) {
		super(origins, width, height, evenStart, color, minTL, moves);
		this.preMoves = moves;
		originsTL = origins;
		this.turns = new ArrayList<Turn>();
		turnNum = 1;
	}
	
	public boolean submitMoves() {
		determineActiveTLS();
		boolean presColor = calcPresent();
		if(!opponentCanCaptureKing() && !(presColor == color)) {
			turns.add(new Turn( turnMoves, turnTLs));
			turns.get(turns.size()-1).turnNum = turnNum;
			currTurn++;
			turnTLs.clear();
			turnMoves.clear();
			turnNum = !color ? turnNum + 1 : turnNum; 
			color =! color;
			startPresent = present;
			return true;
		}
		if(GUI.Globals.es != null) {
			if(presColor == color) {
				MessageEvent m = new MessageEvent("The present Still rests on your color.");
				GUI.Globals.es.broadcastEvent(m);
			}else {
				MessageEvent m = new MessageEvent("Submitting now Would Allow your opponent to capture your king.");
				GUI.Globals.es.broadcastEvent(m);
			}			
		}
		return false;
	}
	
	//Set the gamestate to a previous or future turn depending on the int given.
	public boolean setTurn(int turnNum) {//TODO test this
		if(turnNum < 0 || turnNum > turns.size()) {
			return false;
		}
		if(currTurn == turnNum) {
			return true;
		}
		if(turnNum > currTurn) {
			while(currTurn < turnNum) {
				this.makeTurn(turns.get(currTurn).moves);
				currTurn++;
			}
		}else {
			while(currTurn > turnNum) {
				undoTurn(turns.get(currTurn).tls);
				currTurn--;
			}
		}
		return true;
	}

	public boolean makeTurn(Turn turn) {
		if(turn == null) {
			return false;
		}
		for(Move m : turn.moves) {
			if(!this.makeMove(m)) {
				return false;
			}
		}
		this.submitMoves();
		return true;
	}
}

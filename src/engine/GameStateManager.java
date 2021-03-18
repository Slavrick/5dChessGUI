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
		currTurn = -1;
	}
	
	public boolean submitMoves() {
		determineActiveTLS();
		boolean presColor = calcPresent();
		if(!opponentCanCaptureKing() && !(presColor == color)) {
			if(currTurn + 1 < turns.size()) {
				clearFutureTurns();
			}
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
	public boolean setTurn(int targetTurn) {
		undoTempMoves();
		if(targetTurn < -1 || targetTurn > turns.size()) {
			return false;
		}
		if(currTurn == targetTurn) {
			return true;
		}
		if(targetTurn > currTurn) {
			while(currTurn < targetTurn) {
				incrementTurn(turns.get(currTurn+1));
				turnNum++;
			}
		}else {
			while(currTurn > targetTurn) {
				undoTurn(turns.get(currTurn).tls);
				currTurn--;
				turnNum--;
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
	
	//MUST BE USED ONLY INCREMENTALLY
	private boolean incrementTurn(Turn t) {
		currTurn++;
		for(Move m : t.moves) {
			this.makeSilentMove(m);
		}
		determineActiveTLS();
		color = !color;
		startPresent = present;
		return true;
	}
	
	private void clearFutureTurns() {
		for(int i = turns.size() - 1 ; i > currTurn ; i--) {
			turns.remove(i);			
		}
	}
}

package engine;

import java.util.ArrayList;

public class GameStateManager extends GameState{
	
	public Move[] preMoves;
	public Timeline[] originsTL;
	public int startminTL;

	//TODO make this into a tree, that way we can have diff lines.
	public ArrayList<Turn> turns;
	public int currTurn;
	
	
	public GameStateManager(Timeline[] origins, int width, int height, boolean evenStart, boolean color, int minTL, Move[] moves) {
		super(origins, width, height, evenStart, color, minTL, moves);
		this.preMoves = moves;
		originsTL = origins;
		this.turns = new ArrayList<Turn>();
	}
	
	public boolean submitMoves() {
		determineActiveTLS();
		boolean presColor = calcPresent();
		if(!opponentCanCaptureKing() && !(presColor == color)) {
			turns.add(new Turn( turnMoves, turnTLs));
			currTurn++;
			turnTLs.clear();
			turnMoves.clear();
			color =! color;
			startPresent = present;
			return true;
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
}

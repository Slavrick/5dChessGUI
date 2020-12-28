package engine;

import java.util.ArrayList;

public class GameStateManager extends GameState{
	
	//TODO make this more good so it can be exportable etc.
	public Move[] preMoves;
	public Board originBoards[];
	public int startminTL;

	//TODO make this into a tree, that way we can have diff lines.
	public ArrayList<Turn> turns;
	public int currTurn;
	
	
	public GameStateManager(Timeline[] origins, int width, int height, boolean evenStart, boolean color, int minTL, Move[] moves) {
		super(origins, width, height, evenStart, color, minTL, moves);
		this.preMoves = moves;
		for(int i = 0; i < origins.length ; i++){ // TODO this aint gonna work.
			originBoards[i] = origins[i].getPlayableBoard();
		}
	}
	
	public GameStateManager(GameState game) {//TODO fix this
		super(game);
		this.turns = new ArrayList<Turn>();
		currTurn = 1; 
	}
	
	public boolean submitMoves() {
		determineActiveTLS();
		boolean presColor = calcPresent();
		if(!opponentCanCaptureKing() && !(presColor == color)) {
			turns.add(new Turn( turnMoves, turnTLs));
			turnTLs.clear();
			turnMoves.clear();
			color =! color;
			startPresent = present;
			return true;
		}
		return false;
	}
	
	public boolean setTurn(int turnNum) {//TODO finish this func
		return false;
	}
}

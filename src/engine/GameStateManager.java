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
		for(int i = 0; i < origins.length ; i++){
			if(origins[i].colorStart) {
				originBoards[i] = origins[i].wboards.get(0);
			}else {
				originBoards[i] = origins[i].bboards.get(0);
			}
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
	
	//Set the gamestate to a previous or future turn depending on the int given.
	public boolean setTurn(int turnNum) {//TODO finish this func
		
		return false;
	}
}

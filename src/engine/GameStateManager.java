package engine;

import java.util.ArrayList;

public class GameStateManager extends GameState{
	
	public ArrayList<Turn> turns;
	public int currTurn;
	
	public GameStateManager(GameState game) {
		super(game);
		this.turns = new ArrayList<Turn>();
		currTurn = 1; 
	}
	
	//Overriden function
		public boolean submitMoves() {
			determineActiveTLS();
			boolean presColor = calcPresent();
			if(!isInCheck() && !(present == startPresent && presColor == color)) {
				turnTLs.clear();
				turnMoves.clear();
				color =! color;
				startPresent = present;
				return true;
			}
			return false;
		}
	
}

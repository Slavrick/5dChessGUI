package engine;

import java.util.ArrayList;

public class GameStateManager {
	
	public GameState game;
	public ArrayList<Turn> turns;
	public ArrayList<Move> turnMoves;
	public ArrayList<Integer> turnTls;
	public int currTurn;
	
	public GameStateManager(GameState game) {
		this.turns = new ArrayList<Turn>();
		currTurn = 0; 
		this.game = game;
	}
	
	
}

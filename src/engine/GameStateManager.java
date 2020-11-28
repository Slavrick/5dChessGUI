package engine;

import java.util.ArrayList;

public class GameStateManager {
	
	public GameState game;
	public ArrayList<Move> gameMoves;
	
	
	public GameStateManager(GameState game) {
		this.game = game;
	}
}

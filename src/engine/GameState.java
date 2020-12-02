package engine;

import java.util.ArrayList;

public class GameState {
	public static final boolean WHITE = true;
	public static final boolean BLACK = false;

	public ArrayList<Timeline> multiverse;
	public boolean color;
	public int present;
	public int width;
	public int height;
	public int maxTL;
	public int minTL;
	public int minActiveTL;
	public int maxActiveTL;
	
	//This is a planned workaround for 2 timeline +-0 starting positions. We may start with 0,1 tl, but white can still branch
	//in other words, a posative value means that white can make n more timelines than black, and the opposite for negative numbers
	//therefore in the above example, you would have 2 timeline start. starters would be 0,1 and handicap would be 1.
	//without this, the +2 TL would be inactive.
	public int tlHandicap;

	public GameState(Board start) {
		multiverse = new ArrayList<Timeline>();
		multiverse.add(new Timeline(start, true, 1, 0));
		minTL = 0;
		maxTL = 0;
		tlHandicap = 0;
		this.width = start.width;
		this.height = start.height;
	}

	public GameState(Timeline starter) {
		multiverse = new ArrayList<Timeline>();
		multiverse.add(starter);
		minTL = 0;
		maxTL = 0;
		tlHandicap = 0;
		color = true;
		Board b = starter.getPlayableBoard();
		this.width = b.width;
		this.height = b.height;
	}

	public GameState(Timeline[] starters, int minTL, int maxTL) {
		Board b = starters[0].getPlayableBoard();
		this.width = b.width;
		this.height = b.height;
		multiverse = new ArrayList<Timeline>();
		for (Timeline t : starters)
			multiverse.add(t);
		this.minTL = minTL;
		this.maxTL = maxTL;
		tlHandicap = 0;
		color = true;
	}

	public GameState(ArrayList<Timeline> starters, int minTL, int maxTL) {
		Board b = starters.get(0).getPlayableBoard();
		this.width = b.width;
		this.height = b.height;
		multiverse = starters;
		this.minTL = minTL;
		this.maxTL = maxTL;
		tlHandicap = 0;
		color = true;
	}
	
	public GameState(ArrayList<Timeline> starters, int minTL, int maxTL, int handicap) {
		Board b = starters.get(0).getPlayableBoard();
		this.width = b.width;
		this.height = b.height;
		multiverse = starters;
		this.minTL = minTL;
		this.maxTL = maxTL;
		tlHandicap = handicap;
		color = true;
	}
	
	public boolean makeTurn(Move move) {
		Move[] moves = { move };
		return makeTurn(moves);
	}

	public boolean makeTurn(Move[] moves) {
		for (Move m : moves) {
			if (m.type == 1) { // if the move is spatial
				getTimeline(m.origin.L).addSpatialMove(m, color);
			} else {
				Timeline tlOrigin = getTimeline(m.origin.L);
				Timeline tldest = getTimeline(m.dest.L);
				int pieceMoved = tlOrigin.addJumpingMove(m.origin, color);
				Board b = tldest.addJumpingMoveDest(m.dest, color, pieceMoved);
				if (b != null) { // means that the move branches.
					if (color) {
						maxTL++;
						Timeline branch = new Timeline(b, !color, m.dest.T, maxTL);
						branch.active = (maxTL <= ((minTL * -1) + 1 + tlHandicap));
						multiverse.add(branch);
						determineActiveTimelines();
					} else {
						minTL--;
						Timeline branch = new Timeline(b, !color, m.dest.T + 1, minTL);
						branch.active = ((-1 * minTL) <= (maxTL + 1 + tlHandicap));
						multiverse.add(0, branch);

					}
				}
			}
		}
		color = !color;
		return true;
	}

	private boolean validateTurn(Move[] turn, boolean nextPlayer){
		for(int i = minActiveTL - minTL; i < maxActiveTL; i++) {
			if(multiverse.get(i).colorPlayable == color && multiverse.get(i).Tend <= present) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isInBounds(CoordFour c, boolean boardColor) {
		if (c.L > maxTL || c.L < minTL) {
			return false;
		}
		if (!multiverse.get(c.L + (-1 * minTL)).timeExists(c.T, boardColor)) {
			return false;
		}
		if (c.x < 0 || c.x >= width)
			return false;
		if (c.y < 0 || c.y >= height)
			return false;
		return true;
	}

	public boolean layerExists(int layer) {
		return (layer >= minTL && layer <= maxTL);
	}
	
	public Timeline getTimeline(int layer) {
		if(!layerExists(layer)) {
			return null;
		}
		return multiverse.get(layer + (-1 * minTL));
	}

	public Board getBoard(CoordFour c, boolean boardColor) {
		if(!layerExists(c.L)) {
			return null;
		}
		return multiverse.get(c.L + (-1 * minTL)).getBoard(c.T, boardColor);
	}

	public void determineActiveTimelines() {

	}

	public void printMultiverse() {
		System.out.println(color);
		int tl = minTL;
		for (Timeline t : multiverse) {
			System.out.println("----------------------TL" + tl + "----------------------");
			t.printTimleline();
			tl++;
		}
	}
	
	/**
	 * Determines whether the current gamestate is mate, with the given color
	 * 
	 * @param color the color of the defending player.
	 * 
	 * @return returns boolean, true if the gamestate is mate
	 */
	public boolean isMate(boolean color) {
		return false;
	}
	
	public boolean isLegalState(boolean color) {
		for(int i = minTL; i < maxTL; i++) {
			
		}
		return false;
	}
	
	/**
	 * undo a move, based on what timelines were effected on that move.
	 * 
	 * @param tlmoved an int array of timelines to undo.
	 * @return false
	 */
	public boolean undoMove(int[] tlmoved) {
		for(int tl : tlmoved) {
			Timeline t = multiverse.get(getTLIndex(tl, minTL));
			if(t.undoMove()) {
				multiverse.remove(getTLIndex(tl,minTL));
			}
		}
		return false;
	}
	
	public static int getTLIndex(int layer, int minTL) {
		return layer + (-1 * minTL);
	}
	
	
	/**
	 * 
	 * @param c coordinate of square
	 * @param color color of square
	 * @return int relating to piece color
	 */
	public int getSquare(CoordFour c, boolean color) {
		if(layerExists(c.L))
			return getTimeline(c.L).getSquare(c, color);
		return -1;
	}
}

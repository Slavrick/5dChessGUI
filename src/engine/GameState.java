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

	private ArrayList<Move> turnMoves;
	private ArrayList<Integer> turnTLs;

	// This is a planned workaround for 2 timeline +-0 starting positions. We may
	// start with 0,1 tl, but white can still branch
	// in other words, a posative value means that white can make n more timelines
	// than black, and the opposite for negative numbers
	// therefore in the above example, you would have 2 timeline start. starters
	// would be 0,1 and handicap would be 1.
	// without this, the +2 TL would be inactive.
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
		initVals();
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
		initVals();
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
		initVals();
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
		initVals();
		Board b = starters.get(0).getPlayableBoard();
		this.width = b.width;
		this.height = b.height;
		multiverse = starters;
		this.minTL = minTL;
		this.maxTL = maxTL;
		tlHandicap = handicap;
		color = true;
	}

	private void initVals() {
		turnMoves = new ArrayList<Move>();
		turnTLs = new ArrayList<Integer>();
	}

	public boolean makeTurn(Move move) {
		Move[] moves = { move };
		return makeTurn(moves);
	}

	// TODO make this turn better.
	public boolean makeTurn(Move[] moves) {
		for (Move m : moves) {
			if (m.type == 1) { // if the move is spatial
				getTimeline(m.origin.L).addSpatialMove(m, color);
			} else {
				// need to add validation to this TODO add validation
				Timeline tlOrigin = getTimeline(m.origin.L);
				Timeline tldest = getTimeline(m.dest.L);
				int pieceMoved = tlOrigin.addJumpingMove(m.origin, color);
				Board b = tldest.addJumpingMoveDest(m.dest, color, pieceMoved);
				if (b != null) { // means that the move branches.
					if (color) { // TODO remove activeness on TL/ this function.
						maxTL++;
						Timeline branch = new Timeline(b, !color, m.dest.T, maxTL);
						branch.active = (maxTL <= ((minTL * -1) + 1 + tlHandicap));
						multiverse.add(branch);
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

	/**
	 * make a move without submitting turn.
	 * 
	 * @param m move to submit
	 * @return boolean whether the move was made or not
	 */
	public boolean makeMove(Move m) {
		if (m.type == 1) {
			boolean moveResult = getTimeline(m.origin.L).addSpatialMove(m, color);
			if(moveResult) {
				turnTLs.add(m.origin.L);
				turnMoves.add(m);
				return true;
			}
			else {
				return false;
			}
		}
		// TODO validate origin.
		Timeline originT = getTimeline(m.origin.L);
		Timeline destT = getTimeline(m.dest.L);
		if (originT == null || destT == null) {
			return false;
		}
		if (!originT.colorPlayable == this.color) {
			return false;
		}
		int pieceMoved = originT.addJumpingMove(m.origin, color);
		// TODO validate piece
		Board b = destT.addJumpingMoveDest(m.dest, color, pieceMoved);
		if (b == null) { // means no branching
			turnTLs.add(m.origin.L);
			turnTLs.add(m.dest.L);
			turnMoves.add(m);
		} else {
			turnTLs.add(m.origin.L);
			turnTLs.add(this.addTimeline(b, m.dest.T));
			turnMoves.add(m);
		}
		return true;
	}
	// make sure to add the move iff there was a move added, and never if not.

	private boolean validateTurn(Move[] turn, boolean nextPlayer) {
		for (int i = minActiveTL - minTL; i < maxActiveTL; i++) {
			if (multiverse.get(i).colorPlayable == color && multiverse.get(i).Tend <= present) {
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
		if (!layerExists(layer)) {
			return null;
		}
		return multiverse.get(layer + (-1 * minTL));
	}

	public boolean getColor() {
		return color;
	}

	/**
	 * 
	 * @param c     coordinate of square
	 * @param color color of square
	 * @return int relating to piece color
	 */
	public int getSquare(CoordFour c, boolean color) {
		if (layerExists(c.L))
			return getTimeline(c.L).getSquare(c, color);
		return -1;
	}

	
	public Board getBoard(CoordFour c, boolean boardColor) {
		if (!layerExists(c.L)) {
			return null;
		}
		return multiverse.get(c.L + (-1 * minTL)).getBoard(c.T, boardColor);
	}

	/**
	 * add a timeline to the multiverse. This assumes that the branch is the color
	 * of the current players turn
	 * 
	 * @param b
	 * @param timeStart the time of the destination of the branch. ie if you jump to
	 *                  time 1 as black, you would input 1 not 2 (2 is the first
	 *                  playable board for white and when the timeline actually
	 *                  starts)
	 * @return integer of the timeline created.
	 */
	private int addTimeline(Board b, int timeStart) {
		if (color) {
			maxTL++;
			Timeline branch = new Timeline(b, !color, timeStart, maxTL);
			multiverse.add(branch);
			return maxTL;
		} else { // black branches,
			minTL--;
			Timeline branch = new Timeline(b, !color, timeStart + 1, maxTL);
			multiverse.add(0,branch);
			return minTL;
		}
	}

	/**
	 * Prints the entire game state -- not very visually appealing though mainly used for debugging
	 */
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
		for (int i = minTL; i < maxTL; i++) {

		}
		return false;
	}

	/**
	 * undo a turn, based on what timelines were effected on that move, this relies on another structure keeping track of the gamestate changes.
	 * 
	 * @param tlmoved an int array of timelines to undo.
	 * @return false
	 */
	public boolean undoTurn(int[] tlmoved) {
		for (int tl : tlmoved) {
			Timeline t = multiverse.get(getTLIndex(tl, minTL));
			if(t.colorPlayable != color)
				continue;
			if (t.undoMove()) {
				multiverse.remove(getTLIndex(tl, minTL));
				// update min/max tls. TODO fix this
			}
		}
		return false;
	}
	
	public void undoTempMoves() {
		if(turnTLs.size() <= 0)
			return;
		for(int i : turnTLs) {
			if(getTimeline(i).undoMove()) {
				//this means that the timeline had only one board.
				multiverse.remove(GameState.getTLIndex(i, this.minTL));
				if(color) {
					maxTL--;
				}
				else {
					minTL++;
				}
			}
		}
		turnTLs.clear();
	}

	public static int getTLIndex(int layer, int minTL) {
		return layer + (-1 * minTL);
	}

	// TODO add handicap to this.
	/**
	 * this function changes the object to reflect which TL are 'active'
	 */
	private void determineActiveTLS() {
		// case 1 -- black has branched more.
		if (maxTL < Math.abs(minTL)) {
			maxActiveTL = maxTL;
			minActiveTL = -1 - maxActiveTL;
		}
		// case 2 -- white has branched more.
		else if (Math.abs(minTL) < maxTL) {
			minActiveTL = minTL;
			maxActiveTL = 1 + Math.abs(minActiveTL);
		}
		// case 3 -- they have branched the same # of times
		else {
			minActiveTL = minTL;
			maxActiveTL = maxTL;
		}

	}

	private boolean calcPresent() {
		int presentTime = getTimeline(minActiveTL).Tend;
		boolean presentColor = getTimeline(minActiveTL).colorPlayable;
		for (int i = minActiveTL; i < maxActiveTL; i++) {
			Timeline t = getTimeline(minActiveTL);
			if (t.Tend < presentTime) {
				presentTime = t.Tend;
				presentColor = t.colorPlayable;
			}
			if (t.Tend == presentTime && !presentColor && t.colorPlayable == GameState.WHITE) {
				presentTime = t.Tend;
				presentColor = t.colorPlayable;
			}
		}
		this.present = presentTime;
		return presentColor;
	}

	public boolean coordIsPlayable(CoordFive c) {
		return getTimeline(c.L).isMostRecentTime(c.T, c.color);
	}

	
}

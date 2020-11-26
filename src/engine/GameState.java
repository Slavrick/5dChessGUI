package engine;

import java.util.ArrayList;

public class GameState {
	public static final boolean WHITE = true;
	public static final boolean BLACK = true;

	public ArrayList<Timeline> multiverse;
	public boolean color;
	public int present;
	public int width;
	public int height;
	public int maxTL;
	public int minTL;
	public int minActiveTL;
	public int maxActiveTL;
	public int tlHandicap;

	public GameState(Board start) {
		multiverse = new ArrayList<Timeline>();
		multiverse.add(new Timeline(start, true, 1, 0));
		minTL = 0;
		maxTL = 0;
		tlHandicap = 0;
	}

	public GameState(Timeline starter) {
		multiverse = new ArrayList<Timeline>();
		multiverse.add(starter);
		minTL = 0;
		maxTL = 0;
		tlHandicap = 0;
		color = true;
	}

	public GameState(Timeline[] starters) {
		multiverse = new ArrayList<Timeline>();
		for (Timeline t : starters)
			multiverse.add(t);
		minTL = 0;
		maxTL = 0;
		tlHandicap = 0;
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
				Timeline tldest = getTimeline(m.origin.L);
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
	
	public boolean isInBounds(CoordFive c, boolean boardColor) {
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

	public Timeline getTimeline(int layer) {
		return multiverse.get(layer + (-1 * minTL));
	}

	public Board getBoard(CoordFive c, boolean boardColor) {
		if(c.L < minTL || c.L > maxTL) {
			return null;
		}
		return multiverse.get(c.L + (-1 * minTL)).getBoard(c.T, boardColor);
	}

	public void determineActiveTimelines() {

	}

	public void printMultiverse() {
		int tl = minTL;
		for (Timeline t : multiverse) {
			System.out.println("----------------------TL" + tl + "----------------------");
			t.printTimleline();
			tl++;
		}
	}
	
	public boolean isMate(boolean color) {
		return false;
	}
	
	public boolean isLegalState(boolean color) {
		for(Timeline t : multiverse) {
			
		}
		return false;
	}
	
	public boolean undoMove() {
		return false;
	}
}

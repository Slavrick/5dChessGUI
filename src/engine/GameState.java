package engine;

import java.util.ArrayList;
import GUI.MessageEvent;

public class GameState {
	public static final boolean WHITE = true;
	public static final boolean BLACK = false;

	public ArrayList<Timeline> multiverse;
	public boolean evenStart;
	public boolean color;
	public int startPresent;
	public int present;
	public int width;
	public int height;
	public int maxTL;
	public int minTL;
	public int minActiveTL;
	public int maxActiveTL;

	protected ArrayList<Move> turnMoves;
	protected ArrayList<Integer> turnTLs;

	// This is a planned workaround for 2 timeline +-0 starting positions. We may
	// start with 0,1 tl, but white can still branch
	// in other words, a posative value means that white can make n more timelines
	// than black, and the opposite for negative numbers
	// therefore in the above example, you would have 2 timeline start. starters
	// would be 0,1 and handicap would be 1.
	// without this, the +2 TL would be inactive.
	// we need to take into consideration parsing of coords
	public int tlHandicap;

	// Copies, but not in the best way, the end results in 2 which point to the same
	// arrays. But it will work for certain purposes, ie. for a subclass.
	public GameState(GameState g) {
		this.multiverse = g.multiverse;
		this.evenStart = g.evenStart;
		this.color = g.color;
		this.startPresent = g.startPresent;
		this.present = g.present;
		this.width = g.width;
		this.height = g.height;
		this.minTL = g.minTL;
		this.maxTL = g.maxTL;
		this.minActiveTL = g.minActiveTL;
		this.maxActiveTL = g.maxActiveTL;
		this.turnMoves = g.turnMoves;
		this.turnTLs = g.turnTLs;
		this.tlHandicap = g.tlHandicap;
	}

	public GameState(Board start) {
		multiverse = new ArrayList<Timeline>();
		multiverse.add(new Timeline(start, true, 1, 0));
		minTL = 0;
		maxTL = 0;
		evenStart = false;
		tlHandicap = 0;
		this.width = start.width;
		this.height = start.height;
		initVals();
	}

	public GameState(Timeline starter) {
		multiverse = new ArrayList<Timeline>();
		multiverse.add(starter);
		minTL = 0;
		maxTL = 0;
		evenStart = false;
		tlHandicap = 0;
		color = true;
		Board b = starter.getPlayableBoard();
		this.width = b.width;
		this.height = b.height;
		initVals();
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
		initVals();
	}

	//This is going to be the new Constructor going foreward.
	public GameState(Timeline[] origins, int width, int height, boolean evenStart, boolean color, int minTL) {
		multiverse = new ArrayList<Timeline>();
		this.width = width;
		this.height = height;
		this.minTL = minTL;
		this.maxTL = minTL + origins.length - 1;
		this.color = color;
		this.evenStart = evenStart;
		if (evenStart) {
			tlHandicap = 1;
		} else {
			tlHandicap = 0;
		}
		for (Timeline t : origins) {
			multiverse.add(t);
		}
		initVals();
	}
	
	// This function is used by the FEN which is why it is so beefy.
	public GameState(Timeline[] origins, int width, int height, boolean evenStart, boolean color, int minTL,
			Move[] moves) {
		multiverse = new ArrayList<Timeline>();
		this.width = width;
		this.height = height;
		this.minTL = minTL;
		this.maxTL = minTL + origins.length - 1;
		this.color = color;
		this.evenStart = evenStart;
		if (evenStart) {
			tlHandicap = 1;
		} else {
			tlHandicap = 0;
		}
		for (Timeline t : origins) {
			multiverse.add(t);
		}
		if (moves == null) {
			initVals();
			return;
		}
		for (Move m : moves) {
			Timeline origin = this.getTimeline(m.origin.L);
			boolean moveColor = origin.colorPlayable;
			if (m.type == 1) {
				origin.addSpatialMove(m, origin.colorPlayable);
			} else {
				Timeline dest = this.getTimeline(m.dest.L);
				int piece = origin.addJumpingMove(m, origin.colorPlayable);
				Board branch = dest.addJumpingMoveDest(m.dest, dest.colorPlayable, piece);
				if (branch != null) {
					if (moveColor) {
						multiverse.add(new Timeline(branch, !moveColor, m.dest.T, maxTL));
						maxTL++;
					} else {
						multiverse.add(0, new Timeline(branch, !moveColor, m.dest.T + 1, minTL));
						minTL--;
					}
				}
			}
		}
		initVals();
	}

	private void initVals() {
		turnMoves = new ArrayList<Move>();
		turnTLs = new ArrayList<Integer>();
		determineActiveTLS();
		calcPresent();
		startPresent = present;
	}

	public boolean makeTurn(Move move) {
		Move[] moves = { move };
		return makeTurn(moves);
	}

	public boolean makeTurn(Move[] moves) {
		for (Move m : moves) {
			makeMove(m);
		}
		if (!submitMoves()) {
			undoTempMoves();
			return false;
		}
		color = !color;
		return true;
	}

	/**
	 * make a move without submitting turn, or effecting the temp move data
	 * structures. (In other words, it becomes up to the caller to handle undo's and
	 * other features. This will do no validation as it should only be called for
	 * setting up positions.
	 * 
	 * @param m move to submit
	 * @return boolean whether the move was made or not
	 */
	protected boolean makeSilentMove(Move m) {//TODO promotion doesn't work for this
		if (m.type == Move.SPATIALMOVE) {
			int pieceMoved = this.getSquare(m.origin, this.color);
			pieceMoved = pieceMoved < 0 ? pieceMoved * -1 : pieceMoved;
			if(pieceMoved == Board.piece.WKING.ordinal() || pieceMoved == Board.piece.BKING.ordinal()) {
				if(Math.abs(m.origin.x - m.dest.x) == 2) {
					return this.getTimeline(m.origin.L).castleKing(m);
				}
			}
			boolean moveResult = getTimeline(m.origin.L).addSpatialMove(m, color);
			if (moveResult) {
				return true;
			} else {
				return false;
			}
		}
		Timeline originT = getTimeline(m.origin.L);
		Timeline destT = getTimeline(m.dest.L);
		if (originT == null || destT == null) {
			return false;
		}
		if (!originT.colorPlayable == this.color) {
			return false;
		}
		int pieceMoved = originT.addJumpingMove(m, color);
		Board b = destT.addJumpingMoveDest(m.dest, color, pieceMoved);
		// This part sets the type on the move to convey information to other functions.
		if (b == null) {
			m.type = 2;
		} else {
			this.addTimeline(b, m.dest.T);
			m.type = 3;
		}
		determineActiveTLS();
		return true;
	}

	/**
	 * make a move without submitting turn. This will also validate the
	 * move.(although maybe I should do that elsewhere.
	 * 
	 * @param m move to submit
	 * @return boolean whether the move was made or not
	 */
	public boolean makeMove(Move m) {
		if(m.specialType == Move.NULLMOVE && this.getTimeline(m.origin.L).colorPlayable == this.color) {
			getTimeline(m.origin.L).addSpatialMove(m, color);
			return true;
		}
		if(!validateMove(m)) {
			return false;
		}
		//handle Special cases First
		if(m.specialType != Move.NORMALMOVE) {
			if(m.specialType >= Move.PROMOTION) {
				if(this.promote(m)) {
					turnTLs.add(m.dest.L);
					turnMoves.add(m);
					return true;
				}else {
					return false;
				}
			}
			/*if(m.specialType == Move.CASTLE) {
				getTimeline(m.origin.L).castleKing(m);
				turnTLs.add(m.origin.L);
				turnMoves.add(m);
				return true;
			}*/
		}
		int pieceMoved = this.getSquare(m.origin, this.color);
		pieceMoved = pieceMoved < 0 ? pieceMoved * -1 : pieceMoved;
		// Move the piece.
		if (m.type == Move.SPATIALMOVE) {
			if(pieceMoved == Board.piece.WKING.ordinal() || pieceMoved == Board.piece.BKING.ordinal()) {
				if(Math.abs(m.origin.x - m.dest.x) == 2) {
					turnTLs.add(m.origin.L);
					turnMoves.add(m);
					return this.getTimeline(m.origin.L).castleKing(m);
				}
			}
			boolean moveResult = getTimeline(m.origin.L).addSpatialMove(m, color);
			if (moveResult) {
				turnTLs.add(m.origin.L);
				turnMoves.add(m);
				return true;
			} else { 
				return false;
			}
		}
		Timeline originT = getTimeline(m.origin.L);
		Timeline destT = getTimeline(m.dest.L);
		originT.addJumpingMove(m, color);
		Board b = destT.addJumpingMoveDest(m.dest, color, pieceMoved);
		// This part sets the type on the move to convey information to other functions.
		if (b == null) {
			turnTLs.add(m.origin.L);
			turnTLs.add(m.dest.L);
			turnMoves.add(m);
			m.type = Move.JUMPINGMOVE;
		} else {
			turnTLs.add(m.origin.L);
			turnTLs.add(this.addTimeline(b, m.dest.T));
			turnMoves.add(m);
			m.type = Move.BRANCHINGMOVE;
		}
		determineActiveTLS();
		return true;
	}
	// make sure to add the move iff there was a move added, and never if not.
	
	//this makes a move, without adding to the move buffer
	public boolean promote(Move m) {
		int promotionType = m.specialType;
		if(m.type != Move.SPATIALMOVE || m.specialType < Move.PROMOTION || (m.dest.y != 0 && m.dest.y != (this.height - 1)) || Board.getColorBool(promotionType) != this.color || promotionType == 0) {
			return false;
		}
		return getTimeline(m.origin.L).promote(m);
	}
	
	public boolean submitMoves() {
		boolean presColor = calcPresent();
		if (!opponentCanCaptureKing() && !(presColor == color)) {
			turnTLs.clear();
			turnMoves.clear();
			color = !color;
			startPresent = present;
			return true;
		}
		//Send Message Event
		if(GUI.Globals.es != null) {
			if(presColor == color) {
				MessageEvent m = new MessageEvent("The present Still rests on your color.");
				GUI.Globals.es.broadcastEvent(m);
			}else {
				ArrayList<CoordFour> checkingpiece = MoveGenerator.getAllCheckingPieces(this);
				MessageEvent m = new MessageEvent("Submitting now Would Allow your opponent to capture your king. \n For example, the piece on: " + checkingpiece.get(0) + " can capture your piece");
				GUI.Globals.es.broadcastEvent(m);
			}			
		}
		return false;
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
			multiverse.add(0, branch);
			return minTL;
		}
	}

	// Validates turn without adding the moves to the gameState.
	private boolean validateTurn(Move[] turn) {
		ArrayList<Integer> affectedTimelines = new ArrayList<Integer>();
		for (Move m : turn) {
			if (m == null) {
				continue;
			}
			if (makeSilentMove(m)) {
				if (m.type == 1) { // Spatial
					affectedTimelines.add(m.origin.L);
				} else if (m.type == 2) { // Jumping
					affectedTimelines.add(m.origin.L);
					affectedTimelines.add(m.dest.L);
				} else { // Branching.
					affectedTimelines.add(m.origin.L);
					if (color) {
						affectedTimelines.add(maxTL);
					} else {
						affectedTimelines.add(minTL);
					}
				}
			} else {
				continue;
			}
		}
		boolean result = false;
		if (calcPresent() != color && !opponentCanCaptureKing()) {
			result = true;
		}
		if (this.undoTurn(affectedTimelines)) {
			color = !color; 
			// This is because the last line changes the color of the structure,
			// but it was not changed when the moves were applied, so we need to undo that factor.
			
		}
		return result;
	}

	protected boolean validateMove(Move m) {
		if (!isInBounds(m.origin, this.color) || !isInBounds(m.dest, this.color)) {
			return false;
		}
		Timeline originTL = getTimeline(m.origin.L);
		if(originTL.colorPlayable != this.color) {
			return false;
		}
		int pieceMoved = this.getSquare(m.origin, this.color);
		int movedTo = this.getSquare(m.dest, this.color);
		if(pieceMoved == Board.EMPTYSQUARE || pieceMoved == Board.ERRORSQUARE || this.color != Board.getColorBool(pieceMoved)) {
			return false;
		}
		if (movedTo != Board.EMPTYSQUARE && Board.getColorBool(movedTo) == color && m.specialType == Move.NORMALMOVE) {
			return false;
		}
		if(m.specialType != Move.NORMALMOVE) {
			if(m.specialType >= Move.PROMOTION) {
				pieceMoved = pieceMoved < 0 ? pieceMoved * -1 : pieceMoved;
				if(m.specialType == 7 || m.specialType > (Board.numTypes * 2)) {
					return false;
				}
				if(pieceMoved == Board.piece.WPAWN.ordinal() || pieceMoved == Board.piece.BPAWN.ordinal()  || 
						pieceMoved == Board.piece.WBRAWN.ordinal() || pieceMoved == Board.piece.BBRAWN.ordinal()) {
					return true;
				}else {
					return false;
				}
			}
			else if(m.specialType == Move.CASTLE) {
				//XXX make sure it is possible to castle-- possibly call the Movegenerator Function.
				if((pieceMoved == -1 * Board.piece.WKING.ordinal() && movedTo == -4) || (pieceMoved == -7 - Board.numTypes && movedTo == -4 - Board.numTypes)) {
					return true;
				}
				else {
					return false;
				}
			}
		}
		//TODO validate Path.....
		return true;
	}
	
	// Determine if we started the turn in check, by passing on all active
	// timelines.
	// For this to work, we assume that the Present and active timelines are already
	// calculated.
	protected boolean isInCheck() {
		ArrayList<Integer> nullmoves = new ArrayList<Integer>();
		for (int i = minActiveTL; i < maxActiveTL; i++) {
			Timeline t = getTimeline(i);
			if (t.colorPlayable == color && t.Tend == startPresent) {
				t.addNullMove();
				nullmoves.add(i);
			}
		}
		boolean inCheck = opponentCanCaptureKing();
		for (int i : nullmoves) {
			getTimeline(i).undoMove();
		}
		return inCheck;
	}

	// Determine if the opponent can immediatly capture our king without null moves,
	// IE this will be used to determine if a move is legal, and also determine
	// if an we start turn in check
	protected boolean opponentCanCaptureKing() {
		for (int i = minTL; i <= maxTL; i++) {
			Timeline t = getTimeline(i);
			if (t.colorPlayable != color) {
				ArrayList<CoordFour> attackingPieces = MoveGenerator.getCheckingPieces(this,
						new CoordFive(0, 0, t.Tend, i, !this.color));
				if (attackingPieces.size() != 0)
					return true;
			}
		}
		return false;
	}

	// Returns whether the coordinate is in bounds.
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

	// Returns whether the given coordinate is playable.
	public boolean coordIsPlayable(CoordFive c) {
		if (c == null || !layerExists(c.L) || c.x < 0 || c.x > this.width || c.y < 0 || c.y > this.height)
			return false;
		return getTimeline(c.L).isMostRecentTime(c.T, c.color);
	}

	public boolean layerExists(int layer) {
		return (layer >= minTL && layer <= maxTL);
	}

	// This assumes that the activeness has been calculated
	public boolean layerIsActive(int layer) {
		if (layer < minActiveTL || layer > maxActiveTL) {
			return false;
		}
		return true;
	}

	public Timeline getTimeline(int layer) {
		if (!layerExists(layer)) {
			return null;
		}
		return multiverse.get(layer + (-1 * minTL));
	}

	public Board getBoard(CoordFour c, boolean boardColor) {
		if (!layerExists(c.L)) {
			return null;
		}
		return multiverse.get(c.L + (-1 * minTL)).getBoard(c.T, boardColor);
	}

	public Board getBoard(CoordFive temporalCoord) {
		return getBoard(temporalCoord, temporalCoord.color);
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
		return Board.ERRORSQUARE;
	}

	public boolean getColor() {
		return color;
	}

	/**
	 * Prints the entire game state -- not very visually appealing though mainly
	 * used for debugging
	 */
	public void printMultiverse() {
		System.out.println("Turn: " + color);
		int tl = minTL;
		for (Timeline t : multiverse) {
			System.out.println("----------------------TL" + tl + "----------------------");
			t.printTimleline();
			tl++;
		}
	}

	/**
	 * undo a turn, based on what timelines were effected on that move, this relies
	 * on another structure keeping track of the gamestate changes.
	 * 
	 * @param tlmoved an int array of timelines to undo.
	 * @return false
	 */
	public boolean undoTurn(int[] tlmoved) {
		if (tlmoved.length == 0)
			return false;
		for(int index = tlmoved.length - 1; index >= 0; index--) {
			if (getTimeline(tlmoved[index]).undoMove()) {
				// this means that the timeline had only one board.
				multiverse.remove(GameState.getTLIndex(tlmoved[index], this.minTL));
				if(tlmoved[index] == this.maxTL) {
					maxTL--;
				} else {
					minTL++;
				}
			}
		}
		determineActiveTLS();
		color = !color;
		calcPresent();
		startPresent = present;
		return true;
	}

	// same as above, for ArrayList
	public boolean undoTurn(ArrayList<Integer> tlmoved) {
		if (tlmoved.size() == 0)
			return false;
		for(int index = tlmoved.size() - 1; index >= 0; index--) {
			if (getTimeline(tlmoved.get(index)).undoMove()) {
				// this means that the timeline had only one board.
				multiverse.remove(GameState.getTLIndex(tlmoved.get(index), this.minTL));
				if(tlmoved.get(index) == this.maxTL) {
					maxTL--;
				} else {
					minTL++;
				}
			}
		}
		determineActiveTLS();
		color = !color;
		calcPresent();
		startPresent = present;
		return true;
	}

	public void undoTempMoves() {
		if (turnTLs.size() <= 0)
			return;
		for(int index = turnTLs.size() - 1; index >= 0; index--) {
			if (getTimeline(turnTLs.get(index)).undoMove()) {
				// this means that the timeline had only one board.
				multiverse.remove(GameState.getTLIndex(turnTLs.get(index), this.minTL));
				if (color) {
					maxTL--;
				} else {
					minTL++;
				}
			}
		}
		determineActiveTLS();
		turnTLs.clear();
		turnMoves.clear();
	}

	/**
	 * this function changes the object to reflect which TL are 'active' Uses
	 * 'handicap' for even tl things -- May be wrong but we will see.
	 * 
	 * Should calculate this after every move that is non spatial -- and
	 * therefore all calculations should be working with correct numbers 
	 */
	protected void determineActiveTLS() {
		// case 1 -- black has branched more.
		if (maxTL < Math.abs(minTL)) {
			maxActiveTL = maxTL;
			minActiveTL = Math.max(-1 - maxActiveTL + tlHandicap, minTL);
		}
		// case 2 -- white has branched more.
		else if (Math.abs(minTL) < maxTL) {
			minActiveTL = minTL;
			maxActiveTL = Math.min(1 + Math.abs(minActiveTL) + tlHandicap, maxTL);
		}
		// case 3 -- they have branched the same # of times
		else {
			minActiveTL = minTL;
			maxActiveTL = maxTL;
		}
	}

	// Sets the present time, and returns the color of the present. I may change
	// that to a variable.
	protected boolean calcPresent() {
		int presentTime = getTimeline(minActiveTL).Tend;
		boolean presentColor = getTimeline(minActiveTL).colorPlayable;
		for (int i = minActiveTL; i <= maxActiveTL; i++) {
			Timeline t = getTimeline(i);
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

	// return true if a branch will be active, this is for checkmate detection.(ie.
	// if we can branch, check for easy moves that may take us out of check)
	private boolean canActiveBranch() {
		if (color) {
			return maxActiveTL <= (tlHandicap - minActiveTL);
		} else {
			return (minActiveTL * -1) <= (maxActiveTL + tlHandicap);
		}
	}

	// TODO set this to return a turn so we can hint, check if this moves properly.
	// TODO Finish this function, Make it so that not all permutations between spatial moves are made.
	public boolean isMated() {
		determineActiveTLS();
		calcPresent();
		if (opponentCanCaptureKing()) {
			return true;
		}
		// Get all the moves from the current place 
		ArrayList<ArrayList<Move>> allMoves = new ArrayList<ArrayList<Move>>();
		for (int i = minTL; i <= maxTL; i++) {
			ArrayList<Move> tlMoves = new ArrayList<Move>();
			Timeline t = getTimeline(i);
			if (t.colorPlayable != color) {
				continue;
			}
			// the null move marks that the timeline is unmoved. Ie this is the case for
			// certain things.(inactive, future timelines etc.)
			tlMoves.add(null);
			tlMoves.addAll(MoveGenerator.getAllMoves(this, color, t.Tend, i));
			allMoves.add(tlMoves);
		}
		//DO a first pass to get rid of any moves that immediatly put you in check
		for(ArrayList<Move> tlMoves : allMoves) {
			for(int i = 0; i < tlMoves.size(); i++) {// TODO test if this function works
				this.makeMove(tlMoves.get(i));
				CoordFive loc = new CoordFive(0 , 0 , tlMoves.get(i).origin.T, tlMoves.get(i).origin.L, this.color);
				if(MoveGenerator.getCheckingPieces(this, loc).size() > 0) {
					tlMoves.remove(i);
				}
				this.undoTempMoves();
				tlMoves.remove(i);
				i--;//I assume I need this, but maybe not.
			}
		}
		int curMove[] = new int[allMoves.size()];
		while (curMove[0] < allMoves.get(0).size()) {
			Move[] moves = new Move[curMove.length];
			for (int getM = 0; getM < curMove.length; getM++) {
				moves[getM] = allMoves.get(getM).get(curMove[getM]);
			}
			if (this.validateAllPermutations(moves)) {
				//System.out.println(Arrays.toString(moves));
				return false;
			}
			int cursor = 0;
			while (true) {
				curMove[cursor]++;
				if (curMove[cursor] >= allMoves.get(cursor).size()) {
					curMove[cursor] = 0;
					cursor++;
					if (cursor >= curMove.length) {
						return true;
					}
				} else {
					break;
				}
			}
		}
		return true;
	}

	// returns true if the position is checkmate.
	public boolean bruteForceMateDetection() {
		determineActiveTLS();
		calcPresent();
		if (opponentCanCaptureKing()) {
			return true;
		}
		// This looks like it should be hackey and bad, i should change it.
		ArrayList<ArrayList<Move>> allMoves = new ArrayList<ArrayList<Move>>();
		for (int i = minTL; i <= maxTL; i++) {
			ArrayList<Move> tlMoves = new ArrayList<Move>();
			Timeline t = getTimeline(i);
			if (t.colorPlayable != color) {
				continue;
			}
			// the null move marks that the timeline is unmoved. Ie this is the case for
			// certain things.(inactive, future timelines etc.)
			tlMoves.add(null);
			tlMoves.addAll(MoveGenerator.getAllMoves(this, color, t.Tend, i));
			allMoves.add(tlMoves);
		}
		int curMove[] = new int[allMoves.size()];
		while (curMove[0] < allMoves.get(0).size()) {
			Move[] moves = new Move[curMove.length];
			for (int getM = 0; getM < curMove.length; getM++) {
				moves[getM] = allMoves.get(getM).get(curMove[getM]);
			}
			if (this.validateAllPermutations(moves)) {
				//System.out.println(Arrays.toString(moves));
				return false;
			}
			int cursor = 0;
			while (true) {
				curMove[cursor]++;
				if (curMove[cursor] >= allMoves.get(cursor).size()) {
					curMove[cursor] = 0;
					cursor++;
					if (cursor >= curMove.length) {
						return true;
					}
				} else {
					break;
				}
			}
		}
		return true;
	}
	
	// Looks at all permutations of a moveset and validates them. this uses a swap
	// permutation algorithm.
	public boolean validateAllPermutations(Move[] moves) {
		if (validateTurn(moves)) {
			return true;
		}
		Move[] temp = new Move[moves.length];
		int counter = 0;
		for (Move m : moves) {
			temp[counter++] = m;
		}
		boolean plus = false;
		int[] c = new int[temp.length];
		for (int i = 0; i < temp.length;) {
			if (c[i] < i) {
				if (i % 2 == 0) {
					swap(temp, 0, i);

				} else {
					swap(temp, c[i], i);
				}
				if (validateTurn(temp)) {
					return true;
				}
				plus = !plus;
				c[i]++;
				i = 0;
			} else {
				c[i] = 0;
				i++;
			}
		}
		return false;
	}

	public static void swap(Object[] array, int index1, int index2) {
		Object obj = array[index1];
		array[index1] = array[index2];
		array[index2] = obj;
	}

	public static int getTLIndex(int layer, int minTL) {
		return layer + (-1 * minTL);
	}

}

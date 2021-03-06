package engine;

import java.util.ArrayList;

public class Timeline implements Comparable<Timeline>{
	/* the white and black boards on the timeline. */
	public ArrayList<Board> wboards;
	public ArrayList<Board> bboards;
	public int layer;
	/* the absolute start and end time of both white or black */
	public int Tstart;
	public int Tend;
	/* white start time, end time */
	public int whiteStart;
	public int whiteEnd;
	/* black start time, end time */
	public int blackStart;
	public int blackEnd;
	/* the color of the current playable board */
	public boolean colorPlayable;
	/*
	 * the color of the first board of the timeline, white(true) for any timeline <
	 * 0. and black(false) for any timeline above 0
	 */
	public boolean colorStart;
	/* the "activeness" of the timeline, not sure if this fits here */
	boolean active;

	// starts the timeline with a timestart and layer
	public Timeline(Board origin, boolean boardColor, int startTime, int layer) {
		wboards = new ArrayList<Board>();
		bboards = new ArrayList<Board>();
		if (boardColor) {
			wboards.add(origin);
			whiteStart = startTime;
			whiteEnd = startTime;
			blackStart = startTime;
			blackEnd = startTime - 1;
		} else {
			bboards.add(origin);
			whiteStart = startTime + 1;
			whiteEnd = startTime;
			blackStart = startTime;
			blackEnd = startTime;
		}
		this.layer = layer;
		colorPlayable = boardColor;
		colorStart = boardColor;
		Tstart = startTime;
		Tend = startTime;
	}

	// returns if the time exists or not, ie if the time is in bounds @TODO fix
	// this, not nessesaraly true for instance if white branches to t1, a black t1
	// exists but not white t1, but timestart would be 1,
	public boolean timeExists(int t, boolean boardColor) {
		if ((boardColor == GameState.WHITE && (t < whiteStart || t > whiteEnd))) {
			return false;
		} else if ((boardColor == GameState.BLACK && (t < blackStart || t > blackEnd))) {
			return false;
		}
		return true;
	}

	public boolean isMostRecentTime(int t, boolean color) {
		return colorPlayable == color && t == Tend;
	}

	// gets the board on the timeline at time T and color C
	public Board getBoard(int t, boolean boardColor) {
		if (!timeExists(t, boardColor)) {
			return null;
		}
		if (boardColor == GameState.WHITE) {
			return wboards.get(t - whiteStart);
		} else {
			return bboards.get(t - blackStart);
		}
	}

	// gets the playable board on the timeline
	public Board getPlayableBoard() {
		if (colorPlayable) {
			return wboards.get(wboards.size() - 1);
		} else {
			return bboards.get(bboards.size() - 1);
		}
	}

	public int getSquare(CoordFour c, boolean color) {
		Board b = getBoard(c.T, color);
		if (b != null) {
			return b.getSquare(c);
		}
		return Board.ERRORSQUARE;
	}

	public boolean addSpatialMove(Move m, boolean moveColor) {
		if (moveColor != colorPlayable)//XXX move this validation up the chain.
			return false;
		Board b = getPlayableBoard();
		Board newBoard = new Board(b);
		int piece = newBoard.getSquare(m.origin);
		piece = piece < 0 ? piece * -1 : piece;
		m.pieceMoved = piece;
		newBoard.setSquare(m.origin, Board.piece.EMPTY.ordinal());
		newBoard.setSquare(m.dest, piece);
		if((piece == Board.piece.WPAWN.ordinal() || piece == Board.piece.BPAWN.ordinal() || piece == Board.piece.WBRAWN.ordinal() || piece == Board.piece.BBRAWN.ordinal()) && Math.abs(m.dest.y - m.origin.y) == 2) {
			newBoard.enPassentSquare = m.dest.clone();
			newBoard.enPassentSquare.y = (m.dest.y + m.origin.y) / 2; 
		}
		if((piece == Board.piece.WPAWN.ordinal() || piece == Board.piece.BPAWN.ordinal() || piece == Board.piece.WBRAWN.ordinal() || piece == Board.piece.BBRAWN.ordinal()) && b.enPassentSquare != null && m.dest.spatialEquals(b.enPassentSquare) ) {
			CoordFour pawnSquare = m.origin.clone();
			pawnSquare.x = m.dest.x;
			newBoard.setSquare(pawnSquare, Board.EMPTYSQUARE);
		}
		return addMove(newBoard);
	}
	
	public boolean castleKing(Move m) {
		Board b = getPlayableBoard();
		Board newBoard = new Board(b);
		int king = newBoard.getSquare(m.origin) * -1;
		CoordFour direction = CoordFour.sub(m.dest,m.origin);
		direction.flatten();
		CoordFour index = CoordFour.add(direction, m.origin);
		while(b.getSquare(index) != Board.piece.WROOK.ordinal() * -1 && b.getSquare(index) != Board.piece.BROOK.ordinal() * -1) {
			index.add(direction);
		}
		m.pieceMoved = king;
		int rook = newBoard.getSquare(index) * -1;
		newBoard.setSquare(m.origin,0);
		newBoard.setSquare(index, 0);
		newBoard.setSquare(m.dest,king);
		newBoard.setSquare(CoordFour.sub(m.dest, direction), rook);
		return addMove(newBoard);
	}
	
	public boolean promote(Move m) {
		Board b = getPlayableBoard();
		Board newBoard = new Board(b);
		newBoard.setSquare(m.origin, Board.piece.EMPTY.ordinal());
		newBoard.setSquare(m.dest, m.specialType);
		return addMove(newBoard);
	}

	// adds jumping origin move, basically just removes that piece from the board.
	public int addJumpingMove(Move m, boolean moveColor) {
		if (moveColor != colorPlayable)
			return -1;
		CoordFour origin = m.origin;
		Board b = getPlayableBoard();
		Board newBoard = new Board(b);
		int piece =  newBoard.getSquare(origin);
		piece = piece < 0 ? piece * -1 : piece;
		m.pieceMoved = piece;
		newBoard.setSquare(origin, Board.piece.EMPTY.ordinal());
		addMove(newBoard);
		return piece;
	}

	// add a move jumping, if the move is branching return the branched board,
	// otherwise, add the board onto the end of the timeline.
	public Board addJumpingMoveDest(CoordFour dest, boolean moveColor, int piece) {
		Board b = getBoard(dest.T, moveColor);
		Board newBoard = new Board(b);
		newBoard.setSquare(dest, piece);
		if (dest.T != Tend || moveColor != colorPlayable) {
			return newBoard;
		}
		addMove(newBoard);
		return null;
	}

	// Adds a null move to the Timeline. This can be used to check for certain
	// things.
	public void addNullMove() {
		addMove(getPlayableBoard());
	}

	// add a board to the end of the timeline.
	private boolean addMove(Board b) {
		if (colorPlayable) {
			bboards.add(b);
			blackEnd++;
		} else {
			wboards.add(b);
			whiteEnd++;
			Tend++;
		}
		colorPlayable = !colorPlayable;
		return true;
	}

	// will pop off the last board to 'undo'
	public boolean undoMove() {
		if (colorPlayable) {
			wboards.remove(wboards.size() - 1);
			whiteEnd--;
			Tend--;
		} else {
			bboards.remove(bboards.size() - 1);
			blackEnd--;
		}
		colorPlayable = !colorPlayable;
		if (wboards.size() == 0 && bboards.size() == 0) {
			return true;
		}
		return false;
	}

	// prints the board in a primative way
	public void printTimleline() {
		int lastWindex = wboards.size();
		int lastBindex = bboards.size();
		if (colorStart) {
			for (int i = 0; i < lastBindex || i < lastWindex; i++) {
				if (i < lastWindex) {
					System.out.println("__W_T_" + (i + Tstart) + "__\n");
					System.out.println(wboards.get(i));
				}
				if (i < lastBindex) {
					System.out.println("__B_T_" + (i + Tstart) + "__\n");
					System.out.println(bboards.get(i));
				}
			}
		} else {
			for (int i = 0; i < lastBindex || i < lastWindex; i++) {
				if (i < lastBindex) {
					System.out.println("__B_T_" + (i + Tstart) + "__\n");
					System.out.println(bboards.get(i));
				}
				if (i < lastWindex) {
					System.out.println("__W_T_" + (i + Tstart) + "__\n");
					System.out.println(wboards.get(i));
				}
			}
		}
	}

	@Override
	public int compareTo(Timeline compareTo) {
		if(this.layer > compareTo.layer) {
			return 1;
		}
		if(this.layer > compareTo.layer) {
			return -1;
		}
		return 0;
	}
}

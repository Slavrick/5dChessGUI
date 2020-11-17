package engine;

import java.util.ArrayList;

public class Timeline {
	public ArrayList<Board> wboards;
	public ArrayList<Board> bboards;
	int layer;
	public int Tstart;
	public int Tend;
	public int whiteStart;
	int whiteEnd;
	int blackStart;
	int blackEnd;
	public boolean colorPlayable;
	public boolean colorStart;
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
			blackEnd = startTime-1;
		} else {
			bboards.add(origin);
			whiteStart = startTime + 1;
			whiteEnd = startTime;
			blackStart = startTime;
			blackEnd = startTime;
		}
		colorPlayable = boardColor;
		colorStart = boardColor;
		Tstart = startTime;
		Tend = startTime;
	}

	//returns if the time exists or not, ie if the time is in bounds
	public boolean timeExists(int t, boolean boardColor) {
		if (t > Tstart && t < Tend)
			return true;
		if (boardColor == GameState.WHITE) {
			if (t >= whiteStart && t <= whiteEnd) {
				return true;
			}
		} else {
			if (t >= blackStart && t <= blackEnd) {
				return true;
			}
		}
		return false;
	}

	// gets the board on the timeline at time T and color C
	public Board getBoard(int t, boolean boardColor) {
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
			return bboards.get(wboards.size() - 1);
		}
	}

	// add a board to the end of the timeline.
	public boolean addMove(Board b) {
		if (colorPlayable) {
			bboards.add(b);
			blackEnd++;
		} else {
			wboards.add(b);
			whiteEnd++;
			Tend++;
		}
		colorPlayable = !colorPlayable;
		return false;
	}

	// this func sucks im debating on adding implicit checks here
	public boolean addSpatialMove(Move m, boolean moveColor) {
		if(moveColor != colorPlayable)
			return false;
		Board b = getPlayableBoard();
		Board newBoard = new Board(b);
		int piece = newBoard.brd[m.origin.x][m.origin.y];
		newBoard.brd[m.origin.x][m.origin.y] = Board.piece.EMPTY.ordinal();
		newBoard.brd[m.dest.x][m.dest.y] = piece;
		return addMove(newBoard);
	}

	//adds jumping destination move, basically just removes that piece from the board.
	public int addJumpingMove(CoordFive origin, boolean moveColor) {
		if(moveColor != colorPlayable)
			return -1;
		Board b = getPlayableBoard();
		Board newBoard = new Board(b);
		int piece = newBoard.brd[origin.x][origin.y];
		newBoard.brd[origin.x][origin.y] = Board.piece.EMPTY.ordinal();
		addMove(newBoard);
		return piece;
	}
	
	
	//add a move jumping, if the move is branching return the branched board, otherwise, add the board onto the end of the timeline.
	public Board addJumpingMoveDest(CoordFive dest, boolean moveColor, int piece) {
		Board b = getBoard(dest.T,moveColor);
		Board newBoard = new Board(b);
		newBoard.brd[dest.x][dest.y] = piece;
		if(dest.T != Tend) {
			return newBoard;
		}
		addMove(newBoard);
		return null;
	}
	
	//prints the board in a primative way
	public void printTimleline() { 
		int lastWindex = wboards.size();
		int lastBindex = bboards.size();
		if (colorStart) {
			for (int i = 0; i < lastBindex || i < lastWindex; i++) {
				if(i < lastWindex) {					
					System.out.println("W" + (i+Tstart));
					System.out.println(wboards.get(i));
				}
				if(i < lastBindex) {	
					System.out.println("B" + (i+Tstart));
					System.out.println(bboards.get(i));
				}
			}
		} else {
			for (int i = 0; i < lastBindex || i < lastWindex; i++) {
				if(i < lastBindex) {	
					System.out.println("B");
					System.out.println(bboards.get(i));
				}
				if(i < lastWindex) {					
					System.out.println("W");
					System.out.println(wboards.get(i));
				}
			}
		}
	}

	//will pop off the last board to 'undo'
	public boolean undoMove() {
		return false;
	}
}

package engine;

import java.util.ArrayList;

public class AnnotatedTurn extends Turn{
	
	public ArrayList<CoordFive> highlights;
	public ArrayList<Move> arrows;
	public String comment;

	public AnnotatedTurn(ArrayList<Move> turnMoves, ArrayList<Integer> tlEffected) {
		super(turnMoves, tlEffected);
	}
	
}

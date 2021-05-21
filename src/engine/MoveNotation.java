package engine;

public class MoveNotation {
	
	//There are 2 types of pieces, riders and leapers. A rider will "ride" along some sort of line, indefinetely until they hit an edge or other piece.
	//a leaper is a piece who "Leaps" a specific length and stays there.
	//rooks, bishops, queens, unicorns, dragons, are all riders
	//Kings, Rooks, and pawns are leapers. however Pawn 2 move on 2nd/7th rank is not a leap, but psuedo leap/ride.
	
	boolean rider;
	int[] movnote;
	
	public static final int[] ROOK = {1};
	public static final int[] BISHOP = {2};
	public static final int[] UNICORN = {3};
	public static final int[] DRAGON = {4};
	public static final int[] KING = {1,2,3,4};
	public static final int[] QUEEN = {1,2,3,4};
	
	//All commented out vectors are pure foreward time travel, or pure foreward with some spatial
	//which means that it is impossible and we dont need to consider
	//Stuff gets messed up with a vector of 0,
	
	
	public static final CoordFour[] NULLMOVESET = {};
	
	public static final CoordFour[] ROOKMOVESET = {
			//spatial
			new CoordFour(1,0,0,0),
			new CoordFour(0,1,0,0),
			new CoordFour(-1,0,0,0),
			new CoordFour(0,-1,0,0),
			//temporal
			new CoordFour(0,0,0,1),
			new CoordFour(0,0,-1,0),
			new CoordFour(0,0,0,-1)		
			//new CoordFive(0,0,1,0), redundant, cannot move foreward purely
	};
	
	public static final CoordFour[] BISHOPMOVESET = {
		//pure spatial
		new CoordFour(1,1,0,0),
		new CoordFour(1,-1,0,0),
		new CoordFour(-1,1,0,0),
		new CoordFour(-1,-1,0,0),
		//pure Temporal
		new CoordFour(0,0,1,1),
		new CoordFour(0,0,1,-1),
		new CoordFour(0,0,-1,1),
		new CoordFour(0,0,-1,-1),
		//+L
		new CoordFour(1,0,0,1),
		new CoordFour(-1,0,0,1),
		new CoordFour(0,1,0,1),
		new CoordFour(0,-1,0,1),
		//-L
		new CoordFour(1,0,0,-1),
		new CoordFour(-1,0,0,-1),
		new CoordFour(0,1,0,-1),
		new CoordFour(0,-1,0,-1),
		//-T
		new CoordFour(1,0,-1,0),
		new CoordFour(-1,0,-1,0),
		new CoordFour(0,1,-1,0),
		new CoordFour(0,-1,-1,0),
		//+T
		/*
		new CoordFour(1,0,1,0),
		new CoordFour(-1,0,1,0),
		new CoordFour(0,1,1,0),
		new CoordFour(0,-1,1,0),
		*/
	};
	
	public static final CoordFour[] UnicornMoveset = {
			//new CoordFour(1,1,1,0),
			new CoordFour(1,1,0,1),
			new CoordFour(1,0,1,1),
			new CoordFour(0,1,1,1),
			
			//new CoordFour(-1,1,1,0),
			new CoordFour(-1,1,0,1),
			new CoordFour(-1,0,1,1),
			new CoordFour(0,-1,1,1),
			
			//new CoordFour(1,-1,1,0),
			new CoordFour(1,-1,0,1),
			new CoordFour(1,0,-1,1),
			new CoordFour(0,1,-1,1),
			
			new CoordFour(1,1,-1,0),
			new CoordFour(1,1,0,-1),
			new CoordFour(1,0,1,-1),
			new CoordFour(0,1,1,-1),
			
			//new CoordFour(-1,-1,1,0),
			new CoordFour(-1,-1,0,1),
			new CoordFour(-1,0,-1,1),
			new CoordFour(0,-1,-1,1),
			
			new CoordFour(-1,1,-1,0),
			new CoordFour(-1,1,0,-1),
			new CoordFour(-1,0,1,-1),
			new CoordFour(0,-1,1,-1),
			
			new CoordFour(1,-1,-1,0),
			new CoordFour(1,-1,0,-1),
			new CoordFour(1,0,-1,-1),
			new CoordFour(0,1,-1,-1),
			
			new CoordFour(-1,-1,-1,0),
			new CoordFour(-1,-1,0,-1),
			new CoordFour(-1,0,-1,-1),
			new CoordFour(0,-1,-1,-1),
	};
	
	public static final CoordFour[] DragonMoveset = {
			new CoordFour(1,1,1,1),
			new CoordFour(-1,1,1,1),	
			new CoordFour(1,-1,1,1),	
			new CoordFour(1,1,-1,1),
			new CoordFour(1,1,1,-1),	
			new CoordFour(-1,-1,1,1),	
			new CoordFour(-1,1,-1,1),	
			new CoordFour(-1,1,1,-1),
			new CoordFour(1,-1,-1,1),
			new CoordFour(1,-1,1,-1),
			new CoordFour(1,1,-1,-1),
			new CoordFour(1,-1,-1,-1),
			new CoordFour(-1,1,-1,-1),	
			new CoordFour(-1,-1,1,-1),	
			new CoordFour(-1,-1,-1,1),
			new CoordFour(-1,-1,-1,-1),
	};
	
	public static final CoordFour[] KNIGHTMOVESET = {
			//pure Spatial
			new CoordFour(1,2,0,0),
			new CoordFour(2,1,0,0),
			new CoordFour(-1,2,0,0),
			new CoordFour(-2,1,0,0),
			new CoordFour(-2,-1,0,0),
			new CoordFour(-1,-2,0,0),
			new CoordFour(1,-2,0,0),
			new CoordFour(2,-1,0,0),
			//pure temporal
			new CoordFour(0,0,1,2),
			new CoordFour(0,0,2,1),
			new CoordFour(0,0,-1,2),
			new CoordFour(0,0,-2,1),
			new CoordFour(0,0,-2,-1),
			new CoordFour(0,0,-1,-2),
			new CoordFour(0,0,1,-2),
			new CoordFour(0,0,2,-1),
			//Half Spatial/temporal
			//    +L
			new CoordFour(2,0,0,1),
			new CoordFour(-2,0,0,1),
			new CoordFour(0,2,0,1),
			new CoordFour(0,-2,0,1),
			new CoordFour(1,0,0,2),
			new CoordFour(-1,0,0,2),
			new CoordFour(0,1,0,2),
			new CoordFour(0,-1,0,2),
			//  -L
			new CoordFour(2,0,0,-1),
			new CoordFour(-2,0,0,-1),
			new CoordFour(0,2,0,-1),
			new CoordFour(0,-2,0,-1),
			new CoordFour(1,0,0,-2),
			new CoordFour(-1,0,0,-2),
			new CoordFour(0,1,0,-2),
			new CoordFour(0,-1,0,-2),
			//  -T
			new CoordFour(2,0,-1,0),
			new CoordFour(-2,0,-1,0),
			new CoordFour(0,2,-1,0),
			new CoordFour(0,-2,-1,0),
			new CoordFour(1,0,-2,0),
			new CoordFour(-1,0,-2,0),
			new CoordFour(0,1,-2,0),
			new CoordFour(0,-1,-2,0)
			//pure foreward (+T)
			/*
			new CoordFour(2,0,1,0),
			new CoordFour(-2,0,1,0),
			new CoordFour(0,2,1,0),
			new CoordFour(0,-2,1,0),
			new CoordFour(1,0,2,0),
			new CoordFour(-1,0,2,0),
			new CoordFour(0,1,2,0),
			new CoordFour(0,-1,2,0)
			*/
			
	};
	
	public static final CoordFour[] PRINCESSMOVESET = {
			//ROOK
			//spatial
			new CoordFour(1,0,0,0),
			new CoordFour(0,1,0,0),
			new CoordFour(-1,0,0,0),
			new CoordFour(0,-1,0,0),
			//temporal
			new CoordFour(0,0,0,1),
			new CoordFour(0,0,-1,0),
			new CoordFour(0,0,0,-1),		
			//new CoordFive(0,0,1,0), redundant, cannot move foreward purely
			
			//Bishop
			//pure spatial
			new CoordFour(1,1,0,0),
			new CoordFour(1,-1,0,0),
			new CoordFour(-1,1,0,0),
			new CoordFour(-1,-1,0,0),
			//pure Temporal
			new CoordFour(0,0,1,1),
			new CoordFour(0,0,1,-1),
			new CoordFour(0,0,-1,1),
			new CoordFour(0,0,-1,-1),
			//+L
			new CoordFour(1,0,0,1),
			new CoordFour(-1,0,0,1),
			new CoordFour(0,1,0,1),
			new CoordFour(0,-1,0,1),
			//-L
			new CoordFour(1,0,0,-1),
			new CoordFour(-1,0,0,-1),
			new CoordFour(0,1,0,-1),
			new CoordFour(0,-1,0,-1),
			//-T
			new CoordFour(1,0,-1,0),
			new CoordFour(-1,0,-1,0),
			new CoordFour(0,1,-1,0),
			new CoordFour(0,-1,-1,0),
			//+T
			/*
			new CoordFour(1,0,1,0),
			new CoordFour(-1,0,1,0),
			new CoordFour(0,1,1,0),
			new CoordFour(0,-1,1,0),
			*/
	};
	
	public static final CoordFour[] KINGMOVESET = {
			//pure Spatial
			new CoordFour(1,0,0,0),
			new CoordFour(1,1,0,0),
			new CoordFour(1,-1,0,0),
			new CoordFour(-1,0,0,0),
			new CoordFour(-1,1,0,0),
			new CoordFour(-1,-1,0,0),
			new CoordFour(0,1,0,0),
			new CoordFour(0,-1,0,0),
			//+L board
			new CoordFour(1,0,0,1),
			new CoordFour(1,1,0,1),
			new CoordFour(1,-1,0,1),
			new CoordFour(-1,0,0,1),
			new CoordFour(-1,1,0,1),
			new CoordFour(-1,-1,0,1),
			new CoordFour(0,1,0,1),
			new CoordFour(0,-1,0,1),
			new CoordFour(0,0,0,1),
			//-L board
			new CoordFour(1,0,0,-1),
			new CoordFour(1,1,0,-1),
			new CoordFour(1,-1,0,-1),
			new CoordFour(-1,0,0,-1),
			new CoordFour(-1,1,0,-1),
			new CoordFour(-1,-1,0,-1),
			new CoordFour(0,1,0,-1),
			new CoordFour(0,-1,0,-1),
			new CoordFour(0,0,0,-1),
			//-T board
			new CoordFour(1,0,-1,0),
			new CoordFour(1,1,-1,0),
			new CoordFour(1,-1,-1,0),
			new CoordFour(-1,0,-1,0),
			new CoordFour(-1,1,-1,0),
			new CoordFour(-1,-1,-1,0),
			new CoordFour(0,1,-1,0),
			new CoordFour(0,-1,-1,0),
			new CoordFour(0,0,-1,0),
			//-T,-L
			new CoordFour(1,0,-1,-1),
			new CoordFour(1,1,-1,-1),
			new CoordFour(1,-1,-1,-1),
			new CoordFour(-1,0,-1,-1),
			new CoordFour(-1,1,-1,-1),
			new CoordFour(-1,-1,-1,-1),
			new CoordFour(0,1,-1,-1),
			new CoordFour(0,-1,-1,-1),
			new CoordFour(0,0,-1,-1),
			//-T,+L
			new CoordFour(1,0,-1,1),
			new CoordFour(1,1,-1,1),
			new CoordFour(1,-1,-1,1),
			new CoordFour(-1,0,-1,1),
			new CoordFour(-1,1,-1,1),
			new CoordFour(-1,-1,-1,1),
			new CoordFour(0,1,-1,1),
			new CoordFour(0,-1,-1,1),
			new CoordFour(0,0,-1,1),
			//+T,+L
			new CoordFour(1,0,1,1),
			new CoordFour(1,1,1,1),
			new CoordFour(1,-1,1,1),
			new CoordFour(-1,0,1,1),
			new CoordFour(-1,1,1,1),
			new CoordFour(-1,-1,1,1),
			new CoordFour(0,1,1,1),
			new CoordFour(0,-1,1,1),
			new CoordFour(0,0,1,1),
			//+T,-L
			new CoordFour(1,0,1,-1),
			new CoordFour(1,1,1,-1),
			new CoordFour(1,-1,1,-1),
			new CoordFour(-1,0,1,-1),
			new CoordFour(-1,1,1,-1),
			new CoordFour(-1,-1,1,-1),
			new CoordFour(0,1,1,-1),
			new CoordFour(0,-1,1,-1),
			new CoordFour(0,0,1,-1),
			//+T board
			/*
			new CoordFour(1,0,1,0),
			new CoordFour(1,1,1,0),
			new CoordFour(1,-1,1,0),
			new CoordFour(-1,0,1,0),
			new CoordFour(-1,1,1,0),
			new CoordFour(-1,-1,1,0),
			new CoordFour(0,1,1,0),
			new CoordFour(0,-1,1,0),
			new CoordFour(0,0,1,0),
			*/
	};
	
	public static final CoordFour[] whitePawnMovement = {
			new CoordFour(0,1,0,0),
			new CoordFour(0,0,0,-1)
	};
	
	public static final CoordFour[] whitePawnattack = {
			new CoordFour(1,1,0,0),
			new CoordFour(-1,1,0,0),
			new CoordFour(0,0,1,-1),
			new CoordFour(0,0,-1,-1)
	};
	
	public static final CoordFour[] blackPawnMovement = {
			new CoordFour(0,-1,0,0),
			new CoordFour(0,0,0,1),
	};
	
	public static final CoordFour[] blackPawnattack = {
			new CoordFour(1,-1,0,0),
			new CoordFour(-1,-1,0,0),
			new CoordFour(0,0,1,1),
			new CoordFour(0,0,-1,1)
	};

	
	public static final CoordFour[] whiteBrawnattack = {
			//Pawn Captures
			new CoordFour(1,1,0,0),
			new CoordFour(-1,1,0,0),
			new CoordFour(0,0,1,-1),
			new CoordFour(0,0,-1,-1),
			//Brawn Specific Captures
			new CoordFour(0,1,-1,0),
			//new CoordFour(0,1,1,0),
			new CoordFour(0,1,0,-1),
			new CoordFour(1,0,0,-1),
			new CoordFour(-1,0,0,-1),
	};
	
	public static final CoordFour[] blackBrawnattack = {
			//Pawn Captures
			new CoordFour(1,-1,0,0),
			new CoordFour(-1,-1,0,0),
			new CoordFour(0,0,1,1),
			new CoordFour(0,0,-1,1),
			//Brawn Captures
			new CoordFour(0,-1,-1,0),
			new CoordFour(0,-1,1,0),
			new CoordFour(0,-1,0,1),
			new CoordFour(1,0,0,1),
			new CoordFour(-1,0,0,1),
			
	};
	
	
	//Jank Solution to the fact that pawns and brawns can move in so many ways, for reverse lookup(Searching for a source from a destination)
	//Notice the 1 or two movement. This could fail if there is an inproper configuration, ie. the reverse lookup is destined to fail(this doesnt have any validations)
	//However say 2 pawns are in a line, This code will always find the one in front based of this description(so it should work fine, given that the reverse lookup is
	//non ambiguous and actually exitst)
	public static final CoordFour[] whitePawnRLkup = {
			//Pawn Movement
			new CoordFour(0,1,0,0),
			new CoordFour(0,0,0,-1),
			new CoordFour(0,2,0,0),
			new CoordFour(0,0,0,-2),
			//Pawn Attack
			new CoordFour(1,1,0,0),
			new CoordFour(-1,1,0,0),
			new CoordFour(0,0,1,-1),
			new CoordFour(0,0,-1,-1)
	};
	
	public static final CoordFour[] blackPawnRLkup = {
			//Pawn Movement
			new CoordFour(0,-1,0,0),
			new CoordFour(0,0,0,1),
			new CoordFour(0,-2,0,0),
			new CoordFour(0,0,0,2),
			//Pawn Attack
			new CoordFour(1,-1,0,0),
			new CoordFour(-1,-1,0,0),
			new CoordFour(0,0,1,1),
			new CoordFour(0,0,-1,1)
	};
	
	public static final CoordFour[] whiteBrawnRLkup = {
			//Pawn Movement
			new CoordFour(0,1,0,0),
			new CoordFour(0,0,0,-1),
			new CoordFour(0,2,0,0),
			new CoordFour(0,0,0,-2),
			//Pawn Captures
			new CoordFour(1,1,0,0),
			new CoordFour(-1,1,0,0),
			new CoordFour(0,0,1,-1),
			new CoordFour(0,0,-1,-1),
			//Brawn Specific Captures
			new CoordFour(0,1,-1,0),
			new CoordFour(0,1,1,0),
			new CoordFour(0,1,0,-1),
			new CoordFour(1,0,0,-1),
			new CoordFour(-1,0,0,-1),
	};
	
	public static final CoordFour[] blackBrawnRLkup = {
			//Pawn Movement
			new CoordFour(0,-1,0,0),
			new CoordFour(0,0,0,1),
			new CoordFour(0,-2,0,0),
			new CoordFour(0,0,0,2),
			//Pawn Captures
			new CoordFour(1,-1,0,0),
			new CoordFour(-1,-1,0,0),
			new CoordFour(0,0,1,1),
			new CoordFour(0,0,-1,1),
			//Brawn Captures
			new CoordFour(0,-1,-1,0),
			new CoordFour(0,-1,1,0),
			new CoordFour(0,-1,0,1),
			new CoordFour(1,0,0,1),
			new CoordFour(-1,0,0,1),
	};
	
	
	//Impossible to avoid magic number antipattern below (or so i think)
	//If I were to do for instantce case board.piece.pawn.ordinal()
	//java complains its no constant :(
	//Perhaps changing this to an if else is something I want to do
	//However, as long as I strictly add new pieces in a predictable pattern, this is fine
	
	/**
	 * Take a piece and turns it into an array of movement vectors,
	 * 
	 * @param piece ordinal integer relating to the piece enum defined in board
	 * @return an array of vectors, or empty array if nothing is found.
	 */
	public static CoordFour[] getMoveVectors(int piece) {
		piece = piece < 0 ? piece * -1 : piece;
		switch(piece) {
		case 1:
			return whitePawnMovement;
		case 1 + Board.numTypes:
			return blackPawnMovement;
		case 2:
		case 2 + Board.numTypes:
			return KNIGHTMOVESET;
		case 3:
		case 3 + Board.numTypes:
			return BISHOPMOVESET;
		case 4:
		case 4 + Board.numTypes:
			return ROOKMOVESET;
		case 5:
		case 5 + Board.numTypes:
			return PRINCESSMOVESET;
		case 6:
		case 6 + Board.numTypes:
			//this case is the queen, but it has the same movement vectors as a king, but is a rider instead. making the queen a "king rider" (that sounds dirty)
			return KINGMOVESET;
		case 7:
		case 7 + Board.numTypes:
			return KINGMOVESET;
		case 8:
		case 8 + Board.numTypes:
			return UnicornMoveset;
		case 9:
		case 9 + Board.numTypes:
			return DragonMoveset;
		case 10:
			return whitePawnMovement;
		case 10 + Board.numTypes:
			return blackPawnMovement;
		case 11:
		case 11 + Board.numTypes://Royal Queen
		case 12:
		case 12 + Board.numTypes://Common King
			return KINGMOVESET;
		case 0:
		default:
			return NULLMOVESET;
		}
	}
	
	//returns whether piece is a leaper or no
	public static boolean pieceIsRider(int piece) {
		piece = piece < 0 ? piece * -1 : piece;
		switch(piece) {
		case 1 + Board.numTypes://Pawn
		case 1:
		case 2 + Board.numTypes://Knight
		case 2:
		case 7 + Board.numTypes://King
		case 7:
		case 10 + Board.numTypes://Brawn
		case 10:
		case 12 + Board.numTypes://common king
		case 12:
			return false;
		default: 
			return true;
		}
	}
	
	public static boolean pieceIsRoyal(int piece) {
		piece = piece < 0 ? piece * -1 : piece;
		switch(piece) {
		case 7 + Board.numTypes://King
		case 7:
		case 11 + Board.numTypes://Royal Queen
		case 11:
			return true;
		default: 
			return false;
		}
	}
}

import java.util.*;

public class Board {
	protected Piece[][] board;
	protected Set<Piece> whiteActive;
	protected Set<Piece> whiteGrave;
	protected Set<Piece> whitePlaceable;
	protected Set<Piece> blackActive;
	protected Set<Piece> blackGrave;
	protected Set<Piece> blackPlaceable;
	protected Scanner scan;
	protected boolean turn;

	//Constructs a new board of size 8 and initializes the white/black active/grave
	// to empty Sets.
	public Board() {
		board = new Piece[8][8];
		whiteActive = new HashSet<Piece>();
		whiteGrave = new HashSet<Piece>();
		whitePlaceable = new HashSet<Piece>();
		blackActive = new HashSet<Piece>();
		blackGrave = new HashSet<Piece>();
		blackPlaceable = new HashSet<Piece>();
		scan = new Scanner(System.in);
		turn = true;
	}

	//Moves the piece at board[y][x] to board[dy][dx] and returns true if the move is legal,
	// else does nothing and returns false.
	// Special cases to note: Castling - legal, requires the king and rook not moved yet,
	//                                   requires input of the king's start x,y and end dx,dy
	//                                   rather than the rook's start and end
	//                        Pawns move two squares if not moved yet - legal
	//                        En passant - illegal
	//                        King moving into check - legal
	public boolean move(boolean player, int x, int y, int dx, int dy) 
			throws InvalidMoveException, NotYourTurnException {
		if (turn != board[y][x].boolColor()) {
			throw new NotYourTurnException();
		}
		if (!valid(x, y, dx, dy)) {
			throw new InvalidMoveException();
		}
		if (board[dy][dx] != null)
			capture(dx, dy);
		if (board[y][x].toString().equals("K") && dx - x == 2) {
			turn = !turn;
			return queenSide(x, y);
		} else if (board[y][x].toString().equals("K") && dx - x == -2) {
			turn = !turn;
			return kingSide(x, y);
		} else {
			board[dy][dx] = board[y][x];
			board[y][x] = null;
			board[dy][dx].updatePosition(dx, dy);
			board[dy][dx].updatePossibleMoves();
//			if (canPromote(dx, dy)) {
//				promotion(board[dy][dx].boolColor(), dx, dy);
//			}
			turn = !turn;
		}
		return true;
	}

	public boolean canPromote(int dx, int dy) {
		return board[dy][dx].toString().equals("P") && (dy == 7 || dy == 0);
	}
	
	public void promotion(boolean color, int dx, int dy, String ans) {
		if (ans.equals("Q")) {
			board[dy][dx] = new Queen(dx, dy, color);
		} else if (ans.equals("N")) {
			board[dy][dx] = new Knight(dx, dy, color);
		} else if (ans.equals("B")) {
			board[dy][dx] = new Bishop(dx, dy, color);
		} else if (ans.equals("R")) {
			board[dy][dx] = new Rook(dx, dy, color);
		} else {
			System.err.println("No such piece.");
		}
	}

//	public void promotion(boolean color, int dx, int dy) {
//		System.out.println("What would like to promote the pawn to?");
//		String ans = scan.next();
//		if (ans.equals("Q")) {
//			board[dy][dx] = new Queen(dx, dy, color);
//		} else if (ans.equals("N")) {
//			board[dy][dx] = new Knight(dx, dy, color);
//		} else if (ans.equals("B")) {
//			board[dy][dx] = new Bishop(dx, dy, color);
//		} else if (ans.equals("R")) {
//			board[dy][dx] = new Rook(dx, dy, color);
//		} else {
//			System.err.println("No such piece.");
//		}
//	}

	//Castles kingside and returns true if possible to castle
	// (king and kingside rook have not moved), else does nothing and returns false.
	private boolean kingSide(int x, int y) {
		if (board[y][x - 1] == null && board[y][x - 2] == null && board[y][0] != null &&
				board[y][0].toString().equals("R") && !board[y][0].moved()) {
			board[y][x - 2] = board[y][x];
			board[y][x] = null;
			board[y][x - 1] = board[y][0];
			board[y][0] = null;
			board[y][x - 2].updatePosition(x - 2, y);
			board[y][x - 2].updatePossibleMoves();
			board[y][x - 1].updatePosition(x - 1, y);
			board[y][x - 1].updatePossibleMoves();
			return true;
		} else {
			System.out.println("Can't castle.");
			return false;
		}
	}

	//Castles queenside and returns true if possible to castle 
	// (king and queenside rook have not moved), else does nothing and returns false.
	private boolean queenSide(int x, int y) {
		if (board[y][x + 1] == null && board[y][x + 2] == null && board[y][x + 3] == null &&
				board[y][7] != null && board[y][7].toString().equals("R") && !board[y][7].moved()) {
			board[y][x + 2] = board[y][x];
			board[y][x] = null;
			board[y][x + 1] = board[y][7];
			board[y][7] = null;
			board[y][x + 2].updatePosition(x + 2, y);
			board[y][x + 2].updatePossibleMoves();
			board[y][x + 1].updatePosition(x + 1, y);
			board[y][x + 1].updatePossibleMoves();
			return true;
		} else {
			System.out.println("Can't castle.");
			return false;
		}
	}

	//If x,y in bounds and there is a piece at board[y][x]:
	// adds it to whiteGrave if that piece is white, 
	// adds it to blackGrave if that piece is black.
	private void capture(int x, int y) {
		boolean pcolor = board[y][x].boolColor();
		if (pcolor) {
			whiteGrave.add(board[y][x]);
		} else {
			blackGrave.add(board[y][x]);
		}
	}

	//Return true iff it is valid for the piece at board[y][x] to move to board[dy][dx].
	// Returns false if invalid, i.e.: !inBounds(x, y) || !inBounds(dx, dy) || board[y][x] == null
	// || piece is blocked going from x,y to dx,dy
	private boolean valid(int x, int y, int dx, int dy) {
		if (!inBounds(x, y) || !inBounds(dx, dy) || board[y][x] == null || !board[y][x].isPossible(dx, dy)) {
			return false;
		} else {
			if (board[y][x].toString().equals("K") || board[y][x].toString().equals("N")) {
				return !knBlocked(x, y, dx, dy);
			} else if (board[y][x].toString().equals("B")) {
				return !bishBlocked(x, y, dx, dy);
			} else if (board[y][x].toString().equals("Q")) {
				return !queenBlocked(x, y, dx, dy);
			} else if (board[y][x].toString().equals("R")) {
				return !rookBlocked(x, y, dx, dy);
			} else {
				return !pawnBlocked(x, y, dx, dy);
			}
		}
	}

	//Return true iff the queen is blocked (by another piece) from moving from x,y to dx,dy.
	// Special cases to note: returns true if there is an enemy piece on dx,dy 
	// that it would have to capture, returns false if there is an ally piece on dx,dy
	// Pre: dx,dy is along the trajectory of a queen from x,y. 
	private boolean queenBlocked(int x, int y, int dx, int dy) {
		if (Math.abs(dx - x) == Math.abs(dy - y)) {
			return bishBlocked(x, y, dx, dy);
		} else {
			return rookBlocked(x, y, dx, dy);
		}
	}

	//Return true iff the rook is blocked (by another piece) from moving from x,y to dx,dy.
	// Special cases to note: returns true if there is an enemy piece on dx,dy 
	// that it would have to capture, returns false if there is an ally piece on dx,dy
	// Pre: dx,dy is along the trajectory of a rook from x,y. 
	private boolean rookBlocked(int x, int y, int dx, int dy) {
		int incX = 0;
		int incY = 0;
		if (dx - x != 0) {
			incX = (dx - x) / (Math.abs(dx - x));
		} else {
			incY = (dy - y) / (Math.abs(dy - y));
		}
		int tx = x + incX;
		int ty = y + incY;
		while (tx != dx || ty != dy) {
			if (board[ty][tx] != null) {
				return true;
			}
			tx += incX;
			ty += incY;
		}
		return !(board[dy][dx] == null || board[dy][dx].boolColor() ^ board[y][x].boolColor());
	}

	//Return true iff the bishop is blocked (by another piece) from moving from x,y to dx,dy.
	// Special cases to note: returns true if there is an enemy piece on dx,dy 
	// that it would have to capture, returns false if there is an ally piece on dx,dy
	// Pre: dx,dy is along the trajectory of a bishop from x,y. 
	private boolean bishBlocked(int x, int y, int dx, int dy) {
		int incX = (dx - x) / (Math.abs(dx - x));
		int incY = (dy - y) / (Math.abs(dy - y));
		int tx = x + incX;
		int ty = y + incY;
		while (tx != dx || ty != dy) {
			if (board[ty][tx] != null) {
				return true;
			}
			tx += incX;
			ty += incY;
		}
		return !(board[dy][dx] == null || board[dy][dx].boolColor() ^ board[y][x].boolColor());
	}
	
	//Returns true if the knight or king is blocked (by another piece of the same color) from
	// moving from x,y to dx,dy. Else returns false.
	private boolean knBlocked(int x, int y, int dx, int dy) {
		return !(board[dy][dx] == null || board[dy][dx].boolColor() ^ board[y][x].boolColor());
	}

	private boolean pawnBlocked(int x, int y, int dx, int dy) {
		if (x == dx) {
			if (Math.abs(dy - y) == 2) {
				return board[(y + dy) / 2][(x + dx) / 2] != null ||
					   board[dy][dx] != null;
			} 
			return board[dy][dx] != null;
		} else {
			return board[dy][dx] == null ||
				   !(board[dy][dx].boolColor() ^ board[y][x].boolColor());
		}
	}

	public void beginning() {
		for (int i = 0; i < 8; i++) {
			board[1][i] = new Pawn(i, 1, true);
			whiteActive.add(board[1][i]);
			board[6][i] = new Pawn(i, 6, false);
			blackActive.add(board[6][i]);
		}
		for (int i = 0; i < 8; i++) {
			if (i == 0 || 7 - i == 0) {
				board[0][i] = new Rook(i, 0, true);
				board[7][i] = new Rook(i, 7, false);
			} else if (i == 1 || 7 - i == 1) {
				board[0][i] = new Knight(i, 0, true);
				board[7][i] = new Knight(i, 7, false);
			} else if (i == 2 || 7 - i == 2) {
				board[0][i] = new Bishop(i, 0, true);
				board[7][i] = new Bishop(i, 7, false);
			} else {
				if (i == 4) {
					board[0][i] = new Queen(i, 0, true);
					board[7][i] = new Queen(i, 7, false);
				} else {
					board[0][i] = new King(i, 0, true);
					board[7][i] = new King(i, 7, false);
				}
			}
			whiteActive.add(board[0][i]);
			blackActive.add(board[7][i]);
		}
	}

	private boolean inBounds(int x, int y) {
		return (0 <= x) && (x <= 7) && (0 <= y) && (x <= 7);
	}

	public Piece[][] getBoard() { return board; }
	public Set<Piece> getWhiteActive() { return whiteActive; }
	public Set<Piece> getwhiteGrave() { return whiteGrave; }
	public Set<Piece> getBlackActive() { return blackActive; }
	public Set<Piece> getBlackGrave() { return blackGrave; }

/*	public String toString() {
		StringBuffer b = new StringBuffer();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board[i][j] != null) {
					b.append(board[i][j].toString() + ' ');
				} else {
					b.append("- ");
				}
				if (j == 7) {
					b.append('\n');
				}
			}
			System.out.println();
		}
		return b.toString();
	} */

	//Returns a String representation of the board, defaults to White's POV
	public String toString() {
		if (turn) {
			return toStringWhite();
		} else {
			return toStringBlack();
		}
	}

	//Returns a String representation of the board from White's POV, i.e. 
	// White is on the bottom of the board String.
	public String toStringWhite() {
		StringBuffer b = new StringBuffer();
		for (int i = 7; i >= 0; i--) {
			for (int j = 7; j >= 0; j--) {
				if (board[i][j] != null) {
					b.append(board[i][j].toString() + ' ');
				} else {
					b.append("- ");
				}
			}
			b.append(i + "\n");
		}
		b.append("7 6 5 4 3 2 1 0\n");
		return b.toString();
	}

	//Returns a String representation of the board from Black's POV, i.e. 
	// Black is on the bottom of the board String.
	public String toStringBlack() {
		StringBuffer b = new StringBuffer();
		b.append("  0 1 2 3 4 5 6 7\n");
		for (int i = 0; i < 8; i++) {
			b.append(i + " ");
			for (int j = 0; j < 8; j++) {
				if (board[i][j] != null) {
					b.append(board[i][j].toString() + ' ');
				} else {
					b.append("- ");
				}
			}
			b.append('\n');
		}
		return b.toString();
	}

	public void printBoard() {
		System.out.println(toString());
	}

	public boolean end() {
		for (Piece piece : blackGrave) {
			if (piece.toString().equals("K")) {
				System.out.println("White wins!");
				return true;
			}
		}
		for (Piece piece : whiteGrave) {
			if (piece.toString().equals("K")) {
				System.out.println("Black wins!");
				return true;
			}
		}
		return false;
	}

	public boolean place(String p, boolean color, int dx, int dy) {
		if (board[dy][dx] != null) {
			System.err.println("Can't place there.");
			return false;
		}
		if (color) {
			for (Piece piece : whitePlaceable) {
				if (piece.toString().equals(p)) {
					board[dy][dx] = piece;
					turn = !turn;
					return true;
				}
			}
			return false;
		} else {
			for (Piece piece : blackPlaceable) {
				if (piece.toString().equals(p)) {
					board[dy][dx] = piece;
					turn = !turn;
					return true;
				}
			}
			return false;
		}
	}

	public void run() {
		while (!end()) {
//			System.out.println("Would you like to place a piece? Y or N");
//			String answer = scan.next();
			boolean worked = false;
			while (!worked) {
/*				if (answer.equals("Y")) {
					String p = scan.next();
					int dx = scan.nextInt();
					int dy = scan.nextInt();
					worked = place(p, turn, dx, dy);
				} else {
					int x = scan.nextInt();
					int y = scan.nextInt();
					int dx = scan.nextInt();
					int dy = scan.nextInt();
					worked = move(turn, x, y, dx, dy);
				} */
				int x = scan.nextInt();
				int y = scan.nextInt();
				int dx = scan.nextInt();
				int dy = scan.nextInt();
				try {
					worked = move(turn, x, y, dx, dy);
				} catch (InvalidMoveException im) {
					System.err.println("You entered an invalid move.");
				} catch (NotYourTurnException nyt) {
					System.err.println("It's not your turn.");
				}		
			}
			printBoard();
		}
	}

	public static void main(String[] args) {
		Random r = new Random();
		Board b = new Board();
		Scanner scan = new Scanner(System.in);

		b.beginning();
		b.printBoard();

		b.run();
	} 
}

/*System.out.println("Would you like to place a piece? Y or N.");
			String answer = scan.next();
			if (answer.charAt(0) == 'y' || answer.charAt(0) == 'y') {

			} else {
				int x = scan.nextInt();
				int y = scan.nextInt();
				int dx = scan.nextInt();
				int dy = scan.nextInt();
				move(x, y, dx, dy);
			} */

/* 	class Player {

		protected boolean color;
		protected Set<Piece> placeable;

		public Player(boolean color) {
			this.color = color;
			this.placeable = new HashSet<Piece>();
		}

		public void move(Scanner scan) {
			int x = scan.nextInt();
			int y = scan.nextInt();
			if (board[y][x] == null || board[y][x].boolColor() != color) {
				System.err.println("Invalid move.");
				return;
			}
			int dx = scan.nextInt();
			int dy = scan.nextInt();
			move(x, y, dx, dy);
		}

		public Piece place(String p) {
			if (placeable.isEmpty()) {
				System.out.println("No pieces to place.");
				return null;
			} else {
				for (Piece piece : placeable) {
					if (piece.toString.equals(p)) {
						return piece;
					}
				}
				return null;
			}
		}
	} */


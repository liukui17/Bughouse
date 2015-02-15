import java.util.*;

public class Board {
	private Piece[][] board;
	private Set<Piece> whiteActive;
	private Set<Piece> whiteGrave;
	private Set<Piece> blackActive;
	private Set<Piece> blackGrave;

	public Board() {
		board = new Piece[8][8];
		whiteActive = new HashSet<Piece>();
		whiteGrave = new HashSet<Piece>();
		blackActive = new HashSet<Piece>();
		blackGrave = new HashSet<Piece>();
	}

	public void move(int x, int y, int dx, int dy) {
		if (!valid(x, y, dx, dy)) {
			System.out.println("Invalid Move!");
			return;
		}
		if (board[dy][dx] != null)
			capture(dx, dy);
		if (board[y][x].toString().equals("K") && dx - x == 2) {
			queenSide(x, y);
		} else if (board[y][x].toString().equals("K") && dx - x == -2) {
			kingSide(x, y);
		} else {
			board[dy][dx] = board[y][x];
			board[y][x] = null;
			board[dy][dx].updatePosition(dx, dy);
			board[dy][dx].updatePossibleMoves();
		}
	}

	private void kingSide(int x, int y) {
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
		} else {
			System.out.println("Can't castle.");
		}
	}

	private void queenSide(int x, int y) {
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
		} else {
			System.out.println("Can't castle.");
		}
	}

	private void capture(int x, int y) {
		boolean pcolor = board[y][x].boolColor();
		if (pcolor) {
			whiteGrave.add(board[y][x]);
		} else {
			blackGrave.add(board[y][x]);
		}
	}

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

	private boolean queenBlocked(int x, int y, int dx, int dy) {
		if (Math.abs(dx - x) == Math.abs(dy - y)) {
			return bishBlocked(x, y, dx, dy);
		} else {
			return rookBlocked(x, y, dx, dy);
		}
	}

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
				return false;
			}
			tx += incX;
			ty += incY;
		}
		return !(board[dy][dx] == null || board[dy][dx].boolColor() ^ board[y][x].boolColor());
	}

	private boolean bishBlocked(int x, int y, int dx, int dy) {
		int incX = (dx - x) / (Math.abs(dx - x));
		int incY = (dy - y) / (Math.abs(dy - y));
		int tx = x + incX;
		int ty = y + incY;
		while (tx != dx || ty != dy) {
			if (board[ty][tx] != null) {
				return false;
			}
			tx += incX;
			ty += incY;
		}
		return !(board[dy][dx] == null || board[dy][dx].boolColor() ^ board[y][x].boolColor());
	}
	
	private boolean knBlocked(int x, int y, int dx, int dy) {
		return !(board[dy][dx] == null || board[dy][dx].boolColor() ^ board[y][x].boolColor());
	}

	private boolean pawnBlocked(int x, int y, int dx, int dy) {
		if (x == dx) {
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

	public String toString() {
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
	}

	public void printBoard() {
		System.out.println(toString());
	}

	// for now, do 10 moves
	public void run(Scanner scan) {
		for (int i = 0; i < 10; i++) {
			int x = scan.nextInt();
			int y =scan.nextInt();
			int dx = scan.nextInt();
			int dy = scan.nextInt();
			move(x, y, dx, dy);
			printBoard();
		}
	}

	public static void main(String[] args) {
		Random r = new Random();
		Board b = new Board();
		Scanner scan = new Scanner(System.in);

		b.beginning();
		b.printBoard();

		b.run(scan);
	} 
}

/*	public void move(int x, int y, int dx, int dy) {
		if (!valid(x, y, dx, dy)) {
			throw new IllegalArgumentException("Invalid Move!");
		}
		boolean capture = !(board[x][y].boolColor() ^ board[dx][dy].boolColor());
		if (capture) {
			addToGrave(dx, dy);
		}
		board[dx][dy] = board[x][y];
		board[x][y] = null;
		board[dx][dy].updatePosition(dx, dy);
		board[dx][dy].updatePossibleMoves();
	}

	private void addToGrave(int x, int y) {
		if (board[x][y] == null) {
			throw new IllegalArgumentException();
		}
		boolean color = board[x][y].boolColor();
		if (color) {
			whiteGrave.put(board[x][y].getPosition(), board[x][y]);
		} else {
			blackGrave.put(board[x][y].getPosition(), board[x][y]);
		}
	}

	private boolean valid(int x, int y, int dx, int dy) {
		if (!inBounds(dx, dy) || !inBounds(x, y) || board[x][y] == null) {
			return false;
		} else {
			if (!board[x][y].isPossible(dx, dy)) {
				return false;
			} else {
				if (board[x][y].toString() == "K" || board[x][y].toString() == "H") {
					return !KKBlocked(x, y, dx, dy);
				} else if (board[x][y].toString() == "B") {
					return !bishBlocked(x, y, dx, dy);
				} else if (board[x][y].toString() == "R") {
					return !rookBlocked(x, y, dx, dy);
				} else if (board[x][y].toString() == "Q") {
					return !queenBlocked(x, y, dx, dy);
				} else {
					return !pawnBlocked(x, y, dx, dy);
				}
			}
		}
	}

	private boolean KKBlocked(int x, int y, int dx, int dy) {
		return !(board[x][y].boolColor() ^ board[dx][dy].boolColor());
	}

	private boolean pawnBlocked(int x, int y, int dx, int dy) {
		if (x == dx) {
			return board[dx][dy] != null;
		} else {
			return !(board[dx][dy] != null && 
				    (board[x][y].boolColor() ^ board[dx][dy].boolColor()));
		} 
	}

	private boolean queenBlocked(int x, int y, int dx, int dy) {
		return rookBlocked(x, y, dx, dy) || bishBlocked(x, y, dx, dy);
	}

	private boolean rookBlocked(int x, int y, int dx, int dy) {
		int tx = x;
		int ty = y;
		int incX = 0;
		int incY = 0;
		if (dx == x) {
			incY = (ty - dy) / Math.abs(ty - dy);
		} else {
			incX = (tx - dx) / Math.abs(tx - dx);
		}
		tx += incX;
		ty += incY;
		while (tx != dx ^ ty != dy) {
			if (board[tx][ty] != null) {
				return true;
			}
			tx += incX;
			ty += incY;
		}
		if (board[tx][ty] == null) {
			return false;
		} else {
			return !(board[x][y].boolColor() ^ board[dx][dy].boolColor());
		}
	}

	private boolean bishBlocked(int x, int y, int dx, int dy) {
		int tx = x;
		int ty = y;
		int incX = (tx - dx) / Math.abs(tx - dx);
		int incY = (ty - dy) / Math.abs(ty - dy);
		tx += incX;
		ty += incY;
		while (tx != dx && ty != dy) {
			if (board[tx][ty] != null) {
				return true;
			}
			tx += incX;
			ty += incY;
		}
		if (board[tx][ty] == null) {
			return false;
		} else {
			return !(board[x][y].boolColor() ^ board[dx][dy].boolColor());
		}
	} */
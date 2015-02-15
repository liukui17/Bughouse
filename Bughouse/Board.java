import java.util.*;

public class Board {
	private Piece[][] board;
	private Map<Point, Piece> whiteActive;
	private Map<Point, Piece> whiteGrave;
	private Map<Point, Piece> blackActive;
	private Map<Point, Piece> blackGrave;

	public Board() {
		board = new Piece[8][8];
		whiteActive = new HashMap<Point, Piece>();
		whiteGrave = new HashMap<Point, Piece>();
		blackActive = new HashMap<Point, Piece>();
		blackGrave = new HashMap<Point, Piece>();
	}

	public void move(int x, int y, int dx, int dy) {
		if (!valid(x, y, dx, dy)) {
			throw new IllegalArgumentException("Invalid Move!");
		}
		board[dy][dx] = board[y][x];
		board[y][x] = null;
		board[dy][dx].updatePosition(dx, dy);
		board[dy][dx].updatePossibleMoves();
	}

	private boolean valid(int x, int y, int dx, int dy) {
		if (!inBounds(x, y) || !inBounds(dx, dy) || board[y][x] == null || !board[y][x].isPossible(dx, dy)) {
			return false;
		} else {
			if (board[y][x].toString().equals("K") || board[y][x].toString().equals("H")) {
				return !kkBlocked(x, y, dx, dy);
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
		int tx = x + incX;
		int ty = y + incY;
		if (dx - x != 0) {
			incX = (dx - x) / (Math.abs(dx - x));
		} else {
			incY = (dy - y) / (Math.abs(dy - y));
		}
		while (tx != dx || ty != dy) {
			if (board[ty][tx] != null) {
				return false;
			}
			tx += incX;
			ty += incY;
		}
		return board[dy][dx] != null && !(board[dy][dx].boolColor() ^ board[y][x].boolColor());
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
		return board[dy][dx] != null && !(board[dy][dx].boolColor() ^ board[y][x].boolColor()); 
	}
	
	private boolean kkBlocked(int x, int y, int dx, int dy) {
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
			board[6][i] = new Pawn(i, 6, false);
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
		}
	}

	private boolean inBounds(int x, int y) {
		return (0 <= x) && (x <= 7) && (0 <= y) && (x <= 7);
	}

	public String getBoard() {
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
		System.out.println(getBoard());
	}

	public static void main(String[] args) {
		Random r = new Random();
		Board b = new Board();
		Scanner scan = new Scanner(System.in);

		b.beginning();
		b.printBoard();

/*		int x = scan.nextInt();
		int y = scan.nextInt();
		int dx = scan.nextInt();
		int dy = scan.nextInt();

		System.out.println(b.board[y][x].getPosition().toString());
		b.move(x, y, dx, dy);
		b.printBoard(); */

		for (int i = 0; i < 10; i++) {
			int x = scan.nextInt();
			int y = scan.nextInt();
			int dx = scan.nextInt();
			int dy = scan.nextInt();
			b.move(x, y, dx, dy);
			b.printBoard();
		} 
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
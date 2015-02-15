import java.util.ArrayList;

public class Knight extends Piece {

	private Point[] moves = {
			new Point(2, 1),
			new Point(2, -1),
			new Point(-2, 1),
			new Point(-2, -1),
			new Point(1, 2),
			new Point(1, -2),
			new Point(-1, 2),
			new Point(-1, -2)
		};

	public Knight(int x, int y, boolean color) {
		super(x, y, color);

		for (int i = 0; i < 8; i++) {
			possible.add(new Point(0, 0));
		}
		updatePossibleMoves();
	}

	public void updatePossibleMoves() {
		for (int i = 0; i < moves.length; i++) {
			possible.get(i).setPoint(position.getX() + moves[i].getX(), position.getY() + moves[i].getY());
		}
	}

	public boolean isPossible(int x, int y) {
		int absX = Math.abs(position.getX() - x);
		int absY = Math.abs(position.getY() - y);
		return (absX == 1 && absY == 2) || (absX == 2 && absY == 1);
	}

	public String toString() { return "H"; }

}
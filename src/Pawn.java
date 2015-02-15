import java.util.ArrayList;

public class Pawn extends Piece {

	private Point[] moves;
	protected boolean isFirst;

	public Pawn(int x, int y, boolean color) {
		super(x, y, color);
		isFirst = true;

		moves = new Point[3];
		if (color) {
			moves[0] = new Point(1, 1);
			moves[1] = new Point(0, 1);
			moves[2] = new Point(-1, 1);
		} else {
			moves[0] = new Point(-1, -1);
			moves[1] = new Point(0, -1);
			moves[2] = new Point(1, -1);
		}

		for (int i = 0; i < 3; i++) {
			possible.add(new Point(0, 0));
		}

		updatePossibleMoves();
		if (color) {
			possible.add(new Point(position.getX(), position.getY() + 2));
		} else {
			possible.add(new Point(position.getX(), position.getY() - 2));
		} 
	}

	public void updatePosition(int x, int y) {
		int oldx = position.getX();
		int oldy = position.getY();
		super.updatePosition(x, y);
		if (possible.size() == 4 && (position.getX() != oldx || position.getY() != oldy)) {
			possible.remove(3);
		}
		isFirst = false;
	}

	public void updatePossibleMoves() {
		for (int i = 0; i < moves.length; i++) {
			possible.get(i).setPoint(position.getX() + moves[i].getX(), position.getY() + moves[i].getY());
		}
	} 

	public boolean isPossible(int x, int y) {
		if (color) {
			return Math.abs(position.getX() - x) <= 1  && 
				   (position.getY() - y == -1 || 
				   (isFirst && position.getY() - y == -2));
		} else {
			return Math.abs(position.getX() - x) <= 1 &&
			       (position.getY() - y == 1 || 
				   (isFirst && position.getY() - y == 2));
		}
	}

	public String toString() { return "P"; }

}
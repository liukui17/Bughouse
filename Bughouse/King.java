import java.util.ArrayList;

public class King extends Piece {

	private Point[] moves = {
			new Point(1, 0),
			new Point(-1, 0),
			new Point(0, 1),
			new Point(0, -1),
			new Point(1, 1),
			new Point(1, -1),
			new Point(-1, 1),
			new Point(-1, -1)
		};

	public King(int x, int y, boolean color) {
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
		int xd = Math.abs(position.getX() - x);
		int yd = Math.abs(position.getY() - y);
		return (xd == 1 && yd == 1) || 
			   (xd == 1 && yd == 0) ||
			   (xd == 0 && yd == 1);
	}

	public String toString() { return "K"; }

}
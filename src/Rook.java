import java.util.ArrayList;

public class Rook extends Piece {

	public Rook(int x, int y, boolean color) {
		super(x, y, color);

		for (int i = 0; i < 14; i++) {
			possible.add(new Point(0, 0));
		}
		updatePossibleMoves();
		imagePath = imagePath + "_rook.png";
	}

	public void updatePossibleMoves() {
		for (int i = 0; i < position.getX(); i++) {
			possible.get(i).setPoint(i, position.getY());
		}
		for (int i = position.getX() + 1; i < 8; i++) {
			possible.get(i - 1).setPoint(i, position.getY());
		}
		for (int i = 0; i < position.getY(); i++) {
			possible.get(i + 7).setPoint(position.getX(), i);
		}
		for (int i = position.getY() + 1; i < 8; i++) {
			possible.get(i + 6).setPoint(position.getX(), i);
		}
	}

	public boolean isPossible(int x, int y) {
		return (position.getX() == x) ^ (position.getY() == y);
	}

	public String toString() { return "R"; }

}
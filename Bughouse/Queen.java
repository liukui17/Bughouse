
public class Queen extends Piece {

	public Queen(int x, int y, boolean color) {
		super(x, y, color);
	}

	public boolean isPossible(int x, int y) {
		return ((position.getX() == x) ^ (position.getY() == y)) || 
			(Math.abs(position.getX() - x) == Math.abs(position.getY() - y));
	}

	public String toString() { return "Q"; }

}

public class Bishop extends Piece {

	public Bishop(int x, int y, boolean color) {
		super(x, y, color);
		imagePath = imagePath + "_bishop.png";
	}

	public boolean isPossible(int x, int y) {
		return Math.abs(position.getX() - x) == Math.abs(position.getY() - y);
	}

	public String toString() { return "B"; }

}
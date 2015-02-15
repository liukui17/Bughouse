
public class Queen extends Piece {

	public Queen(int x, int y, boolean color) {
		super(x, y, color);
		imagePath = imagePath + "_queen.png";
	}

	public boolean isPossible(int x, int y) {
		System.out.println(position.getX() - x);
		System.out.println(position.getY() - y);
		return ((position.getX() == x) ^ (position.getY() == y)) || 
			   (Math.abs(position.getX() - x) == Math.abs(position.getY() - y));
	}

	public String toString() { return "Q"; }

}
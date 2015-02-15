import java.util.ArrayList;

public class Piece {

	protected Point position;
	protected boolean color;
	protected ArrayList<Point> possible;
	protected boolean hasMoved;

	public Piece(int x, int y, boolean color) {
		position = new Point(x, y);
		this.color = color;
		possible = new ArrayList<Point>();
		hasMoved = false;
	}

	public void updatePosition(int x, int y) {
		for (Point p : possible) {
			if (p.getX() == x && p.getY() == y) {
				position.setPoint(x, y);
				break;
			}
		}
		hasMoved = true;
	} 

	public boolean moved() { return hasMoved; }
	public void updatePossibleMoves() {}
	public String toString() { return "?"; }
	public boolean boolColor() { return color; }
	public String getColor() { return color ? "White" : "Black"; }
	public ArrayList<Point> getPossibleMoves() { return possible; }
	public Point getPosition() { return position; }
	public boolean isPossible(int x, int y) { return true; }

}
import javax.swing.*; // for JFrame
import java.awt.*; // for Grid Layout
import java.awt.event.*; // for events

import java.util.*;

public class Gui {
	public static void main(String[] args) {
		Gui gui = new Gui();
	}
	
	private JFrame frame;
	private Board game;
	
	// constructor
	public Gui() {
		frame = new JFrame(); // make frame
		
		game = new Board(); // construct board
		game.beginning(); // add the pieces
		
		// set-up frame
		frame.setTitle("BugHouse");
		frame.setSize(800, 800);
		frame.setLayout(new FlowLayout());
		frame.setLocation(50, 100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// add board to frame
		JPanel buttons = new JPanel();
		buttons.setSize(300, 300);
		buttons.setLayout(new GridLayout(8,8));
		
		Piece[][] boardArray = game.getBoard();
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				JButton space;
				if (boardArray[i][j] != null) {
					space = new ChessButton(i, j, boardArray[i][j].toString());
				} else {
					space = new ChessButton(i, j, "");
				}
				// if (space[i])
				space.addActionListener(new InputListener());
				buttons.add(space);
			}
		}
		
		Scanner scan = new Scanner(System.in);
		
//		for (int i = 0; i < 10; i++) {
//			int x = scan.nextInt();
//			int y = scan.nextInt();
//			int dx = scan.nextInt();
//			int dy = scan.nextInt();
//			game.move(x, y, dx, dy);
//			boardArray = game.getBoard();
//		} 
		
		frame.add(buttons);
		      
		frame.setVisible(true);
	}
	
	private class InputListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			ChessButton source = (ChessButton) event.getSource();
			System.out.println(source.x + " " + source.y);
		}
	}
	
	public class ChessButton extends JButton{
		int x;
		int y;
		// String display;
		// Piece piece;
		// JButton button;
		
		public ChessButton(int x, int y, String display) {
			super(display);
			this.x = x;
			this.y = y;
			// this.display = display;
			// this.button = button;
			// this.piece = piece;
		}
	}
}
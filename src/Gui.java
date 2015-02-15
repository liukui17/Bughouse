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
		
		// set-up frame
		frame.setTitle("BugHouse");
		frame.setSize(800, 800);
		frame.setLayout(new GridLayout(8, 8));
		frame.setLocation(50, 100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// add board to frame
		JPanel board = new JPanel();
		board.setLayout(new GridLayout(8,8));
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				JButton space = new JButton(i + ", " + j);
				board.add(space);
			}
		}
		
		frame.add(board);
		      
		frame.setVisible(true);
	}
}
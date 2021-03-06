import java.net.*;
import java.io.*;
import javax.swing.*; // for JFrame
import java.awt.*; // for Grid Layout
import java.awt.event.*; // for events
import java.util.*;

public class BugHouseServer {
	public static final Color LIGHT_SPACES = Color.WHITE;
	public static final Color DARK_SPACES = Color.YELLOW;
	public static final Color SELECTED_PIECE = Color.PINK;
	
	private JFrame frame;
	private JPanel buttons;
	private JLabel turn;
	
	private Board game;
	
	private boolean moving;
	private ChessButton prev;

	protected boolean color;
	protected ServerSocket server;
	protected Socket clientSocket;
	protected PrintWriter out;
	protected BufferedReader in;
	protected BufferedReader stdIn;

	public static void main(String[] args) 
			throws IOException, InvalidMoveException, UnknownHostException, NotYourTurnException {
		int portNum = Integer.parseInt(args[0]);
		BugHouseServer gui = new BugHouseServer(portNum);

		String input = "";
		while (true) {
			input = gui.in.readLine();
			System.out.println(input);
			String[] movesStr = input.split(" ");
			int[] movesInt = {Integer.parseInt(movesStr[0]),
							  Integer.parseInt(movesStr[1]),
							  Integer.parseInt(movesStr[2]),
							  Integer.parseInt(movesStr[3])};
			gui.game.move(movesInt[0], movesInt[1], movesInt[2], movesInt[3]);
			gui.drawBoard();			
		} 
	}
	
	// constructor
	public BugHouseServer(int port) 
			throws IOException, UnknownHostException {
		color = true;
		server = new ServerSocket(port);
		clientSocket = server.accept();
		out = new PrintWriter(clientSocket.getOutputStream(), true);
		in = new BufferedReader(
			new InputStreamReader(clientSocket.getInputStream()));
//		stdIn = new BufferedReader(new InputStreamReader(System.in));

		frame = new JFrame(); // make frame

		game = new Board(); // construct board
		game.beginning(); // add the pieces
		
		turn = new JLabel(game.turn ? "white" : "black"); // make turn indicator
		
		moving = false;
		prev = null;
		
		// set-up frame
		frame.setTitle("BugHouse Host");
		frame.setSize(800, 800);
//		frame.setLayout(new FlowLayout());
		frame.setLocation(50, 100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// add board to frame
		buttons = new JPanel();
		buttons.setSize(300, 300);
		buttons.setLayout(new GridLayout(8,8));
		
		frame.add(turn);
		
		drawBoard();  
		
		frame.setVisible(true);
	}
	
	public void drawBoard() {
		buttons.removeAll();
		frame.remove(turn);
		
		Piece[][] boardArray = game.getBoard();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				JButton space;
				if (boardArray[i][j] != null) {
					space = new ChessButton(i, j, boardArray[i][j], new ImageIcon(boardArray[i][j].imagePath));
				} else {
					space = new ChessButton(i, j, null, "");
				}
				if ((i % 2 == 0 && j % 2 != 0) || (i % 2 != 0 && j % 2 == 0)) {
					space.setBackground(DARK_SPACES);
				} else {
					space.setBackground(LIGHT_SPACES);
				}
				space.addActionListener(new InputListener());
				buttons.add(space);
			}
		}
		turn = new JLabel((game.turn) ? "white" : "black"); // change turn indicator
		
		frame.add(turn);
		
		changeBorder(game.turn);
		
		frame.add(buttons, BorderLayout.CENTER);
		
		frame.revalidate();
	}
	
	private void changeBorder(Boolean turn) {
		Color color = turn ? Color.WHITE : Color.BLACK;
		
		JPanel west = new JPanel();
		west.setBackground(color);
		frame.add(west, BorderLayout.WEST);
		
		JPanel north = new JPanel();
		north.setBackground(color);
		frame.add(north, BorderLayout.NORTH);
		
		JPanel east = new JPanel();
		east.setBackground(color);
		frame.add(east, BorderLayout.EAST);
		
		JPanel south = new JPanel();
		south.setBackground(color);
		frame.add(south, BorderLayout.SOUTH);
	}

	private class InputListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			ChessButton source = (ChessButton) event.getSource();
			System.out.println(source.x + " " + source.y);
			
			if (source.piece != null && !moving) { // select
				source.setBackground(SELECTED_PIECE);
				moving = !moving;
				prev = source;
			} else {
				if (prev != null && source.piece != null && source.piece.equals(prev.piece)) { // deselect
					// moving = !moving;
					// prev = null;
					deselect(source);
					
				} else if (prev != null) {
					try { 
						if (game.move(prev.y, prev.x, source.y, source.x)) { // move
							//moving = !moving;
							deselect(source);
							if (game.canPromote(source.y, source.x)) {
								game.promotion(game.getBoard()[source.x][source.y].boolColor(), source.y, source.x, 
											   JOptionPane.showInputDialog(null, "What would you like to promote to?"));
							}
							drawBoard();
							out.println(prev.x + " " + prev.y + " " + source.x + " " + source.y);
						}
					} catch (InvalidMoveException e) { // invalid move
						JOptionPane.showMessageDialog(null, "Invalid Move. Try Again");
						deselect(prev);
					} catch (NotYourTurnException e) { // not your turn, deselect
						JOptionPane.showMessageDialog(null, "Not your turn. Try Again");
						// moving = !moving;
						deselect(prev);
						// prev = null;
					} catch (Exception e) { // unknown exception
						deselect(prev);
						JOptionPane.showMessageDialog(null, "Unknown exception: REALLY BAD");
					}
				} 
			}
		}
		
		private void deselect(ChessButton target) {
			moving = !moving;
			if ((target.x % 2 == 0 && target.y % 2 != 0) || 
				(target.x % 2 != 0 && target.y % 2 == 0)) {
				target.setBackground(DARK_SPACES);
			} else {
				target.setBackground(LIGHT_SPACES);
			}
			prev = null;
		}
	}

	
	public class ChessButton extends JButton{
		int x;
		int y;
		Piece piece;
		
		public ChessButton(int x, int y, Piece piece, Icon display) {
			super(display);

			this.x = x;
			this.y = y;
			this.piece = piece;
		}
		
		public ChessButton(int x, int y, Piece piece, String display) {
			super(display);

			this.x = x;
			this.y = y;
			this.piece = piece;
		}
	} 
}
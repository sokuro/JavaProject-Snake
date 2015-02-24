package snake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.*;

import javax.swing.*;

/*
 * The game field is set into a JFrame frame.
 */

class SnakeFrame extends JFrame {
	
	//JFrame id
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		
		//the entry panel #1
		ImageIcon iconEntry = new ImageIcon(SnakeGame.class.getResource("/resources/SnakeWin.jpeg"));
		JOptionPane.showMessageDialog(null, "Welcome to the game SNAKE\n \n"
				+ "Hit ENTER to start the game", null, JOptionPane.INFORMATION_MESSAGE, iconEntry);
		
		//the entry panel #2, changing the color of the font
		UIManager.put("OptionPane.messageForeground", Color.RED);
		JOptionPane.showMessageDialog(null, "Eat the reds!!!", null, JOptionPane.INFORMATION_MESSAGE);
		
		//font color returning to black
		UIManager.put("OptionPane.messageForeground", Color.BLACK);
		
		
		//building an object of the SnakeGame class
		final SnakeGame game = new SnakeGame();
				
		//name and size of the main frame
		JFrame frame = new JFrame("Snake Game");
		frame.setSize(800, 800);
		
		/*
		 * Toolkit class as an abstract superclass of all actual 
		 * implementations of the Abstract Window Toolkit. Subclasses
		 * of it are used to bind the various components to particular
		 * native toolkit implementations.
		 */
		Toolkit newKit = frame.getToolkit();
		
		/*
		 * the dimension class encapsulates the width and height of
		 * a component (in integer precision) in a single object. 
		 */
		Dimension screenSize = newKit.getScreenSize();
		
		//the location of the frame in the middle of the screen
		frame.setLocation((screenSize.width / 2 - frame.getWidth() / 2), 
				(screenSize.height / 2 - frame.getHeight() / 2));
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//menu + button "File"
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_F); //shortcut 
		frame.setJMenuBar(menuBar);
		menuBar.add(file);
		
		//menu + button "Help"
		JMenu help = new JMenu("Help");
		help.setMnemonic(KeyEvent.VK_H);
		frame.setJMenuBar(menuBar);
		menuBar.add(help);
		
		//menu + about-button for short infos about the game
		JMenuItem about = new JMenuItem("About");
		help.add(about);
		about.setMnemonic(KeyEvent.VK_A); //shortcut (ALT + A)
		about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				JOptionPane.showMessageDialog(null, "The goal is to eat "
						+ "all the reds.\nMove the snake using the arrow\n"
						+ "keys. Beware reaching the borders!!!\n"
						+ "And don't eat yourself!!!\n"
						+ "", "Karol says:", 
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		/* 
		 * menu + File-button: new game. Using the ActionListener as
		 * an anonymous class to bind the button to start a new game.
		 * Using this I can declare and instantiate a class at the 
		 * same time, without naming it. It allows to define a one-
		 * shot class exactly where it is needed. (And it makes the 
		 * code shorter :-) ..but it took me some time to under-
		 * stand it right.
		*/ 
		JMenuItem newGame = new JMenuItem("New Game");
		file.add(newGame);
		newGame.setMnemonic(KeyEvent.VK_N);	//shortcut (ALT + F)
		newGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				JOptionPane.showMessageDialog(null, "New Game");
				SnakeGame.th.interrupt();	//small BUG (but works!)
				game.gameStart();
			}
		});
		
		/*
		 * menu + File-button: exit the game. Using the ActionListener as
		 * an anonymous class to bind the button to exit the game.
		*/
		JMenuItem exit = new JMenuItem("Exit");
		file.add(exit);
		exit.setMnemonic(KeyEvent.VK_X); //shortcut (ALT + X)
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				JOptionPane.showMessageDialog(null, "Game Over!");
				System.exit(0); 
			}
		});
		
		//frame uses the object game of the SnakeGame class as its content.
		frame.add(game);
		
		//adding the KeyListener Interface (anonymous class) 
	    frame.addKeyListener(new KeyAdapter() { 
	    	public void keyPressed(KeyEvent event) {
	    		game.keyPressed(event);
	    		}
	    });
	    
	    //the size of the frame can't be resized
	    frame.setResizable(false); 
	    
	    //the window is sized to fit the preferred size and layouts of its subcomponents
		frame.pack();
		
		//boolean for the drawing of the frame
		frame.setVisible(true);
	}
}

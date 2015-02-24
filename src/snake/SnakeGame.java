package snake;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class SnakeGame extends JPanel {
	
	//constants and instance variables
	private static final long serialVersionUID = 1L; //JPanel id
	private static final int SIZE = 15;		//constant for size
	private static final int SPEED = 100;	//speed (ms)
	private final Random rand = new Random();	//for random position if reds
	private Snake snake;	//declaring snake object
	private ArrayList<Point> red = new ArrayList<Point>();	//reds as points
	private int lastPressedKey = 0;	//init for the keyEvent method
	static Thread th;		//declaring thread
	Image snakeImage;		//background image
	
	/*
	 * implementing sound. The sound files must be saved in 
	 * their specific folder and this folder must be a
	 * part of the src folder of the package! The url3 must
	 * be static, so the static method snakeEat can be reached
	 * by the Snake class!
	 */
	URL url = this.getClass().getClassLoader().getResource("sound/BiteApple.wav");
	URL url2 = this.getClass().getClassLoader().getResource("sound/BreakGlass.wav");
	public static URL url3 = SnakeGame.class.getResource("/sound/EatSelf.wav");
	URL url4 = this.getClass().getClassLoader().getResource("sound/Applause.wav");
	URL url5 = this.getClass().getClassLoader().getResource("sound/Snoring.wav");
	
	AudioClip eat = Applet.newAudioClip(url);
	AudioClip border = Applet.newAudioClip(url2);
	public static AudioClip cannibal = Applet.newAudioClip(url3);
	AudioClip gameWin = Applet.newAudioClip(url4);
	AudioClip snoring = Applet.newAudioClip(url5);
	
	//constants for calculating the reached time
	public static long startTime, endTime, diffTime;
	
	//constructor
	public SnakeGame() {
		
		//load the background image and set it's position
		loadImage();	
		int width = snakeImage.getWidth(this);	
		int height = snakeImage.getHeight(this);
		System.out.println(width + height);
		setPreferredSize(new Dimension(width, height));
		
		//put the reds on the grid
		putRed();
		
		//start the game
		gameStart();
	}
	
	/*
	 * modifying thread for moving the snake. Used anonymous
	 * class for Thread (<- Runnable). Start calculating
	 * the time (ms) from the start of the thread. 
	 */
	public void gameStart() {
		snake = new Snake(0, 0, SIZE);
		th = new Thread(new Runnable() {
			public void run() {
				while (true) {
					game(); //refer to the game method
					try { //try->catch (satisfied with an exception 
						Thread.sleep(SPEED); //used speed constant	
						} catch (InterruptedException e) {}
					}
				}
			
			});
		th.start();	//starts the thread
		
		//start of measuring the time (thread)
		startTime = System.currentTimeMillis();
	}
	
	//method for loading the background image
	public void loadImage() {
		ImageIcon image = new ImageIcon("img/BlueSnake.jpg");
		snakeImage = image.getImage();
	}
	
	//the paint method draws the snake and the reds
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		
		//drawing the background image
		g.drawImage(snakeImage, 0, 0, null);
		
		//drawing the reds
		g2.setColor(Color.RED);
		for (int i = 0; i < red.size(); i++) {
			g2.fillArc(red.get(i).x, red.get(i).y, SIZE, SIZE, 0, 360);
		}
		g2.setColor(Color.GREEN);
		for (int j = 0; j < red.size(); j++) {
			g2.drawArc(red.get(j).x, red.get(j).y, SIZE, SIZE, 0, 360);
		}
		
		//drawing the snake of the Snake class
		snake.paint(g2);
	}
	
	//Using the arrow keys to send the snake in the direction of the arrow
	public void keyPressed(KeyEvent event) {
		switch (event.getKeyCode()) {
			
			case KeyEvent.VK_PAUSE:
				snake.setDirection(Snake.D_STATIC);
				gamePaused();
				break;
		
			case KeyEvent.VK_UP:
			if (lastPressedKey != KeyEvent.VK_DOWN) {	//the key is no longer down
				snake.setDirection(Snake.D_UP);			//set direction
				lastPressedKey = event.getKeyCode();	//get the last pressed key
			}
			break;
			case KeyEvent.VK_DOWN:
			if (lastPressedKey != KeyEvent.VK_UP) {
				snake.setDirection(Snake.D_DOWN);
				lastPressedKey = event.getKeyCode();
			}
			break;
			case KeyEvent.VK_LEFT:
			if (lastPressedKey != KeyEvent.VK_RIGHT) {
				snake.setDirection(Snake.D_LEFT);
				lastPressedKey = event.getKeyCode();
			}
			break;
			case KeyEvent.VK_RIGHT:
			if (lastPressedKey != KeyEvent.VK_LEFT) {
				snake.setDirection(Snake.D_RIGHT);
				lastPressedKey = event.getKeyCode();
			}
			break;
		}
		if (event.getKeyCode() == KeyEvent.VK_ESCAPE)
		System.exit(0);
	}
	
	//Random generating of all reds
	public void putRed() {
		int x = 0, y = 0;
		for (int i = 0; i < 15; i++) {
			x = SIZE * rand.nextInt(40);
			y = SIZE * rand.nextInt(40);
			red.add(new Point(x, y));
		}
	}
	
	//method for moving the snake using conditions
	public void game() {
		
		//the move method of the Snake class
		Point p = snake.move();
		
		//Check the border
		if (p.x < 0 || p.x > 50*SIZE) borderReached();//left & right border
		else if (p.y < 0 || p.y > 50*SIZE) borderReached(); //bottom & up border
		
		//Eat reds and grow
		else {
			for (int i = 0; i < red.size(); i++) { 
				if (red.get(i).x == p.x && red.get(i).y == p.y) {
					red.remove(p);	//remove eaten red
					snake.add();	//add one point to the snake's body
					eat.play();		//play sound by eating
				}
				
				//condition: all reds eaten?
				if (red.size() == 0) {	//all eaten? -> stop the snake
					snake.setDirection(Snake.D_STATIC);	
					
					//stopping thread, setting the end time for calculating
					endTime = System.currentTimeMillis();	
					gameWin();
				}
			}
		}
		//updating the GUI
		this.repaint();
	}
	
	//methods for the game breaking images using JPanel
	//first: game paused
	public void gamePaused() {
		snoring.play();
		ImageIcon icon = new ImageIcon(getClass().getResource("/resources/aztecSnake.gif"));
		JOptionPane.showMessageDialog(null, "..waiting", "GAME PAUSED", JOptionPane.INFORMATION_MESSAGE, icon);
	}
	
	//second: game over -> border reached
	public void borderReached() { 
		border.play();
		ImageIcon icon = new ImageIcon(getClass().getResource("/resources/SnakeDie.jpeg"));
		JOptionPane.showMessageDialog(null, "The border is reached..you die!", "GAME OVER", JOptionPane.ERROR_MESSAGE, icon);
		System.exit(0);
	}

	//static method snakeEat to be reached from the Snake class.
	//third: game over -> snake eats itself
	public static void snakeEat() {
		cannibal.play();
		ImageIcon icon = new ImageIcon(SnakeGame.class.getResource("/resources/SnakeSelf.jpeg")); 
		JOptionPane.showMessageDialog(null, "Cannibal snake..you die!", "GAME OVER", JOptionPane.ERROR_MESSAGE, icon);
		System.exit(0);
	}
	
	//fourth: game over -> player wins
	public void gameWin() {
		gameWin.play();
		ImageIcon icon = new ImageIcon(getClass().getResource("/resources/SnakeWin.jpeg"));
		JOptionPane.showMessageDialog(null, "Congrats..you win!", "GAME OVER", JOptionPane.INFORMATION_MESSAGE, icon);
		diffTime = endTime - startTime;
		JOptionPane.showMessageDialog(null, diffTime, "The final time in ms", JOptionPane.INFORMATION_MESSAGE);
		System.exit(0);
	}
}
		
		
		



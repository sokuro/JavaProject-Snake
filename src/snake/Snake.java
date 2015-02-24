package snake;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

/**
 * Project for "Einführung in die Programmierung"
 * Karol S. Ugorcak 2015
 * ProjectName: "Snake"
 * 
 * Plays the game of Snake. It draws a snake on the screen, sets randomly
 * red spots, symbolizing apples, which need to be eaten by the snake. By
 * doing so the snake will grow in its tail. The user uses the arrow keys 
 * to move the snake and try to eat the food (-> reds). The snake dies if 
 * it runs into the boundary or itself.
 */

class Snake {
	
	/*
	 * constants and instance variables. The constants are used also
	 * outside this class. Assigning values to each variable.
	 */
	static final int D_STATIC = 0;
	static final int D_UP = 1;
	static final int D_DOWN = 2;
	static final int D_LEFT = 3;
	static final int D_RIGHT = 4;
	
	/*
	 * building the body of the snake. 
	*/
	private ArrayList<Point> body = new ArrayList<Point>();
	private int size; 
	private int direction = D_STATIC; 
	
	/*
	 * initializing of the constructor. Grouping the row a column position
	 * into a pair for treating them as a single object.
	 */
	public Snake(int x0, int y0, int sz) {
		size = sz;
		int x = x0 * sz;	
		int y = y0 * sz;
		body.add(new Point(x, y));
	}
	
	//the direction method for the snake method move
	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	//the add-method adding the new position of the snake
	public void add() {
		body.add(0, new Point(body.get(0).x, body.get(0).y));
	}
	
	/*
	 * the delete-method to cut the snake's tail, so the snake will not have 
	 * a tail from the beginning of the building of the array.
	 * Sublist returns a view of the portion of this list between the specified 
	 * fromIndex, inclusive, and toIndex, exclusive. 
	 */
	public void delete() {
		for (int i = 1; i < body.size(); i++) {
			if (body.get(i).equals(body.get(0))) {
				body.removeAll(body.subList(i, body.size())); 
			}
		}
	}

	//painting the snake
	public void paint(Graphics2D g) {
		for (Point p : body) {
			g.setColor(Color.GREEN);
			g.fillArc(p.x, p.y, size, size, 0, 500);
			g.setColor(Color.BLUE);
			g.drawArc(p.x, p.y, size, size, 0, 500);
		}
	}
	
	//method to check if the coordinates are part of the snake body
	public boolean checkPoint(int x, int y){
		for(int i = 0; i < this.body.size(); i++){
			if (body.get(i).x == x && body.get(i).y == y){
				return true;
			} 
		}
		return false;
	}
	
	/*
	 * the move method of the snake. Determining the position reached
	 * by moving in the specified direction.
	 */
	public Point move() {
		Point last = body.get(body.size() - 1);	//snake's tail
		Point temP = last;	//temp position

		if (direction != D_STATIC) {
			body.remove(0);	
			switch (direction) {
			case D_UP: 
				temP = new Point(last.x, last.y - size);
				break;
			case D_DOWN:
				temP = new Point(last.x, last.y + size);
				break;
			case D_LEFT:
				temP = new Point(last.x - size, last.y);
				break;
			case D_RIGHT:
				temP = new Point(last.x + size, last.y);
				break;
			}
			
			//check if the snake is eating itself
			if (this.checkPoint(temP.x, temP.y) == true){	
				SnakeGame.snakeEat(); //ref -> static method 
			}
			body.add(temP);
			this.delete();
		}
		return temP;
	}
}

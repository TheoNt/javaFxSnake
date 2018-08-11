import java.awt.Point;
import java.util.LinkedList;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class Snake extends Application{

	private int stageWidth = 600, stageHeight = 600;
	private GraphicsContext gc;
	private int headX = 58, headY = 20;
	private int snakeWidth = 10, snakeHeight = 10, foodWidth = 10, foodHeight = 10;
	private boolean goUp = false, goDown = false, goRight = true, goLeft = false; 
	private LinkedList<Point> snake;
	private Point food, head, tail;
	private boolean gameOver = false;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Snake in JavaFX");
		Group root = new Group();
		Scene scene = new Scene(root, Color.BLACK);
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				readKeys(event);
			}
		});
		primaryStage.setScene(scene);
		Canvas canvas = new Canvas(stageWidth, stageHeight);
		root.getChildren().add(canvas);
		gc = canvas.getGraphicsContext2D();
		
		primaryStage.show();
		
		snake = new LinkedList<Point>();
		snake.add(new Point(headX, headY));
		snake.add(new Point(headX-1, headY));
		snake.add(new Point(headX-2, headY));
		snake.add(new Point(headX-3, headY));
		food = new Point(3, 3);
		
		new AnimationTimer() {
			long lastTime = 0;
			@Override
			public void handle(long now) {
				if(now - lastTime >= 400_000_000){
				clearScreen();
				updateSnake();
				drawSnake();
				collision();
				updateFood();
				lastTime = now;
				}
				if(gameOver){
					gameOverScreen();
				}
			}
		}.start();
	}
	
	private void updateSnake(){
		head = new Point(snake.getFirst().x, snake.getFirst().y);
		if(goUp){
			head.y--;
			}
		if(goDown){
			head.y++;
			}
		if(goLeft){
			head.x--;
			}
		if(goRight){
			head.x++;
			}
		snake.push(head);
		snake.removeLast();
		System.out.println(snake.size());
		
		//System.out.println("X = " + snake.getFirst().x * 10 + " " + "Y = " + snake.getFirst().y * 10);
	}
	
	private void drawSnake(){
		for(Point p : snake){
			gc.fillRect(p.x * 10, p.y * 10, snakeWidth, snakeHeight);
			//System.out.println("X = " + p.x * 10 + " " + "Y = " + p.y * 10 + " " + snake.size());
			}
	}
	
	private void collision(){
		head = snake.removeFirst();
		if(snake.contains(head)){
			System.out.println(true);
		}
		snake.addFirst(head);
		if(head.x >= 59){
			head = new Point(0, head.y);
			snake.removeLast();
			snake.addFirst(head);
		}
		if(head.x <= 0 && goLeft){
			head = new Point(59, head.y);
			snake.removeLast();
			snake.addFirst(head);
		}
		if(head.y >= 59){
			head = new Point(head.x, 0);
			snake.removeLast();
			snake.addFirst(head);
		}
		if(head.y <= 0 && goUp){
			head = new Point(head.x, 59);
			snake.removeLast();
			snake.addFirst(head);
		}
	}
	
	private void updateFood(){
		gc.fillRect(food.x * 10, food.y * 10, foodWidth, foodHeight);
		head = snake.getFirst();
		if(food.equals(snake.getFirst())){
			Random newPointX = new Random();
			Random newPointY = new Random();
			int x, y;
			//System.out.println("foodX = " + x + "foodY = " + y);
			do {
				x = newPointX.nextInt(59);
				y = newPointY.nextInt(59);
			} while (snake.contains(new Point(x,y)));
			food.move(x,y);
			Point newHead = null;
			if(goUp){
				tail = new Point(snake.getLast().x, snake.getLast().y+1);
			}
			if(goDown){
				tail = new Point(snake.getLast().x, snake.getLast().y-1);
			}
			if(goLeft){
				tail = new Point(snake.getLast().x+1, snake.getLast().y);
			}
			if(goRight){
				tail = new Point(snake.getLast().x-1, snake.getLast().y);
			}
			snake.addLast(tail);
		}
	}
	
	private void gameOverScreen(){
		
	}
	
	private void clearScreen(){
		gc.clearRect(0, 0, stageWidth, stageHeight);
		gc.setFill(Color.WHITE);
	}
	
	private void readKeys(KeyEvent event){
		KeyCode key = event.getCode();
		switch (key) {
		case UP:
			if(!goDown){
				goUp = true;
				goDown = false;
				goLeft = false;
				goRight = false;
			}		
			break;
		case DOWN:
			if(!goUp){
			goUp = false;
			goDown = true;
			goLeft = false;
			goRight = false;
			}
			break;
		case LEFT:
			if(!goRight){
			goUp = false;
			goDown = false;
			goLeft = true;
			goRight = false;
			}
			break;
		case RIGHT:
			if(!goLeft){
			goUp = false;
			goDown = false;
			goLeft = false;
			goRight = true;
			}
			break;
		}
	}

}

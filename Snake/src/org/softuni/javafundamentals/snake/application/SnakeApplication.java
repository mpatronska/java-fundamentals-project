package org.softuni.javafundamentals.snake.application;
	
import org.softuni.javafundamentals.snake.utils.Constants;
import org.softuni.javafundamentals.snake.utils.Direction;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;


public class SnakeApplication extends Application {
	
	private Direction direction = Direction.RIGHT;
	
	private boolean isMoved = false;
	private boolean isApplicationRunning = false;
	
	private Timeline animation = new Timeline();
	
	private ObservableList<Node> snake;
	
	private Parent createContent() {
		Pane root = new Pane();
		root.setPrefSize(Constants.WIDTH, Constants.HEIGHT);
		
		Group snakeBody = new Group();
		snake = snakeBody.getChildren();
		
		Rectangle food = new Rectangle(Constants.BLOCK_SIZE, Constants.BLOCK_SIZE);
		food.setFill(Color.BLUE);
		food.setTranslateX((int)(Math.random() * (Constants.HEIGHT - Constants.BLOCK_SIZE)) / Constants.BLOCK_SIZE * Constants.BLOCK_SIZE);
		food.setTranslateY((int)(Math.random() * (Constants.HEIGHT - Constants.BLOCK_SIZE)) / Constants.BLOCK_SIZE * Constants.BLOCK_SIZE);
		
		KeyFrame frame = new KeyFrame(Duration.seconds(0.1), event -> {
			if (!isApplicationRunning) {
				return;
			}
			
			Node tail = (snake.size() > 1) ? snake.remove(snake.size() - 1) : snake.get(0);
			
			double tailX = tail.getTranslateX();
			double tailY = tail.getTranslateY();
			
			switch (direction) {
			case UP:
				tail.setTranslateX(snake.get(0).getTranslateX());
				tail.setTranslateY(snake.get(0).getTranslateY() - Constants.BLOCK_SIZE);
				break;
			case DOWN:
				tail.setTranslateX(snake.get(0).getTranslateX());
				tail.setTranslateY(snake.get(0).getTranslateY() + Constants.BLOCK_SIZE);
				break;
			case LEFT:
				tail.setTranslateX(snake.get(0).getTranslateX() - Constants.BLOCK_SIZE);
				tail.setTranslateY(snake.get(0).getTranslateY());
				break;
			case RIGHT:
				tail.setTranslateX(snake.get(0).getTranslateX() + Constants.BLOCK_SIZE);
				tail.setTranslateY(snake.get(0).getTranslateY());
				break;
			default:
				break;
			}
			
			isMoved = true;
			
			if (snake.size() > 1) {
				snake.add(0, tail);
			}
			
			//collision detection
			for (Node node : snake) {
				if (node != tail && tail.getTranslateX() == node.getTranslateX() &&
						tail.getTranslateY() == node.getTranslateY()) {
					//the snake hits its own body
					restartGame();
					break;
				}
			}
			
			if (tail.getTranslateX() < 0 || tail.getTranslateX() >= Constants.WIDTH ||
					tail.getTranslateY() < 0 || tail.getTranslateY() >= Constants.HEIGHT) {
				restartGame();
			}
			
			if (tail.getTranslateX() == food.getTranslateX() && tail.getTranslateY() == food.getTranslateY()) {
				food.setTranslateX((int)(Math.random() * (Constants.HEIGHT - Constants.BLOCK_SIZE)) / Constants.BLOCK_SIZE * Constants.BLOCK_SIZE);
				food.setTranslateY((int)(Math.random() * (Constants.HEIGHT - Constants.BLOCK_SIZE)) / Constants.BLOCK_SIZE * Constants.BLOCK_SIZE);
				
				Rectangle rectangle = new Rectangle(Constants.BLOCK_SIZE, Constants.BLOCK_SIZE);
				rectangle.setTranslateX(tailX);
				rectangle.setTranslateY(tailY);
				
				snake.add(rectangle);
			}
		});
		
		animation.getKeyFrames().add(frame);
		animation.setCycleCount(Timeline.INDEFINITE);
		
		root.getChildren().addAll(food, snakeBody);
		
		return root;
	}
	
	private void restartGame() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start(Stage primaryStage) {
		try {
//			BorderPane root = new BorderPane();
			Scene scene = new Scene(createContent());
//			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Snake");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

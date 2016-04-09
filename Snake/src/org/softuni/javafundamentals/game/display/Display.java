package org.softuni.javafundamentals.game.display;

import org.softuni.javafundamentals.game.snake.SnakeLauncher;
import org.softuni.javafundamentals.game.utils.GameUtils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Display {
	
	private SnakeLauncher snakeInstance;
	
	public Display(SnakeLauncher snakeInstance) {
		this.snakeInstance = snakeInstance;
	}
	
	public Parent createContent() {
		Pane root = new Pane();
		root.setPrefSize(GameUtils.WIDTH, GameUtils.HEIGHT);
		
		Group snakeBody = new Group();
		snakeInstance.setSnake(snakeBody.getChildren());
		
		Rectangle food = new Rectangle(GameUtils.BLOCK_SIZE, GameUtils.BLOCK_SIZE);
		food.setFill(Color.BLUE);
		food.setTranslateX((int)(Math.random() * (GameUtils.WIDTH - GameUtils.BLOCK_SIZE)) / GameUtils.BLOCK_SIZE * GameUtils.BLOCK_SIZE);
		food.setTranslateY((int)(Math.random() * (GameUtils.HEIGHT - GameUtils.BLOCK_SIZE)) / GameUtils.BLOCK_SIZE * GameUtils.BLOCK_SIZE);
		
		KeyFrame frame = new KeyFrame(Duration.seconds(0.3), event -> {
			if (!snakeInstance.isApplicationRunning()) {
				return;
			}
			
			boolean toRemove = snakeInstance.getSnake().size() > 1;
			
			Node tail = toRemove ? snakeInstance.getSnake().remove(snakeInstance.getSnake().size() - 1) : snakeInstance.getSnake().get(0);
			
			double tailX = tail.getTranslateX();
			double tailY = tail.getTranslateY();
			
			switch (snakeInstance.getDirection()) {
			case UP:
				tail.setTranslateX(snakeInstance.getSnake().get(0).getTranslateX());
				tail.setTranslateY(snakeInstance.getSnake().get(0).getTranslateY() - GameUtils.BLOCK_SIZE);
				break;
			case DOWN:
				tail.setTranslateX(snakeInstance.getSnake().get(0).getTranslateX());
				tail.setTranslateY(snakeInstance.getSnake().get(0).getTranslateY() + GameUtils.BLOCK_SIZE);
				break;
			case LEFT:
				tail.setTranslateX(snakeInstance.getSnake().get(0).getTranslateX() - GameUtils.BLOCK_SIZE);
				tail.setTranslateY(snakeInstance.getSnake().get(0).getTranslateY());
				break;
			case RIGHT:
				tail.setTranslateX(snakeInstance.getSnake().get(0).getTranslateX() + GameUtils.BLOCK_SIZE);
				tail.setTranslateY(snakeInstance.getSnake().get(0).getTranslateY());
				break;
			}
			
			snakeInstance.setMoved(true);
			
			if (toRemove) {
				snakeInstance.getSnake().add(0, tail);
			}
			
			//collision detection
			for (Node node : snakeInstance.getSnake()) {
				if (node != tail && tail.getTranslateX() == node.getTranslateX() &&
						tail.getTranslateY() == node.getTranslateY()) {
					//the snake hits its own body
					snakeInstance.restartGame();
					break;
				}
			}
			
			if (tail.getTranslateX() < 0 || tail.getTranslateX() >= GameUtils.WIDTH ||
					tail.getTranslateY() < 0 || tail.getTranslateY() >= GameUtils.HEIGHT) {
				snakeInstance.restartGame();
			}
			
			if (tail.getTranslateX() == food.getTranslateX() && tail.getTranslateY() == food.getTranslateY()) {
				food.setTranslateX((int)(Math.random() * (GameUtils.WIDTH - GameUtils.BLOCK_SIZE)) / GameUtils.BLOCK_SIZE * GameUtils.BLOCK_SIZE);
				food.setTranslateY((int)(Math.random() * (GameUtils.HEIGHT - GameUtils.BLOCK_SIZE)) / GameUtils.BLOCK_SIZE * GameUtils.BLOCK_SIZE);
				
				Rectangle rectangle = new Rectangle(GameUtils.BLOCK_SIZE, GameUtils.BLOCK_SIZE);
				rectangle.setTranslateX(tailX);
				rectangle.setTranslateY(tailY);
				
				snakeInstance.getSnake().add(rectangle);
			}
		});
		
		snakeInstance.getAnimation().getKeyFrames().add(frame);
		snakeInstance.getAnimation().setCycleCount(Timeline.INDEFINITE);
		
		root.getChildren().addAll(food, snakeBody);
		
		return root;
	}
}

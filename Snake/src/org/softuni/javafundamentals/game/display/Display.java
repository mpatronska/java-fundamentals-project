package org.softuni.javafundamentals.game.display;

import org.softuni.javafundamentals.game.snake.SnakeApplication;
import org.softuni.javafundamentals.game.utils.GameUtils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * Defines the display of the game.
 *
 */
public class Display {
	
	private SnakeApplication application;
	
	public Display(SnakeApplication application) {
		this.application = application;
	}

	/**
	 * Creates the layout.
	 * 
	 * @return {@link Parent} object
	 */
	public Pane createContent() {
		Pane root = new Pane();
		root.setPrefSize(GameUtils.WIDTH, GameUtils.HEIGHT);

		addElements(root);
		
		return root;
	}

	/**
	 * Add elements to the layout.
	 * 
	 * @param root
	 */
	private void addElements(Pane root) {
		Group snakeBody = new Group();
		application.getGame().setSnake(snakeBody.getChildren());
		
		Rectangle food = new Rectangle(GameUtils.BLOCK_SIZE, GameUtils.BLOCK_SIZE);
		food.setFill(new ImagePattern(new Image(this.getClass().getResource("../resources/question_coin.png").toExternalForm())));
		food.setTranslateX((int)(Math.random() * (GameUtils.WIDTH - GameUtils.BLOCK_SIZE)) / GameUtils.BLOCK_SIZE * GameUtils.BLOCK_SIZE);
		food.setTranslateY((int)(Math.random() * (GameUtils.HEIGHT - GameUtils.BLOCK_SIZE)) / GameUtils.BLOCK_SIZE * GameUtils.BLOCK_SIZE);
		
		KeyFrame frame = new KeyFrame(Duration.seconds(0.3), event -> {
			if (!application.getGame().isApplicationRunning()) {
				return;
			}
			
			boolean toRemove = application.getGame().getSnake().size() > 1;
			
			Node tail = toRemove ? 
					application.getGame().getSnake().remove(application.getGame().getSnake().size() - 1) : 
						application.getGame().getSnake().get(0);
			
			double tailX = tail.getTranslateX();
			double tailY = tail.getTranslateY();
			
			switch (application.getGame().getDirection()) {
			case UP:
				tail.setTranslateX(application.getGame().getSnake().get(0).getTranslateX());
				tail.setTranslateY(application.getGame().getSnake().get(0).getTranslateY() - GameUtils.BLOCK_SIZE);
				break;
			case DOWN:
				tail.setTranslateX(application.getGame().getSnake().get(0).getTranslateX());
				tail.setTranslateY(application.getGame().getSnake().get(0).getTranslateY() + GameUtils.BLOCK_SIZE);
				break;
			case LEFT:
				tail.setTranslateX(application.getGame().getSnake().get(0).getTranslateX() - GameUtils.BLOCK_SIZE);
				tail.setTranslateY(application.getGame().getSnake().get(0).getTranslateY());
				break;
			case RIGHT:
				tail.setTranslateX(application.getGame().getSnake().get(0).getTranslateX() + GameUtils.BLOCK_SIZE);
				tail.setTranslateY(application.getGame().getSnake().get(0).getTranslateY());
				break;
			}
			
			application.getGame().setMoved(true);
			
			if (toRemove) {
				application.getGame().getSnake().add(0, tail);
			}
			
			//collision detection
			detectCollision(food, tail, tailX, tailY);
			
			//increase level
			if (application.getGame().getScore() >= GameUtils.LEVEL_STEP) {
				application.getGame().setLevel(application.getGame().getLevel() + 1);
				application.getLevelLabel().setText("Level: " + application.getGame().getLevel());
				
				application.getGame().setScore(0);
				application.getScoreLabel().setText("Score: " + application.getGame().getScore());
			}
		});
		
		application.getGame().getAnimation().getKeyFrames().add(frame);
		application.getGame().getAnimation().setCycleCount(Timeline.INDEFINITE);
		
		root.getChildren().addAll(food, snakeBody, application.getScoreLabel(), application.getLevelLabel());
	}

	/**
	 * Detects collision:
	 * <ul>
	 * <li>the snake hits its body</li>
	 * <li>the snake hits the border area</li>
	 * <li>the snake eats the food</li>
	 * </ul>
	 * 
	 * @param food
	 * @param tail
	 * @param tailX
	 * @param tailY
	 */
	private void detectCollision(Rectangle food, Node tail, double tailX, double tailY) {
		checkIfSnakeHitsItsBody(tail);
		checkIfSnakeHitsBorderArea(tail);
		checkIfSnakeEatsTheFood(food, tail, tailX, tailY);
	}

	/**
	 * Checks if the snake hits its body.
	 * 
	 * @param tail
	 */
	private void checkIfSnakeHitsItsBody(Node tail) {
		for (Node node : application.getGame().getSnake()) {
			if (node != tail && tail.getTranslateX() == node.getTranslateX() &&
					tail.getTranslateY() == node.getTranslateY()) {
				//the snake hits its own body
				application.handleGameOver(application.getGame(), application.getScene());
				application.escapeGame(application.getGame());
				break;
			}
		}
	}

	/**
	 * Checks if the snake hits the border area.
	 * 
	 * @param tail
	 */
	private void checkIfSnakeHitsBorderArea(Node tail) {
		if (tail.getTranslateX() < 0 || tail.getTranslateX() >= GameUtils.WIDTH ||
				tail.getTranslateY() < 0 || tail.getTranslateY() >= GameUtils.HEIGHT) {
			
			application.handleGameOver(application.getGame(), application.getScene());
			application.escapeGame(application.getGame());
		}
	}

	/**
	 * Checks if the snake eats the food.
	 * 
	 * @param food
	 * @param tail
	 * @param tailX
	 * @param tailY
	 */
	private void checkIfSnakeEatsTheFood(Rectangle food, Node tail, double tailX, double tailY) {
		if (tail.getTranslateX() == food.getTranslateX() && tail.getTranslateY() == food.getTranslateY()) {
			food.setTranslateX((int)(Math.random() * (GameUtils.WIDTH - GameUtils.BLOCK_SIZE)) / GameUtils.BLOCK_SIZE * GameUtils.BLOCK_SIZE);
			food.setTranslateY((int)(Math.random() * (GameUtils.HEIGHT - GameUtils.BLOCK_SIZE)) / GameUtils.BLOCK_SIZE * GameUtils.BLOCK_SIZE);
			
			Rectangle rectangle = new Rectangle(GameUtils.BLOCK_SIZE, GameUtils.BLOCK_SIZE);
			rectangle.setFill(new ImagePattern(new Image(this.getClass().getResource("../resources/snake_block.png").toExternalForm())));
			rectangle.setTranslateX(tailX);
			rectangle.setTranslateY(tailY);
			
			//increase score
			application.getGame().setScore(application.getGame().getScore() + GameUtils.SCORE_STEP);
			application.getScoreLabel().setText("Score: " + application.getGame().getScore());
			
			application.getGame().getSnake().add(rectangle);
		}
	}
}

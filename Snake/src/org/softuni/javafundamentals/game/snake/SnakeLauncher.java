package org.softuni.javafundamentals.game.snake;

import org.softuni.javafundamentals.game.display.Display;
import org.softuni.javafundamentals.game.utils.GameUtils;

import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class SnakeLauncher extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	
	private GameUtils.Direction direction = GameUtils.Direction.RIGHT;
	
	private boolean isMoved = false;
	private boolean isApplicationRunning = false;
	
	private Timeline animation = new Timeline();
	
	private ObservableList<Node> snake;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			Scene scene = new Scene(new Display(this).createContent());
			
			scene.setOnKeyPressed(event -> {
				if (! isMoved()) {
					return;
				}
				
				switch (event.getCode()) {
				case UP:
					if (getDirection() != GameUtils.Direction.DOWN) {
						setDirection(GameUtils.Direction.UP);
					}
					break;
				case DOWN:
					if (getDirection() != GameUtils.Direction.UP) {
						setDirection(GameUtils.Direction.DOWN);
					}
					break;
				case LEFT:
					if (getDirection() != GameUtils.Direction.RIGHT) {
						setDirection(GameUtils.Direction.LEFT);
					}
					break;
				case RIGHT:
					if (getDirection() != GameUtils.Direction.LEFT) {
						setDirection(GameUtils.Direction.RIGHT);
					}
					break;
				}
				
				setMoved(false);
			});
			
			primaryStage.setTitle("Snake");
			primaryStage.setScene(scene);
			primaryStage.show();
			
			startGame();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void restartGame() {
		stopGame();
		startGame();
	}

	private void startGame() {
		setDirection(GameUtils.Direction.RIGHT);
		Rectangle head = new Rectangle(GameUtils.BLOCK_SIZE, GameUtils.BLOCK_SIZE);
		getSnake().add(head);
		getAnimation().play();
		setApplicationRunning(true);
	}

	private void stopGame() {
		setApplicationRunning(false);
		getAnimation().stop();
		getSnake().clear();
	}

	public ObservableList<Node> getSnake() {
		return snake;
	}

	public void setSnake(ObservableList<Node> snake) {
		this.snake = snake;
	}

	public boolean isApplicationRunning() {
		return isApplicationRunning;
	}

	public void setApplicationRunning(boolean isApplicationRunning) {
		this.isApplicationRunning = isApplicationRunning;
	}

	public GameUtils.Direction getDirection() {
		return direction;
	}

	public void setDirection(GameUtils.Direction direction) {
		this.direction = direction;
	}

	public boolean isMoved() {
		return isMoved;
	}

	public void setMoved(boolean isMoved) {
		this.isMoved = isMoved;
	}

	public Timeline getAnimation() {
		return animation;
	}

	public void setAnimation(Timeline animation) {
		this.animation = animation;
	}

}

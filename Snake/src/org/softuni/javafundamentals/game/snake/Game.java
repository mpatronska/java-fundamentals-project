package org.softuni.javafundamentals.game.snake;

import org.softuni.javafundamentals.game.utils.GameUtils.Direction;

import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.scene.Node;

/**
 * POJO class for the game
 */
public class Game {

    /**
     * Shows the direction of the movement. Default direction is RIGHT.
     */
    private Direction direction;
    /**
     * Shows if the snake has been moved. Default value is FALSE.
     */
    private boolean isMoved;
    /**
     * Shows if the application is running. Default value is FALSE.
     */
    private boolean isApplicationRunning;
    /**
     * Shows if the game is paused. Default value is FALSE.
     */
    private boolean isGamePaused;
    /**
     * <{@link Timeline}> object.
     */
    private Timeline animation;
    /**
     * <{@link ObservableList<Node>}> object.
     */
    private ObservableList<Node> snake;

	/**
     * Default constructor. Sets default values.
     */
    public Game() {
        this.direction = Direction.RIGHT;
        this.isMoved = false;
        this.isApplicationRunning = false;
        this.animation = new Timeline();
        this.isGamePaused = false;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public boolean isMoved() {
        return isMoved;
    }

    public void setMoved(boolean isMoved) {
        this.isMoved = isMoved;
    }

    public boolean isApplicationRunning() {
        return isApplicationRunning;
    }

    public void setApplicationRunning(boolean isApplicationRunning) {
        this.isApplicationRunning = isApplicationRunning;
    }

    public Timeline getAnimation() {
        return animation;
    }

    public void setAnimation(Timeline animation) {
        this.animation = animation;
    }

    public void setSnake(ObservableList<Node> snake) {
        this.snake = snake;
    }

    public ObservableList<Node> getSnake() {
        return snake;
    }

    public boolean isGamePaused() {
    	return isGamePaused;
    }

    public void pauseGame() {
        isGamePaused = true;
        this.animation.pause();
    }

    public void resumeGame() {
        isGamePaused = false;
        this.animation.play();
    }
    
}

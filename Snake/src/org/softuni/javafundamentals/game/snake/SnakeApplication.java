package org.softuni.javafundamentals.game.snake;

import org.softuni.javafundamentals.game.display.Display;
import org.softuni.javafundamentals.game.utils.GameUtils;
import org.softuni.javafundamentals.game.utils.GameUtils.Direction;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * Snake Application - entry point
 */
public class SnakeApplication extends Application {

    private Game game;
    
    private Label scoreLabel = new Label("Score: 0");

	public SnakeApplication() {
        this.game = new Game();
    }

    /**
     * Launch the application.
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /* (non-Javadoc)
     * @see javafx.application.Application#start(javafx.stage.Stage)
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {

            Pane root = new Display(this).createContent();
            root.setId("pane");
            Scene scene = new Scene(root);

            scene.getStylesheets().addAll(this.getClass().getResource("../resources/style.css").toExternalForm());
            //String imageBG = "../resources/snake.png";
            //BackgroundImage newIMG = new BackgroundImage();

            handleKeyEvent(game, scene);

            primaryStage.setTitle("Snake");

            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

            startGame(game);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Handles key event:
     * <p>
     * <ul>
     * <li>UP</li>
     * <li>DOWN</li>
     * <li>LEFT</li>
     * <li>RIGHT</li>
     * <li>P</li>
     * <li>S</li>
     * <li>ESC</li>
     * </ul>
     *
     * @param game  {@link Game} object
     * @param scene {@link Scene} object
     */
    private void handleKeyEvent(Game game, Scene scene) {
        scene.setOnKeyPressed(event -> {

            // check if gamestate is paused - resuming game by press any key
            if (game.isGamePaused()) {
                System.out.println("Game resumed");
                game.resumeGame();
            }

            if (!game.isMoved()) {
                return;
            }

            switch (event.getCode()) {
                case UP:
                    if (game.getDirection() != Direction.DOWN) {
                        game.setDirection(Direction.UP);
                    }
                    break;
                case DOWN:
                    if (game.getDirection() != Direction.UP) {
                        game.setDirection(Direction.DOWN);
                    }
                    break;
                case LEFT:
                    if (game.getDirection() != Direction.RIGHT) {
                        game.setDirection(Direction.LEFT);
                    }
                    break;
                case RIGHT:
                    if (game.getDirection() != Direction.LEFT) {
                        game.setDirection(Direction.RIGHT);
                    }
                    break;

                // pause game - if pressed key 'P'
                case P:
                    pauseGame(game);
                    handleKeyEvent(game, scene);
                    break;

                //stop game - if pressed key 'S' or ESCAPE
                case S:
                case ESCAPE:
                	handleEscape(game, scene);
                	escapeGame(game);
                    break;
            }

            game.setMoved(false);
        });
    }

    /**
     * Handles ESCAPE event
     * @param game
     * @param scene
     */
	private void handleEscape(Game game, Scene scene) {
		Label nameLabel = new Label("Please enter your name:");
		TextField textField = new TextField ();
		
		HBox hb = new HBox();
		hb.getChildren().addAll(nameLabel, textField);
		hb.setSpacing(10);
		hb.setTranslateX(GameUtils.WIDTH / 2);
		hb.setTranslateY(20);
		
		Label endGameLabel = new Label();
		endGameLabel.setTranslateX(GameUtils.WIDTH / 2);
		endGameLabel.setTranslateY(60);
		
		textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
		    @Override
		    public void handle(KeyEvent ke) {
		        if (ke.getCode().equals(KeyCode.ENTER)) {
		        	
		            String name = textField.getText();
		            endGameLabel.setText("Thank you, " + name + ". Your score is: " + game.getScore() + ".");
		            ((Pane)scene.getRoot()).getChildren().add(endGameLabel);
		            
		            Button newGameButton = new Button("New game");
		            newGameButton.setTranslateX(GameUtils.WIDTH / 2);
		            newGameButton.setTranslateY(100);
		            
		            Button exitButton = new Button("Exit");
		            exitButton.setTranslateX(GameUtils.WIDTH / 2 + 150);
		            exitButton.setTranslateY(100);
		            
		            ((Pane)scene.getRoot()).getChildren().addAll(newGameButton, exitButton);
		            
		            newGameButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

						@Override
						public void handle(MouseEvent paramT) {
							restartGame(game);
							scoreLabel.setText("Score: " + game.getScore());
							((Pane)scene.getRoot()).getChildren().removeAll(hb, endGameLabel, newGameButton, exitButton);
						}
					});
		            
		            exitButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

						@Override
						public void handle(MouseEvent paramT) {
							endGame(game);
						}
					});
		        }
		    }
		});
		
		((Pane)scene.getRoot()).getChildren().add(hb);
	}

    /**
     * Restarts the game.
     *
     * @param game {@link Game} object.
     */
    public void restartGame(Game game) {
        stopGame(game);
        startGame(game);
    }

    /**
     * Starts the game.
     *
     * @param game {@link Game} object.
     */
    private void startGame(Game game) {
        game.setDirection(Direction.RIGHT);
        Rectangle head = new Rectangle(GameUtils.BLOCK_SIZE, GameUtils.BLOCK_SIZE);
        head.setFill(new ImagePattern(new Image(this.getClass().getResource("../resources/snake_head.png").toExternalForm())));
        game.getSnake().add(head);
        game.getAnimation().play();
        game.setApplicationRunning(true);
        game.setScore(0);       
        
    }

    /**
     * Stops the game.
     *
     * @param game {@link Game} object.
     */
    private void stopGame(Game game) {
        game.setApplicationRunning(false);
        game.getAnimation().stop();
        game.getSnake().clear();
    }

    /**
     * Ends the game.
     * 
     * @param game
     */
    private void endGame(Game game) {
        game.setApplicationRunning(false);
        // TODO exiting label!
        Platform.exit();
        System.exit(0);
    }

    /**
     * Pauses the game.
     * 
     * @param game
     */
    private void pauseGame(Game game) {
        if (!game.isGamePaused()) {
            game.pauseGame();
            // text on screen to pause needed - TODO
            System.out.println("Game paused");
        }
    }

    /**
     * Escapes from the game.
     * 
     * @param game
     */
    private void escapeGame(Game game) {
    	 game.setApplicationRunning(false);
         game.getAnimation().stop();
    }
    
    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Label getScoreLabel() {
  		return scoreLabel;
  	}
}

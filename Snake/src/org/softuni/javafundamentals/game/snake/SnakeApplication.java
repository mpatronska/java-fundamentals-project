package org.softuni.javafundamentals.game.snake;

import org.softuni.javafundamentals.game.display.Display;
import org.softuni.javafundamentals.game.utils.GameUtils;
import org.softuni.javafundamentals.game.utils.GameUtils.Direction;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * Snake Application - entry point
 */
public class SnakeApplication extends Application {

    private Game game;

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

            Parent root = new Display(this).createContent();
            root.setId("pane");
            Scene scene = new Scene(root);

            scene.getStylesheets().addAll(this.getClass().getResource("../resources/style.css").toExternalForm());
            //String imageBG = "../resources/snake.png";
            //BackgroundImage newIMG = new BackgroundImage();

            handleKeyEvent(game, scene);

            primaryStage.setTitle("Snake");

            primaryStage.setScene(scene);
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
                    /*Label label1 = new Label("Name:");
                    TextField textField = new TextField ();
                    HBox hb = new HBox();
                    hb.getChildren().addAll(label1, textField);
                    hb.setSpacing(10);*/

                    endGame(game);
                    break;
            }

            game.setMoved(false);
        });
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
        game.getSnake().add(head);
        game.getAnimation().play();
        game.setApplicationRunning(true);
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

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

}

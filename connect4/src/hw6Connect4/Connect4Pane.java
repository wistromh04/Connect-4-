package hw6Connect4;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;


/**
 * The Pane where the action is drawn for the Connect 4 game.
 * Handles GUI rendering and player moves.
 * Author: Wistrom Herfordt
 * Date: 4/30/2024
 */ 


public class Connect4Pane extends Pane {
    private int col = 10; 
    private int row = 10; 
    private double circleRad = 20.0; 
    private int gap = 10; 
    private double columnWidth = (circleRad * 2.0) + gap; 
    private Label label = new Label(); 
    private Con4Game con4Game; 
    private boolean ballDropping = false;

    public Connect4Pane(Con4Game game) {
        con4Game = game; 
        paint(); 

        setOnMouseClicked(event -> {
            if (!con4Game.isGameOver() && !ballDropping) {  //Cannot make a move if the game or if previous ball is still dropping
                int selectedCol = (int) (event.getX() / columnWidth); //Determine which column ball is too be dropped in by click position
                handleMove(selectedCol);
            }
        });

        VBox vbox = new VBox();
        vbox.getChildren().add(label);
        vbox.setAlignment(Pos.TOP_CENTER);
        getChildren().add(vbox);
    }

    /**
     * Handles a players move
     * @param selectedCol
     */
    public void handleMove(int selectedCol) {
        int currentPlayer = con4Game.getCurrentPlayer();
        int row = con4Game.getRows() - 1; //start at bottom row
        while (row >= 0 && con4Game.getToken(row, selectedCol) != -1) {
            row--;
        }

        if (row >= 0) {
            int token = currentPlayer;
            Circle circle = new Circle((selectedCol * columnWidth) + circleRad, -circleRad, circleRad); //creates a new circle for the move
            circle.setFill(token == 0 ? Color.RED : Color.YELLOW);
            getChildren().add(circle);

            
            // Animation of the ball drop, also prevents new move from starting until ball has dropped with the boolean flag 
            TranslateTransition transition = new TranslateTransition(Duration.seconds(0.75), circle);
            transition.setToY((row * columnWidth) + circleRad);
            transition.setOnFinished(e -> ballDropping = false); 
            ballDropping = true;
            transition.play();

            con4Game.makeMove(selectedCol);
            if (con4Game.isGameOver()) {
                int winner = (currentPlayer == 0 ? 1 : 2); // Adjust player index
                if (con4Game.checkForWin()) {
                    label.setText("Player " + winner + " wins!");
                } else {
                    label.setText("It's a tie!");
                }
                showAlert("Game Over", label.getText());
            } else {
                int nextPlayer = (currentPlayer == 0 ? 1 : 0); // Switch players
                String currentPlayerMessage = "Player " + (nextPlayer + 1) + "'s turn."; // Adjust player index
                label.setText(currentPlayerMessage);
            }
        }
    }

    private void showAlert(String title, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }

    public void paint() {
        getChildren().clear(); 

        //Draws the board
        for (int i = 0; i < col; i++) {
            for (int j = 0; j < row; j++) {
                Rectangle rect = new Rectangle(i * columnWidth, j * columnWidth, columnWidth, columnWidth);
                rect.setFill(Color.LIGHTBLUE);
                getChildren().add(rect);
            }
        }

       //draws the existing circles 
        for (int i = 0; i < col; i++) {
            for (int j = 0; j < row; j++) {
                int token = con4Game.getToken(j, i);
                if (token != -1) {
                    Circle circle = new Circle((i * columnWidth) + circleRad, (j * columnWidth) + circleRad, circleRad);
                    circle.setFill(token == 0 ? Color.RED : Color.YELLOW);
                    getChildren().add(circle);
                }
            }
        }
    }
}
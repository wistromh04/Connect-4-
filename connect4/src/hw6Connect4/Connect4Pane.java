package hw6Connect4;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 * The Pane where the action is drawn for the Connect 4 game.
 * 
 * tweaks and updates may take place here.
 */
//*Wistrom Herfordt 
// 4/23/2024
//Makes the gameboard and allows you to fill it in with tokens -- not a functional game 


public class Connect4Pane extends Pane {
    private int col = 10; // number of columns for the game board
    private int row = 10; // number of rows for the game board
    private double circleRad = 20.0; // radius of the token
    private int gap = 10; // pixel gap between columns
    private double columnWidth = (circleRad * 2.0) + gap; // column width
    private Label label = new Label(); // update the info on the game - who's turn
    private Con4Game con4Game; // game engine, model data

    public Connect4Pane(Con4Game game) {
        con4Game = new Con4Game(row, col); // starts the engine
        paint(); // calls paint method to draw the board

        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!con4Game.isGameOver()) {
                    int selectedCol = (int) (event.getX() / columnWidth);
                    handleMove(selectedCol);
                }
            }
        });

        VBox vbox = new VBox();
        vbox.getChildren().add(label);
        vbox.setAlignment(Pos.TOP_CENTER);
        getChildren().add(vbox);
       
    }

    public void handleMove(int selectedCol) {
        con4Game.makeMove(selectedCol);
        paint();
        if (con4Game.isGameOver()) {
            int winner = con4Game.getCurrentPlayer() == 0 ? 2 : 1;
            if (con4Game.checkForWin()) {
                label.setText("Player " + winner + " wins!");
            } else {
                label.setText("It's a tie!");
            }
            showAlert("Game Over", label.getText());
        } else {
            String currentPlayerMessage = "Player " + (con4Game.getCurrentPlayer() + 1) + "'s turn.";
            showAlert("Next Turn", currentPlayerMessage); //displays the alert with the title "next turn" and the currentPLayerMessage
            label.setText(currentPlayerMessage);
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

   /**
    * Draws board
    */
    public void paint() {
        getChildren().clear(); // Clear the board

        // Draws the rectangles for the game board
        for (int i = 0; i < col; i++) {
            for (int j = 0; j < row; j++) {
                Rectangle rect = new Rectangle(i * columnWidth, j * columnWidth, columnWidth, columnWidth);
                rect.setFill(Color.LIGHTBLUE); // makes the background blue
                getChildren().add(rect);
            }
        }

        // Draw circles representing player tokens
        for (int i = 0; i < col; i++) {
            for (int j = 0; j < row; j++) {
                int token = con4Game.getToken(j, i);
                if (token != -1) {// checking if cell is not empty
                    Circle circle = new Circle((i * columnWidth) + circleRad, (j * columnWidth) + circleRad, circleRad);
                    circle.setFill(token == 0 ? Color.RED : Color.YELLOW); // This makes it so the first person has red tokens and the second yellow
                    getChildren().add(circle);
                }
            }
        }
    }
}
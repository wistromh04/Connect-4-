package hw6Connect4;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Label; 
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

/**
 * The main application window for the Connect 4 game.
 * Handles game setup, menu actions, and UI components.
 * Author: Wistrom Herfordt
 * Date: 4/30/2024
 */

public class Connect4Window extends Application {
    private Con4Game game;
    private Connect4Pane connect4Pane;
    private BorderPane root;
    private Label connect4Label; 

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        game = new Con4Game(10, 10); 
        connect4Pane = new Connect4Pane(game);
       
        MenuItem saveGameMenuItem = new MenuItem("Save Game");
        saveGameMenuItem.setOnAction(e -> saveGame(primaryStage));

        MenuItem loadGameMenuItem = new MenuItem("Load Game");
        loadGameMenuItem.setOnAction(e -> loadGame(primaryStage));

        MenuItem restartGameMenuItem = new MenuItem("Restart Game");
        restartGameMenuItem.setOnAction(e -> restartGame(primaryStage));
        
        MenuItem aboutGameMenuItem = new MenuItem("About");
        aboutGameMenuItem.setOnAction(e -> showAboutDialog());
        
        Menu gameMenu = new Menu("Game");
        gameMenu.getItems().addAll(saveGameMenuItem, loadGameMenuItem, restartGameMenuItem); 
        
        Menu helpMenu = new Menu("Help");
        helpMenu.getItems().addAll(aboutGameMenuItem); 

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(gameMenu, helpMenu);  

        root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(connect4Pane);
        
        // Initialize and add "CONNECT 4!" label
        connect4Label = new Label("CONNECT 4!");
        connect4Label.setStyle("-fx-font-size: 24; -fx-font-weight: bold;");
        VBox bottomBox = new VBox(connect4Label);
        bottomBox.setAlignment(Pos.CENTER);
        root.setBottom(bottomBox);
  
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Connect 4");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    
    
    private void saveGame(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Game");
        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            game.saveGame(file);
        }
    }

    private void loadGame(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Game");
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            game = Con4Game.loadGame(file);
            if (game == null) {
                showAlert("Unexpected error", "Could not load game file.");
            } else {
                restartGame(primaryStage);
            }
        }
    }

    private void restartGame(Stage primaryStage) {
        game.restartGame();
        connect4Pane = new Connect4Pane(game);
        root.setCenter(connect4Pane);
    }
    
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    private void showAboutDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Connect 4 Game");
        alert.setHeaderText(null);
        alert.setContentText("Connect 4 Game\nThis program allows you to play the Connect 4 game.\nMatch 4 tokens together to win.\nby Wistrom Herfordt");
        alert.showAndWait();
    }
}
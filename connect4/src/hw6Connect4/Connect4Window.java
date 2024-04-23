package hw6Connect4;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
/**
 * Connect 4 game implementation
 *  @author  Wistrom Herfordt 
 * 4/23/2024
 * This code provides the Window View class in JavaFX needed implement the
 * the connect four assignment.
 */

public class Connect4Window extends Application {
    private Con4Game game;
    private Connect4Pane connect4Pane;
    private BorderPane root;
    private Map<String, Integer> bestScores;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    /*
     *This initiliazes the game and creates the menu (adds all the menu items into the menu bar) 
     */
    public void start(Stage primaryStage) {
        game = new Con4Game(10, 10); 
        connect4Pane = new Connect4Pane(game);
        bestScores = new HashMap<>();
       
        //Creation of a bunch of menu items 
        MenuItem saveGameMenuItem = new MenuItem("Save Game");
        saveGameMenuItem.setOnAction(e -> saveGame(primaryStage));

        MenuItem loadGameMenuItem = new MenuItem("Load Game");
        loadGameMenuItem.setOnAction(e -> loadGame(primaryStage));

        MenuItem restartGameMenuItem = new MenuItem("Restart Game");
        restartGameMenuItem.setOnAction(e -> restartGame(primaryStage));
        
        MenuItem aboutGameMenuItem = new MenuItem("About");
        aboutGameMenuItem.setOnAction(e -> showAboutDialog());
        
        MenuItem bestScoresMenuItem = new MenuItem("Best Scores");
        bestScoresMenuItem.setOnAction(e -> showBestScores());


        Menu gameMenu = new Menu("Game");
        gameMenu.getItems().addAll(saveGameMenuItem, loadGameMenuItem, restartGameMenuItem, bestScoresMenuItem); //adds the load, save, and restart button to the game menu 
        
        Menu helpMenu = new Menu("Help");
        helpMenu.getItems().addAll(aboutGameMenuItem); //adds the about button to the help menu 

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(gameMenu, helpMenu);  //adds the game menu and help menu to the bar 

        root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(connect4Pane);
        
  
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Connect 4");
        primaryStage.show();
    }

    public void addAScore(String playerName) {
    	bestScores.put(playerName, bestScores.getOrDefault(playerName, 0) + 1);
    }
    
    private void showBestScores() {
    	Alert alert = new Alert(Alert.AlertType.INFORMATION);
    	alert.setTitle("Top Scores");
    	alert.setHeaderText(null);
    	
    	StringBuilder scoresMessage = new StringBuilder();
    	for (Map.Entry<String, Integer> entry : bestScores.entrySet()) {
    		 scoresMessage.append(entry.getKey()).append(": ").append(entry.getValue()).append(" wins\n");
    	}
    	alert.setContentText(scoresMessage.toString());
    	alert.showAndWait();
    }
    
    /**
     * save a game state to a file
     */
    private void saveGame(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Game");
        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            game.saveGame(file);
        }
    }

    /**
     * load a game state from a prev. saved file
     * @param primaryStage
     */
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

    /**
     * restarts game 
     * @param primaryStage
     */
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
    
    /**
     *The about menu code, sets the alert to one that is info related  
     */
    private void showAboutDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Connect 4 Game");
        alert.setHeaderText(null);
        alert.setContentText("Connect 4 Game\nThis program allows you to play the Connect 4 game.\nby Wistrom Herfordt");
        alert.showAndWait();
    }
}
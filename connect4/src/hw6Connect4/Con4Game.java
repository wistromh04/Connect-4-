package hw6Connect4;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

/**
 *  Connect 4 game implementation
 *  @author Catherine Anderson, Sherri WeitlHarms, Wistrom Herfordt
 * 4/29/2024
 */
public class Con4Game {
    private int rows;
    private int cols;
    private int[][] gameArray; // array that indicates which player's tokens are in which column
    private int currentPlayer;

    public Con4Game(int r, int c) {
        rows = r;
        cols = c;
        gameArray = new int[rows][cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                gameArray[row][col] = -1; // -1 means no token present
            }
        }
        currentPlayer = 0;
    }

   /*
    *Makes a move
    *@param c Column where the move goes 
    */
    public void makeMove(int c) {
        for (int row = (rows - 1); row >= 0; row--) {
            if (gameArray[row][c] == -1) {
                gameArray[row][c] = currentPlayer;
                break;
            }
        }
        nextPlayer();
    }

    public int getToken(int r, int c) {
        return gameArray[r][c];
    }

    /**
     * switches player
     */
    public void nextPlayer() {
        currentPlayer++;
        currentPlayer %= 2;
    }

   /**
    * checks if board is full
    * @return
    */
    public boolean isBoardFull() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (gameArray[row][col] == -1) {
                    return false; // Board is not full
                }
            }
        }
        return true; // Board is full
    }

    /**
     * Checks if there is 4 tokens connected
     * @return
     */
    public boolean checkForWin() {
        // Check horizontal
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols - 3; col++) {
                if (gameArray[row][col] != -1 &&
                    gameArray[row][col] == gameArray[row][col + 1] &&
                    gameArray[row][col] == gameArray[row][col + 2] &&
                    gameArray[row][col] == gameArray[row][col + 3]) {
                    return true; // four in a row
                }
            }
        }

        // Check vertically
        for (int row = 0; row < rows - 3; row++) {
            for (int col = 0; col < cols; col++) {
                if (gameArray[row][col] != -1 &&
                    gameArray[row][col] == gameArray[row + 1][col] &&
                    gameArray[row][col] == gameArray[row + 2][col] &&
                    gameArray[row][col] == gameArray[row + 3][col]) {
                    return true; // four in a column
                }
            }
        }

        // Check diagonally (from bottom-left to top-right)
        for (int row = 3; row < rows; row++) {
            for (int col = 0; col < cols - 3; col++) {
                if (gameArray[row][col] != -1 &&
                    gameArray[row][col] == gameArray[row - 1][col + 1] &&
                    gameArray[row][col] == gameArray[row - 2][col + 2] &&
                    gameArray[row][col] == gameArray[row - 3][col + 3]) {
                    return true; //  four in a diagonal
                }
            }
        }

        // Check diagonally (from top-left to bottom-right)
        for (int row = 0; row < rows - 3; row++) {
            for (int col = 0; col < cols - 3; col++) {
                if (gameArray[row][col] != -1 &&
                    gameArray[row][col] == gameArray[row + 1][col + 1] &&
                    gameArray[row][col] == gameArray[row + 2][col + 2] &&
                    gameArray[row][col] == gameArray[row + 3][col + 3]) {
                    return true; // four in a diagonal
                }
            }
        }

        return false; // There is no win 
    }

    /*
     * There is either a winning condition or the board is full (tie)
     */
    public boolean isGameOver() {
        return checkForWin() || isBoardFull();
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public int[][] getGameArray() {
        return gameArray;
    }

    public void setGameArray(int[][] gameArray) {
        this.gameArray = gameArray;
    }

    public void saveGame(File file) {
        try {
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(this);
            objectOut.close();
            fileOut.close();
        } catch (Exception e) {
            System.err.println("Error occurred while saving the game: " + e.getMessage());
        }
    }

    public static Con4Game loadGame(File file) {
        try {
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            Con4Game game = (Con4Game) objectIn.readObject();
            objectIn.close();
            fileIn.close();
            return game;
        } catch (Exception e) { //prints message if error occurs
            System.err.println("Error occurred while loading the game: " + e.getMessage());
            return null;
        }
    }

    public void restartGame() {
        gameArray = new int[rows][cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                gameArray[row][col] = -1; // -1 indicates no token present
            }
        }
        currentPlayer = 0;
    }

}

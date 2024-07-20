package game;
import java.util.ArrayList;
/**
 * 2048 Board with methods to update and manipulate the board.
 * 
 */
public class Board {
    private int[][] gameBoard; // the game board array
    private ArrayList<BoardSpot> openSpaces; // the ArrayList of open spots

    /**
     * Zero-argument Constructor: initializes a 4x4 game board.
     */
    public Board() {
        gameBoard = new int[4][4];
        openSpaces = new ArrayList<>();
    }

    /**
     * One-argument Constructor: initializes a game board based on a given array.
     * 
     * @param board the board array
     */
    public Board(int[][] board) {
        gameBoard = new int[board.length][board[0].length];
        for (int r = 0; r < gameBoard.length; r++) {
            for (int c = 0; c < gameBoard[r].length; c++) {
                gameBoard[r][c] = board[r][c];
            }
        }
        openSpaces = new ArrayList<>();
    }

    /**
     * Updates openSpaces with the current open spots.
     */
    public void updateOpenSpaces() {
        openSpaces.clear();
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                if (gameBoard[i][j] == 0) {
                    openSpaces.add(new BoardSpot(i, j));
                }
            }
        }
    }

    /**
     * Adds a random tile to an open spot with a 90% chance of a 2 value and a 10% chance of a 4 value.
     */
    public void addRandomTile() {
        if (openSpaces.size() > 0) {
            BoardSpot t = openSpaces.get(StdRandom.uniform(openSpaces.size()));
            double prob = StdRandom.uniform();
            int value = (prob < 0.1) ? 4 : 2;
            gameBoard[t.getRow()][t.getCol()] = value;
        }
    }

    /**
     * Shifts all nonzero tiles to the left as far as possible.
     */
    public void swipeLeft() {
        for (int i = 0; i < gameBoard.length; i++) {
            int col = 0;
            for (int j = 0; j < gameBoard[i].length; j++) {
                if (gameBoard[i][j] != 0) {
                    int temp = gameBoard[i][j];
                    gameBoard[i][j] = 0;
                    gameBoard[i][col] = temp;
                    col++;
                }
            }
        }
    }

    /**
     * Merges all identical left pairs in the board.
     */
    public void mergeLeft() {
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 1; j < gameBoard[i].length; j++) {
                if (gameBoard[i][j] != 0 && gameBoard[i][j] == gameBoard[i][j - 1]) {
                    gameBoard[i][j - 1] *= 2;
                    gameBoard[i][j] = 0;
                }
            }
        }
    }

    /**
     * Rotates the board 90 degrees clockwise.
     */
    public void rotateBoard() {
        transpose();
        flipRows();
    }

    /**
     * Transposes the board.
     */
    public void transpose() {
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < i; j++) {
                int temp = gameBoard[i][j];
                gameBoard[i][j] = gameBoard[j][i];
                gameBoard[j][i] = temp;
            }
        }
    }

    /**
     * Reverses the rows of the board.
     */
    public void flipRows() {
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length / 2; j++) {
                int temp = gameBoard[i][j];
                gameBoard[i][j] = gameBoard[i][gameBoard[i].length - j - 1];
                gameBoard[i][gameBoard[i].length - j - 1] = temp;
            }
        }
    }

    /**
     * Makes a move based on the input letter (L, U, R, D).
     * 
     * @param letter the first letter of the action to take
     */
    public void makeMove(char letter) {
        switch (letter) {
            case 'U':
                rotateBoard();
                rotateBoard();
                rotateBoard();
                swipeLeft();
                mergeLeft();
                swipeLeft();
                rotateBoard();
                break;
            case 'D':
                rotateBoard();
                swipeLeft();
                mergeLeft();
                swipeLeft();
                rotateBoard();
                rotateBoard();
                rotateBoard();
                break;
            case 'L':
                swipeLeft();
                mergeLeft();
                swipeLeft();
                break;
            case 'R':
                rotateBoard();
                rotateBoard();
                swipeLeft();
                mergeLeft();
                swipeLeft();
                rotateBoard();
                rotateBoard();
                break;
            default:
                break;
        }
    }
    
    

    /**
     * Checks if the game is lost.
     * 
     * @return true if no empty spaces are available
     */
    public boolean isGameLost() {
        return openSpaces.size() == 0;
    }

    /**
     * Calculates and returns the score.
     * 
     * @return the final score
     */
    public int showScore() {
        int score = 0;
        for (int r = 0; r < gameBoard.length; r++) {
            for (int c = 0; c < gameBoard[r].length; c++) {
                score += gameBoard[r][c];
            }
        }
        return score;
    }

    /**
     * Prints the board as integer values.
     */
    public void print() {
        for (int r = 0; r < gameBoard.length; r++) {
            for (int c = 0; c < gameBoard[r].length; c++) {
                String g = Integer.toString(gameBoard[r][c]);
                StdOut.print((g.equals("0")) ? "-" : g);
                for (int o = 0; o < (5 - g.length()); o++) {
                    StdOut.print(" ");
                }
            }
            StdOut.println();
        }
    }

    /**
     * Prints the board with open spaces denoted by "**".
     */
    public void printOpenSpaces() {
        for (int r = 0; r < gameBoard.length; r++) {
            for (int c = 0; c < gameBoard[r].length; c++) {
                String g = Integer.toString(gameBoard[r][c]);
                for (BoardSpot bs : getOpenSpaces()) {
                    if (r == bs.getRow() && c == bs.getCol()) {
                        g = "**";
                    }
                }
                StdOut.print((g.equals("0")) ? "-" : g);
                for (int o = 0; o < (5 - g.length()); o++) {
                    StdOut.print(" ");
                }
            }
            StdOut.println();
        }
    }

    /**
     * Seed Constructor: Sets a seed for debugging.
     * 
     * @param seed the long seed value
     */
    public Board(long seed) {
        StdRandom.setSeed(seed);
        gameBoard = new int[4][4];
    }

    /**
     * Gets the open board spaces.
     * 
     * @return the ArrayList of BoardSpots containing open spaces
     */
    public ArrayList<BoardSpot> getOpenSpaces() {
        return openSpaces;
    }

    /**
     * Gets the board 2D array values.
     * 
     * @return the 2D array game board
     */
    public int[][] getBoard() {
        return gameBoard;
    }
}

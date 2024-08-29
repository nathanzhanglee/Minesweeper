import java.io.*;

public class Minesweeper {
    private int numCols;
    private int numRows;
    private int numBombs;
    private int numFlags;
    private boolean firstMove;
    private boolean gameWon;
    private boolean gameLost;
    private Space[][] board;


    public Minesweeper() {
        reset();
    }

    public void reset() {
        numCols = 10;
        numRows = 10;
        numBombs = 12;
        numFlags = 12;
        firstMove = true;
        gameWon = false;
        gameLost = false;
        board = new Space[numRows][numCols];
        addSpaces(board);
    }

    public void addSpaces(Space[][] board) {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                board[i][j] = new Space();
            }
        }
    }

    public boolean isFirstMove() {
        return firstMove;
    }

    public int getNumFlags() {
        return updatedFlagCount();
    }

    public Space[][] getBoard() {
        return board;
    }

    public boolean hasGameWon() {
        return gameWon;
    }

    public boolean hasGameLost() {
        return gameLost;
    }

    public void addBombs(int x, int y) {
        int bombsToAdd = numBombs;
        while (bombsToAdd > 0) {
            int col = (int) (Math.random() * (numCols));
            int row = (int) (Math.random() * (numRows));
            if (col != x || row != y) {
                Space currentSpace = board[row][col];
                if (!currentSpace.getBombStatus()) {
                    currentSpace.placeBomb();
                    bombsToAdd--;
                }
            }
        }
    }

    public boolean checkWin() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                Space currSpace = board[i][j];
                if (!currSpace.getBombStatus() && !currSpace.getRevealedStatus()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void uncoverAllBombs() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                Space currSpace = board[i][j];
                if (currSpace.getBombStatus()) {
                    currSpace.reveal();
                }
            }
        }
    }

    public void uncoverSpace(int i, int j) {
        Space currSpace = board[i][j];
        if (firstMove) {
            addBombs(i, j);
            firstMove = false;
            uncoverSpace(i, j);
        }
        if (!gameWon && !gameLost) {
            if (currSpace.getBombStatus() && !currSpace.getFlagStatus() && !currSpace.getRevealedStatus()) {
                currSpace.reveal();
                currSpace.setDetonator();
                uncoverAllBombs();
                gameLost = true;
            } else if (!currSpace.getRevealedStatus() && !currSpace.getFlagStatus()) {
                currSpace.reveal();
                gameWon = checkWin();
                if (numSurrondingBombs(i, j) == 0) {
                    int count = 0;
                    int left = i - 1;
                    int right = i + 1;
                    int top = j - 1;
                    int bottom = j + 1;

                    if (left < 0) {
                        left = 0;
                    }
                    if (right >= numCols) {
                        right = numCols - 1;
                    }
                    if (top < 0) {
                        top = 0;
                    }
                    if (bottom >= numRows) {
                        bottom = numRows - 1;
                    }
                    uncoverSpace(left, j);
                    uncoverSpace(right, j);
                    uncoverSpace(i, bottom);
                    uncoverSpace(i, top);
                }
            }
        }
    }

    public int updatedFlagCount() {
    int count = 0;
        for (int i =0; i < numRows; i++) {
        for (int j = 0; j < numCols; j++) {
            Space currSpace = board[i][j];
            if (currSpace.getFlagStatus()) {
                count++;
            }
        }
    }
        return 12 - count;
}

    public void addFlag(int i, int j) {
        Space currSpace = board[i][j];
        if (!gameWon && !gameLost) {
            if (currSpace.getFlagStatus()) {
                numFlags++;
                currSpace.flag();
            } else if (numFlags > 0 && !currSpace.getRevealedStatus()) {
                numFlags--;
                currSpace.flag();
            }
        }
    }

    public int numSurrondingBombs(int i, int j) {
        int count = 0;
        int left = i - 1;
        int right = i + 1;
        int top = j - 1;
        int bottom = j + 1;

        if (left < 0) {
            left = 0;
        }
        if (right >= numCols) {
            right = numCols - 1;
        }
        if (top < 0) {
            top = 0;
        }
        if (bottom >= numRows) {
            bottom = numRows - 1;
        }
        for (int m = left; m <= right; m++) {
            for (int n = top; n <= bottom; n++) {
                Space currSpace = board[m][n];
                if (currSpace.getBombStatus()) {
                    count++;
                }
            }
        }
        return count;
    }

    public void saveGame() {
        int[][] numberedBoard = new int[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                Space currSpace = board[i][j];
                if (currSpace.saveGameHelper(true, false, true, true)) { // detonated bomb
                    numberedBoard[i][j] = 4;
                }
                if (currSpace.saveGameHelper(true, false, true, false)) { // revealed bomb
                    numberedBoard[i][j] = 5;
                }
                if (currSpace.saveGameHelper(false, true, false, false)) { //flagged non-bomb
                    numberedBoard[i][j] = 6;
                }
                if (currSpace.saveGameHelper(true, true, false, false)) { //flagged bomb
                    numberedBoard[i][j] = 0;
                }
                if (currSpace.saveGameHelper(true, false, false, false)) { //unrevealed bomb
                    numberedBoard[i][j] = 1;
                }
                if (currSpace.saveGameHelper(false, false, false, false)) { //unrevealed space
                    numberedBoard[i][j] = 2;
                }
                if (currSpace.saveGameHelper(false, false, true, false)) { //revealed space
                    numberedBoard[i][j] = 3;
                }
            }
        }
        try {
            BufferedWriter myWriter = new BufferedWriter(new FileWriter("src/GameState.txt"));
            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < numCols; j++) {
                    myWriter.write(numberedBoard[i][j] + "");
                }
            }
            while (numBombs > 0) {
                myWriter.write("B");
                numBombs--;
            }
            while (numFlags > 0) {
                myWriter.write("F");
                numFlags--;
            }
            if (firstMove) {
                myWriter.write("W");
            }
            else {
                myWriter.write("X");
            }
            if (gameWon) {
                myWriter.write("Y");
            }
            if (gameLost) {
                myWriter.write("Z");
            }
            myWriter.close();
        } catch (IOException e) {
            System.out.println ("IOException found!");
        }
    }
    public void loadGame() {
        numBombs = 0;
        numFlags = 0;
        firstMove = true;
        gameWon = false;
        gameLost = false;
        boolean helper = true;
        int[][] numberedBoard = new int[numRows][numCols];
        try {
            BufferedReader myReader = new BufferedReader(new FileReader("src/GameState.txt"));
            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < numCols; j++) {
                    numberedBoard[i][j] = myReader.read() - 48; //converting ascii code to ints
                }
            }

            while (helper) {
                int readInt = myReader.read();
                if (readInt != -1) {
                    if (readInt == 66) { // ascii code for "B"
                        numBombs++;
                    }
                    if (readInt == 70) { //ascii code for "F"
                        numFlags++;
                    }
                    if (readInt == 87) { // ascii code for "W"
                        firstMove = true;
                    }
                    if (readInt == 88) { // ascii code for "X"
                        firstMove = false;
                    }
                    if (readInt == 89) { //ascii code for "Y"
                        gameWon = true;
                    }

                    if (readInt == 90) { // ascii code for "Z"
                        gameLost = true;
                    }
                }
                else {
                    helper = false;
                }
            }
            myReader.close();
            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < numCols; j++) {
                    if (numberedBoard[i][j] == 4) {
                        board[i][j] = new Space(true, false, true, true);
                    }
                    if (numberedBoard[i][j] == 5) {
                        board[i][j] = new Space(true, false, true, false);
                    }
                    if (numberedBoard[i][j] == 6) {
                        board[i][j] = new Space(false, true, false, false);
                    }
                    if (numberedBoard[i][j] == 0) {
                        board[i][j] = new Space(true, true, false, false);
                    }
                    if (numberedBoard[i][j] == 1) {
                        board[i][j] = new Space(true, false, false, false);
                    }
                    if (numberedBoard[i][j] == 2) {
                        board[i][j] = new Space(false, false, false, false);
                    }
                    if (numberedBoard[i][j] == 3) {
                        board[i][j] = new Space(false, false, true, false);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("IOException found!");
        }
    }
}

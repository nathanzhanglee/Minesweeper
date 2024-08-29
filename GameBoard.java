import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;


public class GameBoard extends JPanel {

    private Minesweeper model; // model for the game
    private JLabel status; // current status text

    // Game constants
    public static final int BOARD_WIDTH = 500;
    public static final int BOARD_HEIGHT = 500;
    private BufferedImage unrevealedSpace;
    private BufferedImage detonatedBomb;
    private BufferedImage revealedBomb;
    private BufferedImage revealedBlank;
    private BufferedImage flag;
    private BufferedImage one;
    private BufferedImage two;
    private BufferedImage three;
    private BufferedImage four;
    private BufferedImage five;
    private BufferedImage six;
    private BufferedImage seven;
    private BufferedImage eight;

    /**
     * Initializes the game board.
     */
    public GameBoard(JLabel statusInit) {
        try {
            unrevealedSpace = ImageIO.read(new File("src/unrevealedSpace.png"));
            detonatedBomb = ImageIO.read(new File("src/detonatedBomb.png"));
            revealedBomb = ImageIO.read(new File("src/revealedBomb.png"));
            revealedBlank = ImageIO.read(new File("src/revealedBlank.png"));
            flag = ImageIO.read(new File("src/flag.png"));
            one = ImageIO.read(new File("src/one.png"));
            two = ImageIO.read(new File("src/two.png"));
            three = ImageIO.read(new File("src/three.png"));
            four = ImageIO.read(new File("src/four.png"));
            five = ImageIO.read(new File("src/five.png"));
            six = ImageIO.read(new File("src/six.png"));
            seven = ImageIO.read(new File("src/seven.png"));
            eight = ImageIO.read(new File("src/eight.png"));
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        model = new Minesweeper(); // initializes model for the game
        status = statusInit; // initializes the status JLabel

        /*
         * Listens for mouseclicks. Updates the model, then updates the game
         * board based off of the updated model.
         */
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();

                // updates the model given the coordinates of the mouseclick
                if (SwingUtilities.isLeftMouseButton(e)) {
                    model.uncoverSpace(p.x / 50, p.y / 50);
                }

                else if (SwingUtilities.isRightMouseButton(e)) {
                    model.addFlag(p.x / 50, p.y / 50);
                }

                updateStatus(); // updates the status JLabel
                repaint(); // repaints the game board
            }
        });
    }

    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        model.reset();
        status.setText("Remaining Flags: " + model.getNumFlags());
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }
    public void save() {
        model.saveGame();
        status.setText("Game Saved");
        repaint();
        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    public void load() {
        model.loadGame();
        status.setText("Remaining Flags: " + model.getNumFlags());
        repaint();
        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        status.setText("Remaining Flags: " + model.getNumFlags());

        if (model.hasGameWon()) {
            status.setText("You Win!");
        }

        if (model.hasGameLost()) {
            status.setText("You Lose!");
        }
    }

    /**
     * Draws the game board.
     *
     * There are many ways to draw a game board. This approach
     * will not be sufficient for most games, because it is not
     * modular. All of the logic for drawing the game board is
     * in this method, and it does not take advantage of helper
     * methods. Consider breaking up your paintComponent logic
     * into multiple methods or classes, like Mushroom of Doom.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draws board grid
        int unitWidth = BOARD_WIDTH / 10;
        int unitHeight = BOARD_HEIGHT / 10;
        for (int i = 0; i < 10; i++) {
            g.drawLine(unitWidth * (i + 1), 0, unitWidth * (i + 1), BOARD_HEIGHT);
            g.drawLine(0, unitHeight * (i + 1), BOARD_WIDTH, unitHeight * (i + 1));
        }

        Space[][] board = model.getBoard();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Space currSpace = board[i][j];
                if (!currSpace.getRevealedStatus()) {
                    g.drawImage(unrevealedSpace, i * unitWidth, j * unitHeight, unitWidth, unitHeight, null);
                }
                if (currSpace.getFlagStatus()) {
                    g.drawImage(flag, i * unitWidth, j * unitHeight, unitWidth, unitHeight, null);
                }
                else if (currSpace.getRevealedStatus() && currSpace.getBombStatus() && currSpace.getDetonatorStatus()) {
                    g.drawImage(detonatedBomb, i * unitWidth, j * unitHeight, unitWidth, unitHeight, null);
                }
                else if (currSpace.getRevealedStatus() && currSpace.getBombStatus()) {
                    g.drawImage(revealedBomb, i * unitWidth, j * unitHeight, unitWidth, unitHeight, null);
                }
                else if (currSpace.getRevealedStatus() && model.numSurrondingBombs(i, j) == 0){
                    g.drawImage(revealedBlank, i * unitWidth, j * unitHeight, unitWidth, unitHeight, null);
                }
                else if (currSpace.getRevealedStatus() && model.numSurrondingBombs(i, j) == 1){
                    g.drawImage(one, i * unitWidth, j * unitHeight, unitWidth, unitHeight, null);
                }
                else if (currSpace.getRevealedStatus() && model.numSurrondingBombs(i, j) == 2){
                    g.drawImage(two, i * unitWidth, j * unitHeight, unitWidth, unitHeight, null);
                }
                else if (currSpace.getRevealedStatus() && model.numSurrondingBombs(i, j) == 3){
                    g.drawImage(three, i * unitWidth, j * unitHeight, unitWidth, unitHeight, null);
                }
                else if (currSpace.getRevealedStatus() && model.numSurrondingBombs(i, j) == 4){
                    g.drawImage(four, i * unitWidth, j * unitHeight, unitWidth, unitHeight, null);
                }
                else if (currSpace.getRevealedStatus() && model.numSurrondingBombs(i, j) == 5){
                    g.drawImage(five, i * unitWidth, j * unitHeight, unitWidth, unitHeight, null);
                }
                else if (currSpace.getRevealedStatus() && model.numSurrondingBombs(i, j) == 6){
                    g.drawImage(six, i * unitWidth, j * unitHeight, unitWidth, unitHeight, null);
                }
                else if (currSpace.getRevealedStatus() && model.numSurrondingBombs(i, j) == 7){
                    g.drawImage(seven, i * unitWidth, j * unitHeight, unitWidth, unitHeight, null);
                }
                else if (currSpace.getRevealedStatus() && model.numSurrondingBombs(i, j) == 8){
                    g.drawImage(eight, i * unitWidth, j * unitHeight, unitWidth, unitHeight, null);
                }
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}


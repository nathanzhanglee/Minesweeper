import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RunMinesweeper implements Runnable {
        public void run() {

            final JFrame frame = new JFrame("Minesweeper");
            frame.setLocation(500, 500);

            // Status panel
            final JPanel status_panel = new JPanel();
            frame.add(status_panel, BorderLayout.SOUTH);
            final JLabel status = new JLabel("Setting up...");
            status_panel.add(status);

            // Game board
            final GameBoard board = new GameBoard(status);
            frame.add(board, BorderLayout.CENTER);

            // Reset button
            final JPanel control_panel = new JPanel();
            frame.add(control_panel, BorderLayout.NORTH);

            final JButton reset = new JButton("Reset");
            reset.addActionListener(e -> board.reset());
            control_panel.add(reset);
            final JButton instructions = new JButton("Instructions");
            instructions.addActionListener(e ->
                JOptionPane.showMessageDialog(null, "There are 12 total mines under the tiles."
                                + "\n" + "To win, you must left-click to reveal all spaces that do not have a mine."
                                + "\n" + "To help, you can right-click (click left and center if Mac) spaces to place flags on spaces you believe there are mines"
                        + "\n" + "Note: placing all flags on spaces with mines does not cause a win (all non-mine spots must be revealed)"
                        + "\n" + "The numbers on revealed spaces indicate how many mines directly surround the space, including diagonal spaces"
                        + "\n" + "Press 'Reset' to restart the game" + "\n" + "Press 'Save Game' to save all progress"
                        + "\n" + "Press 'Reload Game' to load the most recently saved game"));
            control_panel.add(instructions);
            final JButton save = new JButton("Save Current Game");
            save.addActionListener(e -> board.save());
            control_panel.add(save);

            final JButton load = new JButton("Reload Game");
            load.addActionListener(e -> board.load());
            control_panel.add(load);


            // Put the frame on the screen
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);

            // Start the game
            board.reset();
        }
    }
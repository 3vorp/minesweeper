package evorp.minesweeper;

import evorp.minesweeper.game.MinesweeperGame;
import evorp.minesweeper.launcher.MinesweeperLauncher;

import javax.swing.*;

public class Minesweeper implements Runnable {
    public static final boolean DEBUG = true;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Minesweeper());
    }

    public void run() {
        final JFrame frame = new JFrame("Minesweeper");
        final MinesweeperLauncher launcher = new MinesweeperLauncher();

        launcher.render(frame, (config) -> {
            final MinesweeperGame game = new MinesweeperGame(config);
            game.render(frame);
            frame.repaint();
        });

        frame.pack();
        // https://stackoverflow.com/a/144893/20327257
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

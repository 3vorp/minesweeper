package evorp.minesweeper.game;

import evorp.minesweeper.config.MinesweeperConfig;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MinesweeperGame {
    // config is now final since the launcher has finished running
    private final MinesweeperConfig config;

    private static final int TOP_BOARD_PADDING = 64;

    public MinesweeperGame(MinesweeperConfig config) {
        this.config = config;
    }

    public void render(JFrame frame) {
        frame.getContentPane().removeAll();
        GameManager manager = new GameManager(config);
        JPanel statusBar = new JPanel(new BorderLayout());
        JLabel statusLabel = new JLabel("Remaining bombs: " + config.getMineCount());
        statusBar.add(statusLabel, BorderLayout.LINE_START);

        Timer timer = addTimerLabel(statusBar);
        GameBoard board = new GameBoard(config, manager, (status) -> {
            statusLabel.setText(getStatus(status, manager));
            // either win or loss
            if (status != GameStatus.InProgress) {
                timer.stop();
            }

        });
        // compensate for title bar
        frame.setSize(
                (int) board.getPreferredSize().getWidth(),
                (int) board.getPreferredSize().getHeight() + TOP_BOARD_PADDING
        );
        frame.add(statusBar, BorderLayout.PAGE_START);
        frame.add(board, BorderLayout.PAGE_END);
        timer.start();
    }

    private Timer addTimerLabel(JPanel statusBar) {
        // intellij suggested this since closure variables must be final
        AtomicInteger time = new AtomicInteger();
        JLabel timerLabel = new JLabel("Time: 0s");
        statusBar.add(timerLabel, BorderLayout.LINE_END);
        Timer timer = new Timer(1000, e -> {
            timerLabel.setText("Time: " + time.getAndIncrement() + "s");
        });
        timer.start();
        return timer;
    }

    private String getStatus(GameStatus status, GameManager manager) {
        switch (status) {
            case InProgress:
                int delta = (config.getMineCount() - manager.getFlaggedBombCount());
                String newText = "Remaining bombs: " + delta;
                if (delta < 0) {
                    newText += " (you've probably messed up)";
                }
                return newText;
            case Won:
                return "You won!";
            case Lost:
                return "You lost!";
            default:
                return "";
        }
    }
}

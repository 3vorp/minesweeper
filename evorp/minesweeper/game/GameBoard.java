package evorp.minesweeper.game;

import evorp.minesweeper.config.MinesweeperConfig;
import evorp.minesweeper.config.PathManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

public class GameBoard extends JPanel {
    private final MinesweeperConfig config;
    private final GameManager manager;

    public static final String TEXTURE_PATH = PathManager.getPath("textures");

    private static final int SPRITE_SIZE = 16;

    public GameBoard(
            MinesweeperConfig config,
            GameManager manager,
            Consumer<GameStatus> statusListener) {
        this.config = config;
        this.manager = manager;

        this.setFocusable(true);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();
                int row = p.y / getFinalSpriteSize();
                int col = p.x / getFinalSpriteSize();
                manager.onClick(row, col, isRightClickLike(e));
                statusListener.accept(manager.getStatusManager().getStatus());
                repaint();
            }
        });
    }

    private static boolean isRightClickLike(MouseEvent e) {
        // ctrl+click is convenient for single-button mice
        boolean isCtrlClick = (e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) != 0;
        return SwingUtilities.isRightMouseButton(e) || isCtrlClick;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(getWidth(), getHeight());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int row = 0; row < config.getHeight(); row++) {
            for (int col = 0; col < config.getWidth(); col++) {
                g.drawImage(
                        getSprite(row, col),
                        col * getFinalSpriteSize(),
                        row * getFinalSpriteSize(),
                        this);
            }
        }
    }

    private Image getSprite(int row, int col) {
        String path = getSpritePath(row, col);

        try {
            Image i = ImageIO.read(new File(path));
            return i.getScaledInstance(
                    getFinalSpriteSize(),
                    getFinalSpriteSize(),
                    Image.SCALE_DEFAULT);
        } catch (IOException err) {
            JOptionPane.showMessageDialog(
                    this,
                    err.getMessage(),
                    "Texture Load Error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private String getSpritePath(int row, int col) {
        GameCell cell = manager.getCell(row, col);

        String path = TEXTURE_PATH + "interacted/";
        if (cell.isConsumed()) {
            if (cell.isMine()) {
                return path + "bomb.png";
            }
            return path + manager.getBorderCount(row, col) + ".png";
        }

        StatusManager statusManager = manager.getStatusManager();

        if (statusManager.isGameOver() || statusManager.hasWon()) {
            if (cell.isChecked() && !cell.isMine()) {
                return path + "wrong_bomb.png";
            }
            // game is over, show all bombs
            if (cell.isMine()) {
                return path + "bomb.png";
            }
        }

        // only use uninteracted sprites if the box is still interactable
        path = TEXTURE_PATH + "uninteracted/";
        if (cell.isChecked()) {
            return path + "flag.png";
        } else {
            return path + "empty.png";
        }
    }

    private int getSpriteMultiplier() {
        if (config.getWidth() > 10 || config.getHeight() > 8) {
            return 2;
        }
        return 4;
    }

    private int getFinalSpriteSize() {
        return SPRITE_SIZE * getSpriteMultiplier();
    }

    public int getWidth() {
        return getFinalSpriteSize() * config.getWidth();
    }

    public int getHeight() {
        return getFinalSpriteSize() * config.getHeight();
    }
}

package evorp.minesweeper.game;

import evorp.minesweeper.Minesweeper;
import evorp.minesweeper.config.MinesweeperConfig;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

// model class, only handles behavior
public class GameManager {
    private final MinesweeperConfig config;
    private final StatusManager statusManager;
    private final GameCell[][] board;
    private boolean firstMove;

    public GameManager(MinesweeperConfig config) {
        this.config = config;
        this.firstMove = true;
        this.board = new GameCell[config.getHeight()][config.getWidth()];
        for (int row = 0; row < config.getHeight(); ++row) {
            for (int col = 0; col < config.getWidth(); ++col) {
                board[row][col] = new GameCell(CellType.Empty);
            }
        }
        this.statusManager = new StatusManager(board);
    }

    public GameCell getCell(int row, int col) {
        return board[row][col];
    }

    public void onClick(int row, int col, boolean isFlagPlace) {
        onClick(row, col, isFlagPlace, new RandomMineGenerator(config));
    }

    // MineGenerator dependency injection for testing
    public void onClick(int row, int col, boolean isFlagPlace, MineGenerator generator) {
        if (this.firstMove) {
            this.onFirstClick(row, col, generator);
            this.firstMove = false;
        }
        board[row][col].handleClick(isFlagPlace);
        if (!isFlagPlace && getBorderCount(row, col) == 0) {
            this.recursiveClearEmpty(row, col, 0);
        }
    }

    private void onFirstClick(int row, int col, MineGenerator generator) {
        Integer[] mines = generator.generateMinePlacements(row, col);
        for (int mine : mines) {
            // wrap it back around
            int mineRow = mine / config.getWidth();
            int mineCol = mine % config.getWidth();
            board[mineRow][mineCol] = new GameCell(CellType.Mine);
        }
        // do it regardless of actual value
        this.recursiveClearEmpty(row, col, 0);
        if (Minesweeper.DEBUG) {
            System.out.println(this);
        }
    }

    // this gets used both for auto-clearing and getting the grid number
    private void forEachNeighbor(int row, int col, BiConsumer<Integer, Integer> consumer) {
        int topBound = Math.max(row - 1, 0);
        int bottomBound = Math.min(row + 1, config.getHeight() - 1);
        int leftBound = Math.max(col - 1, 0);
        int rightBound = Math.min(col + 1, config.getWidth() - 1);

        for (int boundRow = topBound; boundRow <= bottomBound; ++boundRow) {
            for (int boundCol = leftBound; boundCol <= rightBound; ++boundCol) {
                consumer.accept(boundRow, boundCol);
            }
        }
    }

    private boolean isAutoClearableCell(int row, int col) {
        GameCell cell = board[row][col];
        return !cell.isMine() &&
                getBorderCount(row, col) <= 1 &&
                !cell.isConsumed();
    }

    // public only for testing, this would normally be private
    public int autoClearDepth() {
        int boardDim = config.getWidth() * config.getHeight();
        // this is a bit arbitrary but it seems decent for most purposes
        return Math.max(1, boardDim / 25);
    }

    private void recursiveClearEmpty(int row, int col, int depth) {
        if (depth > autoClearDepth()) {
            return;
        }

        forEachNeighbor(row, col, (boundRow, boundCol) -> {
            if (isAutoClearableCell(boundRow, boundCol)) {
                board[boundRow][boundCol].handleClick(false);
                this.recursiveClearEmpty(boundRow, boundCol, depth + 1);
            }
        });
    }

    public int getBorderCount(int row, int col) {
        // intellij suggested this since closure variables must be final
        AtomicInteger acc = new AtomicInteger();
        forEachNeighbor(row, col, (boundRow, boundCol) ->
                acc.addAndGet(board[boundRow][boundCol].getBorderAdder())
        );
        return acc.get();
    }

    public int getFlaggedBombCount() {
        int acc = 0;
        for (GameCell[] row : board) {
            for (GameCell cell : row) {
                if (cell.isChecked()) {
                    ++acc;
                }
            }
        }
        return acc;
    }

    public StatusManager getStatusManager() {
        return statusManager;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (GameCell[] row : board) {
            for (GameCell cell : row) {
                sb.append(cell.toString());
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}

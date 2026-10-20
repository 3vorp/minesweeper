package evorp.minesweeper.game;

public class StatusManager {
    private final GameCell[][] board;

    // only requires readonly access so it can be its own class
    public StatusManager(GameCell[][] board) {
        this.board = board;
    }

    public GameStatus getStatus() {
        if (isGameOver()) {
            return GameStatus.Lost;
        }
        if (hasWon()) {
            return GameStatus.Won;
        }
        return GameStatus.InProgress;
    }

    public boolean isGameOver() {
        for (GameCell[] row : board) {
            for (GameCell cell : row) {
                if (cell.isDetonated()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasWon() {
        // two ways to win: either all cleared or all mines checked
        boolean allCleared = true;
        for (GameCell[] row : board) {
            for (GameCell cell : row) {
                // board has non-consumed cell which isn't a mine
                if (!cell.isConsumed() && !cell.isMine()) {
                    allCleared = false;
                    break;
                }
            }
        }
        if (allCleared) {
            return true;
        }

        boolean allChecked = true;
        for (GameCell[] row : board) {
            for (GameCell cell : row) {
                if (cell.isMine() && !cell.isChecked()) {
                    allChecked = false;
                    break;
                }
                // fix for making sure you can't just check everything
                // it has to just be the right ones
                if (!cell.isMine() && cell.isChecked()) {
                    allChecked = false;
                    break;
                }
            }
        }
        return allChecked;
    }
}

package evorp.minesweeper.game;

public class GameCell {
    private final CellType cellType;
    // clicked, cannot interact with anymore
    private boolean consumed;
    private boolean detonated;
    private boolean checked;

    public GameCell(CellType cellType) {
        this.cellType = cellType;
        this.consumed = false;
        this.checked = false;
        this.detonated = false;
    }

    public void handleClick(boolean isFlagPlace) {
        if (isFlagPlace) {
            this.checked = !this.checked;
        } else {
            this.consumed = true;
            this.consume();
        }
    }

    private void consume() {
        switch (cellType) {
            case Mine:
                this.detonated = true;
                break;
            default:
                break;
        }
    }

    public int getBorderAdder() {
        return switch (cellType) {
            case Empty -> 0;
            case Mine -> 1;
        };
    }

    public CellType getType() {
        return this.cellType;
    }

    public boolean isMine() {
        return this.getType() == CellType.Mine;
    }

    public boolean isChecked() {
        return checked;
    }

    public boolean isConsumed() {
        return consumed;
    }

    public boolean isDetonated() {
        return detonated;
    }

    @Override
    public String toString() {
        // this is really nice for printing out the whole board even if it loses most information
        return switch (cellType) {
            case Mine -> checked ? "+" : "X";
            case Empty -> consumed ? " " : "O";
        };
    }

    public String debugInfo() {
        return
                "{" +
                        "\n\tcellType: " + cellType.toString() +
                        ",\n\tconsumed: " + consumed +
                        ",\n\tchecked: " + checked +
                        ",\n\tgameOver: " + detonated +
                        "\n}";
    }
}

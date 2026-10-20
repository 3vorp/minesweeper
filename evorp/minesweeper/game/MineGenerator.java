package evorp.minesweeper.game;

public interface MineGenerator {
    Integer[] generateMinePlacements(int row, int col);
}

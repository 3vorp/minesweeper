package evorp.minesweeper.game;

import evorp.minesweeper.config.MinesweeperConfig;

import java.util.HashSet;

public final class RandomMineGenerator implements MineGenerator {
    private final MinesweeperConfig config;
    public RandomMineGenerator(MinesweeperConfig config) {
        this.config = config;
    }

    private static int randInt(int max) {
        return (int) (Math.random() * max);
    }

    public Integer[] generateMinePlacements(int row, int col) {
        HashSet<Integer> minePlacement = new HashSet<>();
        int mineCount = config.getMineCount();
        int max = (config.getWidth() * config.getHeight());
        // translate from 2d to 1d
        int firstLocation = (config.getWidth() * col) + row;
        if (mineCount > max) {
            mineCount = max;
        }
        while (minePlacement.size() < mineCount) {
            int nextRand = randInt(max);
            // make sure the first click is safe (bit evil otherwise)
            if (nextRand != firstLocation) {
                minePlacement.add(nextRand);
            }
        }
        return minePlacement.toArray(new Integer[minePlacement.size()]);
    }
}


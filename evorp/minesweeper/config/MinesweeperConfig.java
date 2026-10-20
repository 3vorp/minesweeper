package evorp.minesweeper.config;

import evorp.minesweeper.Minesweeper;

import java.io.Serializable;

public class MinesweeperConfig implements Serializable {
    // can add more config options as needed
    private String name;
    private int height;
    private int width;
    private int mineCount;

    /**
     * really questionable design, but it makes usage way more convenient than with error types
     */
    public String isValid() {
        if (name == null) {
            return "A configuration name is required";
        }
        if (height < 1) {
            return "A board height is required";
        }
        if (width < 1) {
            return "A board width is required";
        }
        if (mineCount < 1) {
            return "A mine count is required";
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        logChange("Name", name);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
        logChange("Height", height);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
        logChange("Width", width);
    }

    public void setSquare(int dim) {
        this.width = dim;
        this.height = dim;
    }

    public int getMineCount() {
        return mineCount;
    }

    public void setMineCount(int mineCount) {
        this.mineCount = mineCount;
        logChange("Mine count", mineCount);
    }

    private static <T> void logChange(String name, T value) {
        if (Minesweeper.DEBUG) {
            System.out.printf("%s changed to %s\n", name, value.toString());
        }
    }

    @Override
    public String toString() {
        return
                "{" +
                        "\n\tname: " + name +
                        ",\n\theight: " + height +
                        ",\n\twidth: " + width +
                        ",\n\tmineCount: " + mineCount +
                        "\n}";

    }
}

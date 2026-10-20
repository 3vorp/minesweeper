package evorp.minesweeper.launcher;

import evorp.minesweeper.config.MinesweeperConfig;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class LoadConfigPanel extends JPanel implements ConfigEditor {
    public static final Dimension LIST_SIZE = new Dimension(
            (int) (MinesweeperLauncher.WIDTH * 0.75), (int) (MinesweeperLauncher.HEIGHT * 0.75)
    );

    private final ConfigList configList;

    public LoadConfigPanel(final Consumer<MinesweeperConfig> onReady) {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(LIST_SIZE);
        this.configList = new ConfigList(onReady);
        JScrollPane scrollPane = new JScrollPane(configList);
        scrollPane.setMaximumSize(LIST_SIZE);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    public void applyConfig() {
        configList.applyConfig();
    }
}

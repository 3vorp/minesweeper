package evorp.minesweeper.launcher;

import evorp.minesweeper.Minesweeper;
import evorp.minesweeper.config.MinesweeperConfig;
import evorp.minesweeper.config.ConfigManager;

import javax.swing.*;
import java.awt.*;
import java.nio.file.FileAlreadyExistsException;
import java.util.function.Consumer;

public class MinesweeperLauncher {
    private MinesweeperConfig config;
    private JPanel panel;

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private static final int CREATE_TAB = 0;
    private static final int LOAD_TAB = 1;

    public void render(JFrame frame, Consumer<MinesweeperConfig> onStart) {
        frame.getContentPane().removeAll();
        panel = new JPanel(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        boolean hasExistingConfigs = ConfigManager.listConfigs().length > 0;

        Consumer<MinesweeperConfig> onSelect = (config) -> {
            this.config = config;
            if (Minesweeper.DEBUG) {
                System.out.println("Selected config:\n" + config.toString());
            }
        };

        CreateConfigPanel createPanel = new CreateConfigPanel(onSelect, hasExistingConfigs);
        LoadConfigPanel loadPanel = new LoadConfigPanel(onSelect);

        tabbedPane.insertTab("Create configuration", null, createPanel, null, CREATE_TAB);
        tabbedPane.insertTab("Load existing preset", null, loadPanel, null, LOAD_TAB);

        if (hasExistingConfigs) {
            tabbedPane.setSelectedIndex(LOAD_TAB);
        }

        tabbedPane.addChangeListener((e) -> {
            // slightly bogus polymorphism usage
            ConfigEditor ce = switch (tabbedPane.getSelectedIndex()) {
                case CREATE_TAB -> createPanel;
                case LOAD_TAB -> loadPanel;
                default -> null;
            };
            if (ce != null) {
                ce.applyConfig();
            }
        });

        panel.add(tabbedPane, BorderLayout.CENTER);
        panel.add(makeButton(onStart, tabbedPane), BorderLayout.PAGE_END);
        frame.add(panel);
    }

    private JButton makeButton(Consumer<MinesweeperConfig> onStart, JTabbedPane tabbedPane) {
        JButton startButton = new JButton("Start game");
        startButton.setPreferredSize(new Dimension(WIDTH, 50));
        startButton.addActionListener((e) -> {
            String message = config.isValid();
            if (message != null) {
                showValidationError(message);
                return;
            }
            // don't rewrite the config if it's only being loaded
            if (tabbedPane.getSelectedIndex() == CREATE_TAB) {
                try {
                    ConfigManager.writeConfig(config);
                } catch (FileAlreadyExistsException err) {
                    showValidationError(err.getMessage());
                    return;
                }
            }

            onStart.accept(config);
        });

        return startButton;
    }

    private void showValidationError(String message) {
        JOptionPane.showMessageDialog(
                panel,
                message,
                "Invalid configuration!",
                JOptionPane.ERROR_MESSAGE
        );
    }
}

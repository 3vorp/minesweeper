package evorp.minesweeper.launcher;

import evorp.minesweeper.config.MinesweeperConfig;
import evorp.minesweeper.config.ConfigManager;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.function.Consumer;

// putting this inline was really ugly
class ConfigListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(
            JList<?> list,
            Object value,
            int index, boolean isSelected,
            boolean cellHasFocus
    ) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        this.setText(((File) value).getName().split("\\.")[0]);
        return this;
    }
}

public class ConfigList extends JList<File> implements ConfigEditor {
    private MinesweeperConfig config;
    private final Consumer<MinesweeperConfig> onReady;

    public ConfigList(final Consumer<MinesweeperConfig> onReady) {
        super(ConfigManager.listConfigs());
        this.onReady = onReady;
        setLayoutOrientation(JList.VERTICAL);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setSelectedIndex(0);

        ListSelectionListener listener = (e) -> {
            if (e.getValueIsAdjusting()) {
                return;
            }
            File selectedFile = this.getSelectedValue();
            try {
                config = ConfigManager.loadConfig(selectedFile);
                applyConfig();
            } catch (FileNotFoundException err) {
                JOptionPane.showMessageDialog(
                        this,
                        err.getMessage(),
                        "Failed to load configuration",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        };

        addListSelectionListener(listener);

        // pre-select first element
        listener.valueChanged(new ListSelectionEvent(this, 0, 0, false));
        setCellRenderer(new ConfigListCellRenderer());
    }

    public void applyConfig() {
        onReady.accept(config);
    }
}

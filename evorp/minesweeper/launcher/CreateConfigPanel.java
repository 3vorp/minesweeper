package evorp.minesweeper.launcher;

import evorp.minesweeper.config.MinesweeperConfig;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.function.Consumer;

public class CreateConfigPanel extends JPanel implements ConfigEditor {
    private final MinesweeperConfig config;
    private final Consumer<MinesweeperConfig> onReady;
    private static final int MINES_DEFAULT = 40;
    private static final int DIM_DEFAULT = 20;

    public CreateConfigPanel(
            final Consumer<MinesweeperConfig> onReady,
            boolean hasExistingConfigs
    ) {
        this.onReady = onReady;
        this.setLayout(new GridLayout(0, 2));
        config = new MinesweeperConfig();
        config.setMineCount(MINES_DEFAULT);
        config.setSquare(DIM_DEFAULT);
        // only emit if we're on the tab to begin with
        if (!hasExistingConfigs) {
            this.applyConfig();
        }
        this.addNameField();
        this.addDimensionFields();
        this.addMineField();
    }

    public void applyConfig() {
        onReady.accept(config);
    }

    private void addNameField() {
        JTextField nameField = new JTextField(config.getName());
        // needed for live typing (I hate this as well)
        nameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                config.setName(nameField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                config.setName(nameField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                config.setName(nameField.getText());
            }
        });
        this.add(new JLabel("Name:"));
        this.add(nameField);
    }

    private void addDimensionFields() {
        final int MIN = 1;
        final int MAX = 99;
        JSlider widthSlider = new JSlider(JSlider.HORIZONTAL, MIN, MAX, DIM_DEFAULT);
        JSlider heightSlider = new JSlider(JSlider.HORIZONTAL, MIN, MAX, DIM_DEFAULT);
        // there's almost certainly a better way to do this
        widthSlider.setMajorTickSpacing(10);
        widthSlider.setMinorTickSpacing(1);
        widthSlider.setPaintTicks(true);
        widthSlider.setPaintLabels(true);
        widthSlider.addChangeListener((e) -> {
            config.setWidth(widthSlider.getValue());
        });
        heightSlider.setMajorTickSpacing(10);
        heightSlider.setMinorTickSpacing(1);
        heightSlider.setPaintTicks(true);
        heightSlider.setPaintLabels(true);
        heightSlider.addChangeListener((e) -> {
            config.setHeight(heightSlider.getValue());
        });
        this.add(new JLabel("Width:"));
        this.add(widthSlider);
        this.add(new JLabel("Height:"));
        this.add(heightSlider);
    }

    private void addMineField() {
        // 24x20 @ 99 mines
        final int MIN = 1;
        final int MAX = 99;
        JSlider mineSlider = new JSlider(JSlider.HORIZONTAL, MIN, MAX, MINES_DEFAULT);
        mineSlider.setMajorTickSpacing(10);
        mineSlider.setMinorTickSpacing(1);
        mineSlider.setPaintTicks(true);
        mineSlider.setPaintLabels(true);
        mineSlider.addChangeListener((e) -> {
            config.setMineCount(mineSlider.getValue());
        });
        this.add(new JLabel("Mines:"));
        this.add(mineSlider);
    }
}

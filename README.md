# Minesweeper

extremely mediocre java swing minesweeper implementation I made in december 2025

## Building

```sh
bash build.sh
```

## Running

```sh
java -jar Minesweeper.jar
```

## Implementation

There's three loose subpackages of organization: configuration handling, the launcher code, and the
actual game code.

1.  The `minesweeper.config` package is only two classes: a static namespace of file utilities, and the actual
    configuration type. `ConfigManager` is the only configuration-related file to interface with I/O, and is hence able to
    hide most of the errors and lower-level control flow from consumers. The `MinesweeperConfig` type is a very
    basic record class storing the savable fields, plus some helper functions for validation and logging.

2.  The `minesweeper.launcher` package is really two main components: the `CreateConfigPanel` and `LoadConfigPanel` tabs,
    which are stored together with the `MinesweeperLauncher` class. `LoadConfigPanel` instantiates a custom `JList` (extends `ConfigList`)
    which uses the `ConfigManager` functions to display a selectable list of available configurations.

    `CreateConfigPanel` is a very simple swing form of text fields and sliders. Both of these propagate events back to the
    main `MinesweeperLauncher` class, which then invokes the `onStart` callback when the configuration is finalized.

3.  `minesweeper.game` also has two main classes: `GameManager` and `GameBoard`. `GameManager` is effectively a combined model and
    controller layer, exposing access to the game board, providing computed properties to get the current game
    status, remaining bombs, etc. `GameManager` has a few child classes for organization (`StatusManager` and `MineGenerator`),
    but they can be treated as functionally part of the main class.

    `GameManager` encapsulates a 2D array of `GameCell` objects, which are either an `Empty` or `Mine` variant according to their
    type field. Each cell is responsible for knowing whether it's been detonated, flagged, and/or consumed.

    `GameBoard` is the actual view, rendering out the board from `GameManager` and providing features like texture rendering
    and UI scaling. `MinesweeperGame` provides a handful of additional features like the status bar and timer, but its
    use of game logic is fairly minimal as the `GameBoard` maintains control of the `GameManager` for the most part.

Finally, the "main" Minesweeper class exposes the root `JFrame` on top of which all this code is built, and invokes
the `render()` methods of both the `MinesweeperGame` and `MinesweeperLauncher` class at the right times.

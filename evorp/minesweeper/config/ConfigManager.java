package evorp.minesweeper.config;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.util.Arrays;

public final class ConfigManager {
    // this is just a namespace
    private ConfigManager() {
    }

    public static final String CONFIG_PATH = PathManager.getPath("config");

    // msc is short for minesweeper config (couldn't think of anything better)
    public static final String CONFIG_EXT = ".msc";

    public static File[] listConfigs() {
        File[] foundFiles = new File(CONFIG_PATH).listFiles();
        if (foundFiles == null) {
            throw new RuntimeException(
                    "Configuration directory not found; this should be impossible.");
        }
        return Arrays
                .stream(foundFiles)
                .filter((f) -> f.getName().endsWith(CONFIG_EXT))
                .toArray(File[]::new);
    }

    public static String nameToPath(String name) {
        return CONFIG_PATH + name + CONFIG_EXT;
    }

    // handy overload
    public static MinesweeperConfig loadConfig(String name) throws FileNotFoundException {
        return loadConfig(new File(nameToPath(name)));
    }

    public static MinesweeperConfig loadConfig(File file) throws FileNotFoundException {
        MinesweeperConfig config;
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            config = (MinesweeperConfig) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException err) {
            throw new FileNotFoundException(
                    "Configuration file invalid or not found: " +
                            file.getName() +
                            "\n\nDetailed error:" +
                            err.getMessage());
        } catch (ClassNotFoundException err) {
            throw new FileNotFoundException("Invalid configuration file format: " + file.getName());
        }
        // return after try/catch so everything closes properly
        return config;
    }

    public static void writeConfig(MinesweeperConfig config) throws FileAlreadyExistsException {
        writeConfig(config, false);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void writeConfig(
            MinesweeperConfig config,
            boolean force) throws FileAlreadyExistsException {
        String filename = config.getName();
        String filepath = nameToPath(filename);
        File f = new File(filepath);
        if (f.exists() && !force) {
            throw new FileAlreadyExistsException(
                    "Configuration file already exists: " + filepath);
        }
        try {
            f.createNewFile();
            FileOutputStream fos = new FileOutputStream(filepath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(config);
            oos.close();
            fos.close();
        } catch (IOException err) {
            err.printStackTrace();
        }
    }
}

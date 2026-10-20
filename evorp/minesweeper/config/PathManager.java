package evorp.minesweeper.config;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class PathManager {
	// this is just a namespace
	private PathManager() {
	}

	private static Path getBasePath() {
		return Paths.get(System.getProperty("user.home"), ".config");
	}

	private static Path getConfigPath() {
		return Paths.get(getBasePath().toString(), "evorp", "minesweeper");
	}

	public static String getPath(String path) {
		return Paths.get(getConfigPath().toString(), path).toString() + "/";
	}
}

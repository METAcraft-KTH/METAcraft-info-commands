package se.datasektionen.mc.infocommands;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;

public class Info implements ModInitializer {

	private static final Path configPath = FabricLoader.getInstance().getConfigDir().resolve("metacraftinfocommands.json");
	private static Config config = null;

	public static final Logger LOGGER = LogManager.getLogger("METAcraft-info-commands");
	@Override
	public void onInitialize() {
		Commands.init();
	}

	/**
	 * Returns the config.
	 * DO NOT CACHE THIS IN VARIABLES FOR LONGER PERIODS OF TIME!
	 * @return The config.
	 */
	public static Config getConfig() {
		if (config == null) {
			config = Config.loadConfig(configPath).orElseGet(() -> {
				var file = configPath.toFile();
				if (file.exists()) {
					LOGGER.error("Unable to load existing config, backing up and creating new default config.");
					Path target = configPath.getParent().resolve("metacraftinfocommands.bak.json");
					int num = 1;
					while (target.toFile().exists()) {
						target = configPath.getParent().resolve("metacraftinfocommands.bak" + num++ + ".json");
						if (num > 10) {
							break;
						}
					}
					try {
						Files.copy(configPath, target, StandardCopyOption.REPLACE_EXISTING);
					} catch (IOException err) {
						LOGGER.fatal("Unable to backup config file! You may have lost stuff!");
						err.printStackTrace();
					}
				}

				var config = new Config();
				var sub = new HashMap<String, InfoNode>();
				config.getCommands().put("example1", new InfoNode(Text.literal("Test"), sub));
				sub.put("example3", new InfoNode(Text.literal("Look")));
				var sub2 = new HashMap<String, InfoNode>();
				sub2.put("example5", new InfoNode(Text.literal("Without limits")));
				sub.put("example4", new InfoNode(Text.literal("It's ").append(Text.literal("recursive.")), sub2));
				config.getCommands().put("example2", new InfoNode(
						Text.literal("And it supports JSON text! ").setStyle(
								Style.EMPTY.withBold(true).withItalic(true).withColor(0xccffff)
						).append(Text.literal("Cool right?").setStyle(Style.EMPTY.withObfuscated(true)))
				));
				config.saveConfig(configPath);
				return config;
			});
		}
		return config;
	}

	public static void reloadConfig() {
		config = null;
	}
}

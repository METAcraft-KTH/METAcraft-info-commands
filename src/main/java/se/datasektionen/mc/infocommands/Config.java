package se.datasektionen.mc.infocommands;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Config {

	public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.unboundedMap(Codec.STRING, InfoNode.CODEC).fieldOf("commands").forGetter(Config::getCommands),
			Codec.BOOL.fieldOf("resendCommandTreeOnReload").forGetter(Config::resendCommandTreeOnReload),
			Codec.BOOL.fieldOf("enableResendCommandTreeCommand").forGetter(Config::enableResendCommandTreeCommand)
	).apply(instance, Config::new));

	private final Map<String, InfoNode> commands;
	private boolean resendCommandTreeOnReload;
	private boolean enableResendCommandTreeCommand;

	public Config(Map<String, InfoNode> commands, boolean resendCommandTreeOnReload, boolean enableResendCommandTreeCommand) {
		this.commands = commands;
		this.resendCommandTreeOnReload = resendCommandTreeOnReload;
		this.enableResendCommandTreeCommand = enableResendCommandTreeCommand;
	}

	public Config() {
		this(new HashMap<>(), true, false);
	}

	public Map<String, InfoNode> getCommands() {
		return commands;
	}

	public boolean resendCommandTreeOnReload() {
		return resendCommandTreeOnReload;
	}

	public boolean enableResendCommandTreeCommand() {
		return enableResendCommandTreeCommand;
	}

	public static Optional<Config> loadConfig(Path configPath) {
		File file = configPath.toFile();
		if (!file.exists()) return Optional.empty();
		try (var reader = new BufferedReader(new FileReader(file))) { //The BufferedReader is to boost performance.
			JsonElement element = JsonParser.parseReader(reader);
			return CODEC.parse(JsonOps.INSTANCE, element).resultOrPartial(Info.LOGGER::error);
		} catch (IOException error) {
			error.printStackTrace();
		}
		return Optional.empty();
	}

	public void saveConfig(Path configPath) {
		CODEC.encodeStart(JsonOps.INSTANCE, this).resultOrPartial(Info.LOGGER::error).ifPresent(data -> {
			File file = configPath.toFile();
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException err) {
					err.printStackTrace();
				}
			}
			//The BufferedWriter is to boost performance.
			try (var writer = new JsonWriter(new BufferedWriter(new FileWriter(file)))) {
				writer.setIndent("\t");
				Streams.write(data, writer);
			} catch (IOException error) {
				error.printStackTrace();
			}
		});
	}

}

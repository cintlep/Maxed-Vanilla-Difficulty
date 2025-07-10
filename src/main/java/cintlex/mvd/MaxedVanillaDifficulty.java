package cintlex.mvd;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MaxedVanillaDifficulty implements ModInitializer {
	public static final String MOD_ID = "maxed-vanilla-difficulty";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static Config CONFIG = new Config();

	@Override
	public void onInitialize() {
		readconfig();
		LOGGER.info("Maxed Vanilla Difficulty is ready");

		ServerTickEvents.END_SERVER_TICK.register(this::checktick);
	}

	private void checktick(MinecraftServer server) {
	}

	private void readconfig() {
		Path configPath = FabricLoader.getInstance().getConfigDir().resolve(MOD_ID + ".json");

		if (Files.exists(configPath)) {
			try {
				String configJson = Files.readString(configPath);
				CONFIG = new Gson().fromJson(configJson, Config.class);
			} catch (IOException ignored) {
			}
		} else {
			savesettings();
		}
	}

	private void savesettings() {
		Path configPath = FabricLoader.getInstance().getConfigDir().resolve(MOD_ID + ".json");

		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String configJson = gson.toJson(CONFIG);
			Files.writeString(configPath, configJson);
		} catch (IOException ignored) {
		}
	}
}
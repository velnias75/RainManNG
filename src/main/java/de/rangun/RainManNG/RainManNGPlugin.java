package de.rangun.RainManNG;

import java.util.Random;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion.Target;
import org.bukkit.plugin.java.annotation.plugin.Description;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.Website;
import org.bukkit.plugin.java.annotation.plugin.author.Author;

import de.rangun.RainManNG.commands.RainManNGCommand;
import de.rangun.RainManNG.commands.WeatherCommand;

@Plugin(name = "RainManNG", version = "1.1-SNAPSHOT")
@Description(value = "Bukkit plugin for controlling rain frequency and length.")
@Website(value = "https://github.com/velnias75/RainManNG")
@ApiVersion(Target.v1_15)
@Author(value = "ABCRic")
@Author(value = "Velnias75")
@Command(name = "rainmanng", desc = "This is the global plugin command.", usage = "/rainmanng <command>", permission = "rainmanng.admin")
@Command(name = "weather", desc = "Sets the weather.", usage = "/weather (clear|rain|thunder) [<duration>]", permission = "rainmanng.weather")
public class RainManNGPlugin extends JavaPlugin implements Listener {

	private final static Random random = new Random();

	// config values
	private double rainChance;
	private double lengthScale;
	private boolean weatherEnabled;
	private boolean tmpWeatherDisabled = false;

	@Override
	public void onEnable() {

		getLogger().info("Starting up " + getDescription().getName() + " " + getDescription().getVersion() + " by "
				+ String.join(", ", getDescription().getAuthors()) + "...");

		getServer().getPluginManager().registerEvents(this, this);

		getCommand("rainmanng").setExecutor(new RainManNGCommand(this));
		getCommand("weather").setExecutor(new WeatherCommand(this));

		saveDefaultConfig();
		loadConfigValues();

		getLogger().info("Enabled.");
	}

	public void loadConfigValues() {

		FileConfiguration config = getConfig();

		weatherEnabled = config.getBoolean("weather-enabled", true);
		rainChance = config.getDouble("rain-chance", 1);
		lengthScale = config.getDouble("rain-length-scale", 1);
	}

	public boolean isWeatherEnabled() {
		return weatherEnabled;
	}

	public void temporaryDisableWeather(boolean b) {
		tmpWeatherDisabled = b;
	}

	@EventHandler(ignoreCancelled = true)
	public void onWeatherChange(WeatherChangeEvent event) {

		if (!tmpWeatherDisabled) {

			tmpWeatherDisabled = false;

			// if it's gonna rain
			if (event.toWeatherState()) {

				if (!weatherEnabled || random.nextDouble() > rainChance || lengthScale <= 0d) {
					event.setCancelled(true);
					return;
				}

				event.getWorld().setWeatherDuration((int) (event.getWorld().getWeatherDuration() * lengthScale));
			}

		} else if (weatherEnabled) {
			getLogger().info("Weather change forced.");
		}

	}
}

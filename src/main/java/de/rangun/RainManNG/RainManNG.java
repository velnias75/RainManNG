package de.rangun.RainManNG;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class RainManNG extends JavaPlugin implements Listener, CommandExecutor {
	
	public Random random;

	// config values
	public double rainChance;
	public double lengthScale;
	public boolean weatherEnabled;

	@Override
	public void onEnable() {

		getLogger().info("Starting up " + getDescription().getName() + " " + getDescription().getVersion() + " by "
				+ String.join(", ", getDescription().getAuthors()) + "…");

		getServer().getPluginManager().registerEvents(this, this);
		getCommand("rainmanng").setExecutor(this);
		random = new Random();
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

	@EventHandler(ignoreCancelled = true)
	public void onWeatherChange(WeatherChangeEvent event) {

		// if it's gonna rain
		if (event.toWeatherState()) {

			if (!weatherEnabled || random.nextDouble() > rainChance || lengthScale <= 0d) {
				event.setCancelled(true);
				return;
			}

			event.getWorld().setWeatherDuration((int) (event.getWorld().getWeatherDuration() * lengthScale));
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (args.length >= 1) {

			// /rainmanng reload
			if (args[0].equalsIgnoreCase("reload")) {

				if (sender.hasPermission("rainmanng.admin")) {
					
					reloadConfig();
					loadConfigValues();
					sender.sendMessage("[" + getDescription().getName() + "] Config reloaded.");
					
				} else {
					sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
				}
				return true;

			} else {
				sendHelp(sender);
				return true;
			}

		} else {
			sendHelp(sender);
			return true;
		}
	}

	public void sendHelp(CommandSender sender) {
		sender.sendMessage("Usage: /rainmanng <command>");
		sender.sendMessage("reload - Reloads the configuration");
	}
}

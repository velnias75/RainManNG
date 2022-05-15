package de.rangun.RainManNG.commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import de.rangun.RainManNG.RainManNGPlugin;

public class WeatherCommand extends AbstractCommand {

	@SuppressWarnings("serial")
	public WeatherCommand(final RainManNGPlugin plugin) {
		super(plugin, new ArrayList<String>() {
			{
				add("clear");
				add("rain");
				add("thunder");
			}
		});
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (args.length == 0) {
			return false;
		}

		if (plugin.isWeatherEnabled()) {

			plugin.temporaryDisablePlugin(true);

			if (plugin.isDebugEnabled()) {
				plugin.getLogger().info("Temporary disabling plugin for weather forced to change.");
			}

			return plugin.getServer().dispatchCommand(sender, "minecraft:weather " + String.join(" ", args));

		} else {
			sender.sendMessage(ChatColor.RED + "Weather is disabled on this server!");
		}

		return true;
	}
}

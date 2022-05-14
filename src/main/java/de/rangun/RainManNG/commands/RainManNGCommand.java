package de.rangun.RainManNG.commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import de.rangun.RainManNG.RainManNGPlugin;

public class RainManNGCommand extends AbstractCommand {

	@SuppressWarnings("serial")
	public RainManNGCommand(final RainManNGPlugin plugin) {
		super(plugin, new ArrayList<String>() {
			{
				add("reload");
			}
		});
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length >= 1) {

			// /rainmanng reload
			if ("reload".equalsIgnoreCase(args[0])) {

				if (sender.hasPermission("rainmanng.admin")) {

					plugin.reloadConfig();
					plugin.loadConfigValues();
					plugin.temporaryDisableWeather(false);

					sender.sendMessage("[" + plugin.getDescription().getName() + "] Config reloaded.");

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

	private void sendHelp(CommandSender sender) {
		sender.sendMessage("Usage: /rainmanng <command>");
		sender.sendMessage("reload - Reloads the configuration");
	}
}

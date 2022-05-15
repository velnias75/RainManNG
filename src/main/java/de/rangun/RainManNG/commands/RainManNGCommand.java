package de.rangun.RainManNG.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import de.rangun.RainManNG.RainManNGPlugin;

public class RainManNGCommand extends AbstractCommand {

	@SuppressWarnings("serial")
	public RainManNGCommand(final RainManNGPlugin plugin) {
		super(plugin, new ArrayList<String>() {
			{
				add("disable-weather");
				add("reload");
				add("save");
				add("show-config");
				add("rain-chance");
				add("rain-length-scale");
			}
		});
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (args.length >= 1 && sender.hasPermission("rainmanng.admin")) {

			// /rainmanng reload
			if ("reload".equalsIgnoreCase(args[0])) {

				plugin.reloadConfig();
				plugin.loadConfigValues();
				plugin.temporaryDisablePlugin(false);

				sender.sendMessage("" + ChatColor.GRAY + ChatColor.ITALIC + "[" + plugin.getDescription().getName()
						+ ": config reloaded]");

				return true;

			} else if ("save".equalsIgnoreCase(args[0])) {

				final FileConfiguration config = plugin.getConfig();

				config.set("weather-enabled", plugin.isWeatherEnabled());
				config.set("rain-chance", plugin.getRainChance());
				config.set("rain-length-scale", plugin.getRainLengthScale());

				plugin.saveConfig();

				sender.sendMessage("" + ChatColor.GRAY + ChatColor.ITALIC + "[" + plugin.getDescription().getName()
						+ ": config saved]");

				return true;

			} else if ("rain-chance".equalsIgnoreCase(args[0])) {

				if (args.length > 1) {

					double rc = plugin.getRainChance();

					try {

						rc = Double.parseDouble(args[1]);

					} catch (NumberFormatException ex) {
						sendInvalidValue(sender, args[1]);
					}

					if (rc < 0.0d || rc > 1.0d) {
						plugin.sendInvalidRainChance(sender, rc);
					} else {
						plugin.setRainChance(rc);
					}
				}

				sendValue(sender, "rain-chance", plugin.getRainChance());

				return true;

			} else if ("rain-length-scale".equalsIgnoreCase(args[0])) {

				if (args.length > 1) {

					double rls = plugin.getRainLengthScale();

					try {

						rls = Double.parseDouble(args[1]);

					} catch (NumberFormatException ex) {
						sendInvalidValue(sender, args[1]);
					}

					plugin.setRainLengthScale(rls);
				}

				sendValue(sender, "rain-length-scale", plugin.getRainLengthScale());

				return true;

			} else if ("disable-weather".equalsIgnoreCase(args[0])) {

				if (args.length > 1) {

					boolean disabled = !plugin.isWeatherEnabled();

					try {

						if ("true".equals(args[1]) || "false".equals(args[1])) {
							disabled = Boolean.parseBoolean(args[1]);
						} else {
							sendInvalidValue(sender, args[1]);
						}

					} catch (NumberFormatException ex) {
						sendInvalidValue(sender, args[1]);
					}

					plugin.setWeatherEnabled(!disabled);
				}

				sendWeatherEnabled(sender);

				return true;

			} else if ("show-config".equalsIgnoreCase(args[0])) {

				sendWeatherEnabled(sender);
				sendValue(sender, "rain-chance", plugin.getRainChance());
				sendValue(sender, "rain-length-scale", plugin.getRainLengthScale());

				return true;
			}

		} else if (!sender.hasPermission("rainmanng.admin")) {
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
			return true;
		}

		return false;
	}

	private void sendInvalidValue(final CommandSender sender, final String value) {
		sender.sendMessage(ChatColor.RED + "Invalid value: \"" + value + "\"");
	}

	private void sendValue(final CommandSender sender, final String name, final double value) {
		sender.sendMessage("" + ChatColor.GRAY + ChatColor.ITALIC + "[" + plugin.getDescription().getName() + ": "
				+ name + " = " + value + "]");
	}

	private void sendWeatherEnabled(final CommandSender sender) {
		sender.sendMessage("" + ChatColor.GRAY + ChatColor.ITALIC + "[" + plugin.getDescription().getName()
				+ ": weather-enabled = " + plugin.isWeatherEnabled() + "]");
	}

	@SuppressWarnings("serial")
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

		if (args.length >= 1 && args.length < 3 && "disable".equalsIgnoreCase(args[0])) {

			return new ArrayList<String>() {
				{
					add("true");
					add("false");
				}
			};

		} else {
			return super.onTabComplete(sender, command, alias, args);
		}
	}
}

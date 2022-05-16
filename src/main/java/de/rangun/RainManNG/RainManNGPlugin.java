/*
 * Copyright 2022 by Heiko Schäfer <heiko@rangun.de>
 *
 * This file is part of RainManNG.
 *
 * RainManNG is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * RainManNG is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with RainManNG.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.rangun.RainManNG;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
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

@Plugin(name = "RainManNG", version = "1.2-SNAPSHOT")
@Description(value = "Bukkit plugin for controlling rain frequency and length.")
@Website(value = "https://github.com/velnias75/RainManNG")
@ApiVersion(Target.v1_15)
@Author(value = "ABCRic")
@Author(value = "Velnias75")
@Command(name = "rainmanng", desc = "Set, show, save or reload plugin config.", usage = "/rainmanng (disable-weather|reload|save|show-config|rain-chance [<value>]|rain-length-scale [<value>])", permission = "rainmanng.admin")
@Command(name = "weather", desc = "Sets the weather.", usage = "/weather (clear|rain|thunder) [<duration>]", permission = "rainmanng.weather")
//@Permission(name = "rainmanng.sendweatherreport", desc = "Displays whether raining was prevented or not to the user's client", defaultValue = PermissionDefault.FALSE)
public final class RainManNGPlugin extends JavaPlugin implements Listener {

	private final static Random random = new Random();

	// config values
	private double rainChance;
	private double lengthScale;
	private boolean weatherEnabled;
	private boolean tmpPluginDisabled = false;
	private boolean debug = false;

	@Override
	public void onEnable() {

		getLogger().info("Starting up " + getDescription().getName() + " " + getDescription().getVersion() + " by "
				+ String.join(", ", getDescription().getAuthors()) + "...");

		getServer().getPluginManager().registerEvents(this, this);

		getCommand("rainmanng").setExecutor(new RainManNGCommand(this));
		getCommand("weather").setExecutor(new WeatherCommand(this));

		saveDefaultConfig();
		loadConfigValues();

		final int pluginId = 15206;
		new Metrics(this, pluginId);

		final PluginManager pm = getServer().getPluginManager();
		final Set<Permission> permissions = pm.getPermissions();
		final Permission perm = new Permission("rainmanng.sendweatherreport",
				"Displays whether raining was prevented or not to the user's client", PermissionDefault.FALSE);

		if (!permissions.contains(perm)) {
			pm.addPermission(perm);
		}

		getLogger().info("Enabled.");
	}

	public void loadConfigValues() {

		FileConfiguration config = getConfig();

		weatherEnabled = config.getBoolean("weather-enabled", true);
		rainChance = config.getDouble("rain-chance", 1);
		lengthScale = config.getDouble("rain-length-scale", 1);
		debug = config.getBoolean("debug", false);

		if (rainChance < 0.0d || rainChance > 1.0d) {
			sendInvalidRainChance(null, lengthScale);
			rainChance = 1.0d;
		}
	}

	public boolean isDebugEnabled() {
		return debug;
	}

	public boolean isWeatherEnabled() {
		return weatherEnabled;
	}

	public void setWeatherEnabled(boolean b) {
		weatherEnabled = b;
	}

	public void temporaryDisablePlugin(boolean b) {
		tmpPluginDisabled = b;
	}

	public double getRainChance() {
		return rainChance;
	}

	public void setRainChance(double rc) {
		rainChance = rc;
	}

	public double getRainLengthScale() {
		return lengthScale;
	}

	public void setRainLengthScale(double rls) {
		lengthScale = rls;
	}

	public void sendInvalidRainChance(final CommandSender sender, final double value) {

		final String str = "rain-chance must be between 0.0 and 1.0 (" + value + ")";

		if (sender != null) {
			sender.sendMessage(ChatColor.RED + str);
		} else {
			getLogger().warning(str + ". Setting it to 1.0.");
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onWeatherChange(WeatherChangeEvent event) {

		final List<Player> lp = new ArrayList<Player>();

		for (Player p : Bukkit.getOnlinePlayers()) {

			if (p.hasPermission("rainmanng.sendweatherreport")) {
				lp.add(p);
			}
		}

		if (!tmpPluginDisabled) {

			// if it's gonna rain
			if (event.toWeatherState()) {

				if (!weatherEnabled || random.nextDouble() > rainChance || lengthScale <= 0d) {
					event.setCancelled(true);

					final String prevented = "Prevented weather from raining (rain chance: " + rainChance + ")";

					if (isDebugEnabled()) {
						getLogger().info(prevented);
					}

					for (Player p : lp) {
						p.sendMessage(
								"" + ChatColor.GRAY + ChatColor.ITALIC + "[" + getName() + ": " + prevented + "]");
					}

					tmpPluginDisabled = false;
					return;

				} else if (weatherEnabled) {

					final String raining = "Allowed weather to rain (rain chance: " + rainChance + ")";

					if (isDebugEnabled()) {
						getLogger().info(raining);
					}

					for (Player p : lp) {
						p.sendMessage("" + ChatColor.GRAY + ChatColor.ITALIC + "[" + getName() + ": " + raining + "]");
					}
				}

				event.getWorld().setWeatherDuration((int) (event.getWorld().getWeatherDuration() * lengthScale));
			}
		}

		tmpPluginDisabled = false;
	}
}

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
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletionException;

import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import de.rangun.RainManNG.commands.RainManNGCommand;
import de.rangun.RainManNG.commands.WeatherCommand;
import dev.derklaro.spiget.SpigetClient;
import dev.derklaro.spiget.http.java8.Java8SpigetClient;
import dev.derklaro.spiget.mapper.gson.GsonMapper;
import dev.derklaro.spiget.model.Resource;
import dev.derklaro.spiget.model.Version;

public final class RainManNGPlugin extends JavaPlugin implements Listener {

	private final static Random random = new Random();

	private final SpigetClient spigetClient = new Java8SpigetClient(GsonMapper.INSTANCE);

	private List<String> joinMessages = new ArrayList<>(2);

	// config values
	private double rainChance;
	private double lengthScale;
	private boolean weatherEnabled;
	private boolean tmpPluginDisabled = false;
	private boolean offlineWeather = false;
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
		final Metrics metrics = new Metrics(this, pluginId);

		metrics.addCustomChart(new SimplePie("weatherEnabled", () -> {
			return "" + isWeatherEnabled();
		}));

		metrics.addCustomChart(new SimplePie("offlineWeather", () -> {
			return "" + hasOfflineWeather();
		}));

		getLogger().info("Enabled.");

		try {

			final int id = 102026;

			final Version latestVersion = spigetClient.latestResourceVersion().resourceId(id).exec().join();
			final Resource resourceDetails = spigetClient.resourceDetails().resourceId(id).exec().join();
			final String currentVersion = getDescription().getVersion();

			if (!currentVersion.endsWith("-SNAPSHOT") && !currentVersion.equals(latestVersion.name())) {

				final String verMsg1 = "A newer version of " + getDescription().getName() + " is available: "
						+ latestVersion.name();
				final String verMsg2 = "Download: " + resourceDetails.file().externalUrl();

				getLogger().warning(verMsg1);
				getLogger().warning(verMsg2);

				joinMessages.add(verMsg1);
				joinMessages.add(verMsg2);

			} else if (currentVersion.endsWith("-SNAPSHOT")) {

				final String verMsg1 = "You are using a development version.";
				final String verMsg2 = "Please report any issues here: "
						+ resourceDetails.links().get("alternativeSupport");

				getLogger().warning(verMsg1);
				getLogger().warning(verMsg2);

				joinMessages.add(verMsg1);
				joinMessages.add(verMsg2);
			}

		} catch (CompletionException e) {
			getLogger().warning("Couldn't retrieve latest version.");
		}

		getServer().getPluginManager().registerEvents(new JoinListener(this), this);
	}

	public void loadConfigValues() {

		FileConfiguration config = getConfig();

		weatherEnabled = config.getBoolean("weather-enabled", true);
		offlineWeather = config.getBoolean("offline-weather", false);
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

	public boolean hasOfflineWeather() {
		return offlineWeather;
	}

	public void setOfflineWeather(boolean b) {
		offlineWeather = b;
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
		final Collection<? extends Player> op = Bukkit.getOnlinePlayers();

		for (Player p : op) {

			if (p.hasPermission("rainmanng.sendweatherreport")) {
				lp.add(p);
			}
		}

		if (!isOfflineWeatherOrTemporaryDisabled(op.size() != 0)) {

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

	private boolean isOfflineWeatherOrTemporaryDisabled(final boolean online) {

		final boolean ow = (offlineWeather && !online);

		if (isDebugEnabled() && !tmpPluginDisabled && isWeatherEnabled() && ow) {
			getLogger().info("Allowing weather to change due to nobody is online.");
		}

		return tmpPluginDisabled || ow;
	}

	List<String> getJoinMessages() {
		return joinMessages;
	}
}

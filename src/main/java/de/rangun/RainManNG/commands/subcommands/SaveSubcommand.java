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

package de.rangun.RainManNG.commands.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import de.rangun.RainManNG.RainManNGPlugin;

/**
 * @author heiko
 *
 */
final class SaveSubcommand extends AbstractRainManNGSubcommand {

	protected SaveSubcommand(final RainManNGPlugin plugin, final String[] args) {
		super(plugin, args);
	}

	@Override
	protected void doCommand(CommandSender sender) {

		final FileConfiguration config = plugin.getConfig();

		config.set("weather-enabled", plugin.isWeatherEnabled());
		config.set("rain-chance", plugin.getRainChance());
		config.set("rain-length-scale", plugin.getRainLengthScale());
		config.set("offline-weather", plugin.hasOfflineWeather());

		plugin.saveConfig();

		sender.sendMessage(msgBase + "config saved]");

	}

}

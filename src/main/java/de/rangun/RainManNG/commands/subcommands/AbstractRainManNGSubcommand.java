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

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import de.rangun.RainManNG.RainManNGPlugin;

/**
 * @author heiko
 *
 */
abstract class AbstractRainManNGSubcommand implements IRainManNGSubcommand {

	protected final RainManNGPlugin plugin;
	protected final String[] args;
	protected final String msgBase;

	protected AbstractRainManNGSubcommand(final RainManNGPlugin plugin, final String[] args) {

		this.plugin = plugin;
		this.args = args;
		this.msgBase = "" + ChatColor.GRAY + ChatColor.ITALIC + "[" + this.plugin.getDescription().getName() + ": ";
	}

	@Override
	public boolean execute(final CommandSender sender) {

		doCommand(sender);

		return true;
	}

	@Override
	public boolean isBooleanCommand() {
		return false;
	}

	abstract protected void doCommand(final CommandSender sender);

	protected void sendInvalidValue(final CommandSender sender, final String value) {
		sender.sendMessage(ChatColor.RED + "Invalid value: \"" + value + "\"");
	}

	protected void sendValue(final CommandSender sender, final String name, final double value) {
		sender.sendMessage(msgBase + name + " = " + value + "]");
	}

	protected void sendWeatherEnabled(final CommandSender sender) {
		sender.sendMessage(msgBase + "weather-enabled = " + plugin.isWeatherEnabled() + "]");
	}

	protected void sendOfflineWeather(final CommandSender sender) {
		sender.sendMessage(msgBase + "offline-weather = " + plugin.hasOfflineWeather() + "]");
	}
}

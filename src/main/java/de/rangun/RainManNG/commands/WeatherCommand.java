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

package de.rangun.RainManNG.commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import de.rangun.RainManNG.RainManNGPlugin;

public final class WeatherCommand extends AbstractCommand {

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

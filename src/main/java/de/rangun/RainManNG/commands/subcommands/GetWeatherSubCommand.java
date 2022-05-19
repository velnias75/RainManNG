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
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import de.rangun.RainManNG.RainManNGPlugin;

/**
 * @author heiko
 *
 */
final class GetWeatherSubCommand extends AbstractRainManNGSubcommand {

	protected GetWeatherSubCommand(final RainManNGPlugin plugin, final String[] args) {
		super(plugin, args);
	}

	@Override
	protected void doCommand(final CommandSender sender) {

		for (World w : plugin.getServer().getWorlds()) {

			if (w.hasStorm()) {
				sender.sendMessage(msgBase + "It is raining at the moment.]");
				return;
			}
		}

		sender.sendMessage(
				msgBase + "It is " + ChatColor.BOLD + "not" + ChatColor.RESET + ChatColor.GRAY + " raining.]");
	}
}

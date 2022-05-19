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
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import de.rangun.RainManNG.RainManNGPlugin;
import de.rangun.RainManNG.commands.subcommands.RainManNGSubcommandFactory;

public final class RainManNGCommand extends AbstractCommand {

	public RainManNGCommand(final RainManNGPlugin plugin) {
		super(plugin, RainManNGSubcommandFactory.getInstance().getSubcommands());
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label,
			final String[] args) {

		if (args.length >= 1 && sender.hasPermission("rainmanng.admin")) {

			try {
				return RainManNGSubcommandFactory.getInstance().createSubcommand(plugin, args).execute(sender);
			} catch (IllegalArgumentException e) {
				return false;
			}

		} else if (!sender.hasPermission("rainmanng.admin")) {
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
			return true;
		}

		return false;
	}

	private boolean isBooleanSubcommand(final String[] args) {

		boolean b = false;

		for (String sc : RainManNGSubcommandFactory.getInstance().getSubcommands()) {
			b |= (sc.equalsIgnoreCase(args[0])
					&& RainManNGSubcommandFactory.getInstance().createSubcommand(plugin, sc, args).isBooleanCommand());
		}

		return b;
	}

	@SuppressWarnings("serial")
	@Override
	public List<String> onTabComplete(final CommandSender sender, final Command command, final String alias,
			final String[] args) {

		if (args.length >= 1 && args.length < 3 && isBooleanSubcommand(args)) {

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

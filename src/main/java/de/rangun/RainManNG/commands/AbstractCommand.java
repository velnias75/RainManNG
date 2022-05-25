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

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import com.google.common.collect.ImmutableList;

import de.rangun.RainManNG.RainManNGPlugin;

abstract class AbstractCommand implements CommandExecutor, TabCompleter {

	private final List<String> cmd_args;

	protected final RainManNGPlugin plugin;

	public AbstractCommand(final RainManNGPlugin plugin, final List<String> cmd_args) {
		this.plugin = plugin;
		this.cmd_args = cmd_args;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

		if (args.length > 0 && args.length < 2 && args[0].length() > 0) {

			List<String> sugg = new ArrayList<>(cmd_args.size());

			for (String string : cmd_args) {
				if (StringUtil.startsWithIgnoreCase(string, args[0])) {
					sugg.add(string);
				}
			}

			return ImmutableList.copyOf(sugg);
		}

		return args.length >= 2 ? ImmutableList.of() : cmd_args;
	}

}

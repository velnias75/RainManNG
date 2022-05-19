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

import de.rangun.RainManNG.RainManNGPlugin;

/**
 * @author heiko
 *
 */
final class RainChanceSubcommand extends AbstractRainManNGSubcommand {

	protected RainChanceSubcommand(final RainManNGPlugin plugin, final String[] args) {
		super(plugin, args);
	}

	@Override
	protected void doCommand(final CommandSender sender) {

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
	}
}

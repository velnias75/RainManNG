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

import de.rangun.RainManNG.RainManNGPlugin;

/**
 * @author heiko
 *
 */
public final class RainManNGSubcommandFactory {

	private static RainManNGSubcommandFactory instance = null;

	private RainManNGSubcommandFactory() {
	}

	public static RainManNGSubcommandFactory getInstance() {

		if (instance == null) {
			instance = new RainManNGSubcommandFactory();
		}

		return instance;
	}

	public IRainManNGSubcommand createSubcommand(final RainManNGPlugin plugin, final String[] args) {

		if ("reload".equalsIgnoreCase(args[0])) {
			return new ReloadSubcommand(plugin, args);
		} else if ("get-weather".equalsIgnoreCase(args[0])) {
			return new GetWeatherSubCommand(plugin, args);
		} else if ("save".equalsIgnoreCase(args[0])) {
			return new SaveSubcommand(plugin, args);
		} else if ("rain-chance".equalsIgnoreCase(args[0])) {
			return new RainChanceSubcommand(plugin, args);
		} else if ("rain-length-scale".equalsIgnoreCase(args[0])) {
			return new RainLengthScaleSubcommand(plugin, args);
		} else if ("offline-weather".equalsIgnoreCase(args[0])) {
			return new OfflineWeatherSubcommand(plugin, args);
		} else if ("disable-weather".equalsIgnoreCase(args[0])) {
			return new DisableWeatherSubcommand(plugin, args);
		} else if ("show-config".equalsIgnoreCase(args[0])) {
			return new ShowConfigSubcommand(plugin, args);
		}

		throw new IllegalArgumentException("Sub command \"" + args[0] + "\" not implemented.");
	}
}

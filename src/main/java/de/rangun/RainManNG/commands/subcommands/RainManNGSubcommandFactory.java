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

import java.util.List;

import com.google.common.collect.ImmutableList;

import de.rangun.RainManNG.RainManNGPlugin;

/**
 * @author heiko
 *
 */
public final class RainManNGSubcommandFactory {

	private final static List<String> SUBCOMMANDS = ImmutableList.of("disable-weather", "offline-weather", "reload",
			"save", "show-config", "rain-chance", "rain-length-scale", "get-weather");

	private static RainManNGSubcommandFactory instance = null;

	private RainManNGSubcommandFactory() {
	}

	public static RainManNGSubcommandFactory getInstance() {

		if (instance == null) {
			instance = new RainManNGSubcommandFactory();
		}

		return instance;
	}

	public List<String> getSubcommands() {
		return SUBCOMMANDS;
	}

	public IRainManNGSubcommand createSubcommand(final RainManNGPlugin plugin, final String[] args) {
		return createSubcommand(plugin, args[0], args);
	}

	public IRainManNGSubcommand createSubcommand(final RainManNGPlugin plugin, final String sc, final String[] args) {

		if (SUBCOMMANDS.get(2).equalsIgnoreCase(sc)) {
			return new ReloadSubcommand(plugin, args);
		} else if (SUBCOMMANDS.get(7).equalsIgnoreCase(sc)) {
			return new GetWeatherSubCommand(plugin, args);
		} else if (SUBCOMMANDS.get(3).equalsIgnoreCase(sc)) {
			return new SaveSubcommand(plugin, args);
		} else if (SUBCOMMANDS.get(5).equalsIgnoreCase(sc)) {
			return new RainChanceSubcommand(plugin, args);
		} else if (SUBCOMMANDS.get(6).equalsIgnoreCase(sc)) {
			return new RainLengthScaleSubcommand(plugin, args);
		} else if (SUBCOMMANDS.get(1).equalsIgnoreCase(sc)) {
			return new OfflineWeatherSubcommand(plugin, args);
		} else if (SUBCOMMANDS.get(0).equalsIgnoreCase(sc)) {
			return new DisableWeatherSubcommand(plugin, args);
		} else if (SUBCOMMANDS.get(4).equalsIgnoreCase(sc)) {
			return new ShowConfigSubcommand(plugin, args);
		}

		throw new IllegalArgumentException("Sub command \"" + sc + "\" not implemented.");
	}
}

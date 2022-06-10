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

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import de.rangun.spiget.MessageRetriever;

final class JoinListener implements Listener {

	private final RainManNGPlugin plugin;
	private final MessageRetriever msgs;

	public JoinListener(final RainManNGPlugin plugin, final MessageRetriever msgs) {
		this.plugin = plugin;
		this.msgs = msgs;
	}

	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent event) {

		if (event.getPlayer().hasPermission("rainmanng.admin")) {

			for (String jm : msgs.getJoinMessages()) {
				event.getPlayer().sendMessage("" + ChatColor.YELLOW + ChatColor.ITALIC + "["
						+ plugin.getDescription().getName() + ": " + jm + "]");
			}
		}
	}
}

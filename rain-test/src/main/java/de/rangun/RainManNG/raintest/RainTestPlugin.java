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

package de.rangun.RainManNG.raintest;

import java.util.List;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

final public class RainTestPlugin extends JavaPlugin {

	@Override
	public void onEnable() {

		final List<World> worlds = getServer().getWorlds();

		(new BukkitRunnable() {

			@Override
			public void run() {

				for (World w : worlds) {
					w.setWeatherDuration(600);
					w.setStorm(true);
				}
			}

		}).runTaskTimer(this, 1200L, 1200L);
	}
}

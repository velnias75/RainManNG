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
import org.bukkit.plugin.java.annotation.dependency.LoadBefore;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion.Target;
import org.bukkit.plugin.java.annotation.plugin.Description;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.Website;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import org.bukkit.scheduler.BukkitRunnable;

@Plugin(name = "RainManNG-raintest", version = "0.0-SNAPSHOT")
@Description(value = "Helper plugin for RainManNG development. DO NOT USE!")
@Website(value = "https://github.com/velnias75/RainManNG")
@Author(value = "Velnias75")
@LoadBefore(value = "RainManNG")
@ApiVersion(Target.v1_15)
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

package de.rangun.RainManNG.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

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

			return sugg;
		}

		return args.length >= 2 ? new ArrayList<>() : cmd_args;
	}

}

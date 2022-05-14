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

	@SuppressWarnings("serial")
	@Override
	final public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

		if (args.length > 0 && args[0].length() > 0) {

			for (String string : cmd_args) {
				if (StringUtil.startsWithIgnoreCase(string, args[0])) {
					return new ArrayList<String>() {
						{
							add(string);
						}
					};
				}
			}
		}

		return cmd_args;
	}

}

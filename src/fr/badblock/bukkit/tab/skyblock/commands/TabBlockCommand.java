package fr.badblock.bukkit.tab.skyblock.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.badblock.bukkit.tab.skyblock.BadBlockTab;

public class TabBlockCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
		if (!sender.hasPermission("tabblock.admin")) return false;
		BadBlockTab.getInstance().reloadConfig();
		sender.sendMessage("§e[§bTabBlock§e] §aConfiguration rechargée.");
		return true;
	}
	
}

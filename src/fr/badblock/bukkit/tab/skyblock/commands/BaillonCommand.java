package fr.badblock.bukkit.tab.skyblock.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.badblock.bukkit.tab.skyblock.BadBlockTab;

public class BaillonCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
		if (!sender.hasPermission("baillon.admin")) return false;
		BadBlockTab instance = BadBlockTab.getInstance();
		if (instance.baillon) {
			instance.baillon = false;
			sender.sendMessage("§6Baillon §cdésactivé§6.");
			Bukkit.broadcastMessage("§6[INFO] Le t'chat a été §cdé-baillonné §6par " + sender.getName() + ".");
			return true;
		}
		instance.baillon = true;
		sender.sendMessage("§6Baillon §aactivé§6.");
		Bukkit.broadcastMessage("§6[INFO] Le t'chat a été §abaillonné §6par " + sender.getName() + ".");
		return true;
	}
	
}

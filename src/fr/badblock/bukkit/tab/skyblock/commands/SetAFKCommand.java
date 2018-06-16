package fr.badblock.bukkit.tab.skyblock.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.badblock.bukkit.tab.skyblock.BadBlockTab;
import fr.badblock.bukkit.tab.skyblock.objects.TabPlayer;

public class SetAFKCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
		if (!sender.hasPermission("setafk")) return false;
		if (!(sender instanceof Player)) return false;
		Player player = (Player) sender;
		TabPlayer tabPlayer = TabPlayer.getPlayer(player);
		BadBlockTab instance = BadBlockTab.getInstance();
		if (instance.afk.contains(player.getName())) {
			instance.afk.remove(player.getName());
			tabPlayer.update();
			sender.sendMessage("§aVous n'êtes plus AFK !");
			return true;
		}
		instance.afk.add(player.getName());
		tabPlayer.update();
		sender.sendMessage("§aVous êtes désormais AFK !");
		return true;
	}
	
}

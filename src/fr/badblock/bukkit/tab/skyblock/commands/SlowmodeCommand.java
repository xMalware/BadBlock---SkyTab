package fr.badblock.bukkit.tab.skyblock.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.badblock.bukkit.tab.skyblock.BadBlockTab;

public class SlowmodeCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
		if (!sender.hasPermission("slowmode.admin")) return false;
		BadBlockTab instance = BadBlockTab.getInstance();
		if (args.length == 0) {
			if (instance.slowmodeTime > 0 && instance.slowmodeMax > System.currentTimeMillis() / 1000L) {
				long reste = instance.slowmodeMax - (System.currentTimeMillis() / 1000L);
				String plurial = reste > 1 ? "s" : "";
				sender.sendMessage("§6Slowmode §aactivé §6pour §e" + instance.slowmodeTime + "§6 secondes par §e" + instance.slowmodePlayer + "§6 (pendant " + reste + " seconde" + plurial + " restante" + plurial + ").");
			}else sender.sendMessage("§cAucun §6slowmode actif.");
			return true;
		}else if (args.length == 1) {
			if (instance.slowmodeTime > 0 && instance.slowmodeMax > System.currentTimeMillis() / 1000L) {
				sender.sendMessage("§aSlowmode désactivé.");
				instance.slowmodeMax = 0L;
				instance.slowmodePlayer = "";
				instance.slowmodeTime = 0L;
				Bukkit.broadcastMessage("§6[INFO] Slowmode §cdésactivé §6par " + sender.getName() + ".");
				return true;
			}else{
				sender.sendMessage("§cAucun slowmode actif.");
				return true;
			}
		}else if (args.length >= 2) {
			String msg = args[0];
			int nb = -1;
			try {
				nb = Integer.parseInt(msg);
			}catch(Exception error) {
			}
			if (nb > 15 && !sender.hasPermission("slowmode.admin.bypass")) {
				sender.sendMessage("§cVous ne pouvez pas séléctionner un slowmode de plus de quinze secondes par message.");
				return true;
			}
			if (nb > 30) {
				sender.sendMessage("§cVous ne pouvez pas séléctionner un slowmode de plus de trente secondes par message.");
				return true;
			}
			if (nb == -1) {
				if (instance.slowmodeTime > 0 && instance.slowmodeMax > System.currentTimeMillis() / 1000L) {
					sender.sendMessage("§aSlowmode désactivé.");
					Bukkit.broadcastMessage("§6[INFO] Slowmode §cdésactivé §6par " + sender.getName() + ".");
					return true;
				}else{
					sender.sendMessage("§cAucun slowmode actif.");
					return true;
				}
			}else{
				msg = args[1];
				int max = -1;
				try {
					max = Integer.parseInt(msg);
				}catch(Exception error) {
					sender.sendMessage("§cLe temps maximal doit être un nombre en minute.");
					return true;
				}
				if (max > 60 && !sender.hasPermission("slowmode.admin.bypass")) {
					sender.sendMessage("§cVous ne pouvez pas mettre un slowmode pendant plus d'une heure.");
					return true;
				}
				if (max > 60*3) {
					sender.sendMessage("§cVous ne pouvez pas mettre un slowmode pendant plus de trois heures.");
					return true;
				}
				instance.slowmodeTime = nb;
				instance.slowmodeMax = (System.currentTimeMillis() / 1000L) + (max * 60);
				instance.slowmodePlayer = sender.getName();
				Bukkit.broadcastMessage("§6[INFO] Slowmode §aactivé §6par " + sender.getName() + " (un message toutes les " + instance.slowmodeTime + " seconde" + (instance.slowmodeTime > 1 ? "s" : "") + " pendant " + max + " minute" + (max > 1 ? "s" : "") + ").");
				return true;
			}
		}
		return true;
	}
	
}

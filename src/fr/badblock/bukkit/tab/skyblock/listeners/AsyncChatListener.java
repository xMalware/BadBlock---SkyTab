package fr.badblock.bukkit.tab.skyblock.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import fr.badblock.bukkit.tab.skyblock.BadBlockTab;
import fr.badblock.bukkit.tab.skyblock.objects.TabPlayer;

public class AsyncChatListener implements Listener {

	@EventHandler
	public void onAsyncChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		BadBlockTab instance = BadBlockTab.getInstance();
		if (!player.hasPermission("baillon.bypass") && instance.baillon) {
			event.setCancelled(true);
			player.sendMessage("§6§l[INFO] §cLe chat est sous baillon, veuillez patienter.");
			return;
		}
		TabPlayer tabPlayer = TabPlayer.getPlayer(player);
		long time = System.currentTimeMillis() / 1000L;
		if (!player.hasPermission("slowmode.bypass") && instance.slowmodeTime > 0 && instance.slowmodeMax > System.currentTimeMillis() / 1000L) {
			long reste = time - tabPlayer.lastMessage;
			if (reste <= instance.slowmodeTime) {
				player.sendMessage("§6§l[INFO] §aVeuillez patienter entre chaque message, le slowmode est actif.");
				event.setCancelled(true);
				return;
			}
		}
		tabPlayer.lastMessage = time;
		String message = event.getMessage();
		if (message == null) event.setCancelled(true);


		String ownSuffix = "";
		if (tabPlayer.ownSuffix != null)
		{
			ownSuffix = tabPlayer.ownSuffix;
		}

		
		String prefix = tabPlayer.prefix;
		String suffix = tabPlayer.suffix == null || "".equals(tabPlayer.suffix) ? "§f" : tabPlayer.suffix;
		
		String playerName = player.getDisplayName() != null && !player.getDisplayName().equals(player.getName()) ? player.getDisplayName() : prefix + player.getName();

		if (player.getName().equalsIgnoreCase("xmalware"))
		{
			playerName = playerName.replace(ChatColor.translateAlternateColorCodes('&', ownSuffix), "");
		}
		
		int level = 0;

		if (BadBlockTab.getInstance().api != null)
		{
			level = (int)BadBlockTab.getInstance().api.getIslandLevel(player);
		}

		for (String g : BadBlockTab.getInstance().afk)
		{
			String b = g.toLowerCase();
			if (event.getMessage().toLowerCase().contains(b))
			{
				Bukkit.broadcastMessage("§c[Attention] " + b + " est actuellement occupé.");
				Bukkit.broadcastMessage("§c[Attention] En cas de problème, envoyez un ticket sur le forum.");
				Bukkit.broadcastMessage("§c[Attention] Forum : §b§nhttps://forum.badblock.fr/");
				break;
			}
		}
		event.setFormat("§f§l[§b" + level + "§f§l] " + ChatColor.translateAlternateColorCodes('&', new StringBuilder(String.valueOf(ownSuffix + playerName)).append(" §7: ").append(suffix).append("%2$s").toString()));
	}

}

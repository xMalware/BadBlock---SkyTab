package fr.badblock.bukkit.tab.skyblock.listeners;

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
		String prefix = tabPlayer.prefix;
		String suffix = tabPlayer.suffix == null || "".equals(tabPlayer.suffix) ? "§f" : tabPlayer.suffix;
		String playerName = player.getDisplayName() != null && !player.getDisplayName().equals(player.getName()) ? player.getDisplayName() : prefix + player.getName();
		event.setFormat(ChatColor.translateAlternateColorCodes('&', playerName + " §7: " + suffix + "%2$s"));
	}
	
}

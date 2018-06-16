package fr.badblock.bukkit.tab.skyblock.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import fr.badblock.bukkit.tab.skyblock.objects.TabPlayer;

public class PlayerJoinListener implements Listener {

	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		//Bukkit.broadcastMessage("§e[STAPE 3] Testing for " + player.getName() + "...");
		if (player == null || !player.isOnline()) return;
		TabPlayer.getPlayer(player).update();
		//Bukkit.broadcastMessage("§e[STAPE 3] (1/2) Testing for " + player.getName() + "...");

		//Bukkit.broadcastMessage("§e[STAPE 3] (2/2) Tested for " + player.getName() + "...");
	}
	
}

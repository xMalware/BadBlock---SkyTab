package fr.badblock.bukkit.tab.skyblock.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import fr.badblock.bukkit.tab.skyblock.BadBlockTab;
import fr.badblock.bukkit.tab.skyblock.objects.TabPlayer;
import fr.badblock.bukkit.tab.skyblock.permissions.PermissionsExManager;
import ru.tehkode.permissions.events.PermissionEntityEvent;

public class PermissionEntityListener implements Listener {
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPermissionEntity(PermissionEntityEvent event) {
		Player player = Bukkit.getPlayer(event.getEntity().getName());
		//Bukkit.broadcastMessage("§e[STAPE 2] Testing for " + player.getName() + "...");
		if (player == null || !player.isOnline()) return;
		BadBlockTab instance = BadBlockTab.getInstance();
		PermissionsExManager permissionsExManager = instance.permissionsExManager;
		TabPlayer tabPlayer = TabPlayer.getPlayer(player);
		tabPlayer.group = permissionsExManager.getGroup(player);
		tabPlayer.suffix = permissionsExManager.getSuffix(player);
		tabPlayer.prefix = permissionsExManager.getPrefix(player);
		//Bukkit.broadcastMessage("§e[STAPE 2] (1/2) Testing for " + player.getName() + "...");
		tabPlayer.update();
		//Bukkit.broadcastMessage("§e[STAPE 2] (2/2) Tested for " + player.getName() + "...");
	}
	
}

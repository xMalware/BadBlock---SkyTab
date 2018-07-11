package fr.badblock.bukkit.tab.skyblock.permissions;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PermissionsExManager extends AbstractPermissions {

	public final PermissionManager manager = PermissionsEx.getPermissionManager();

	@SuppressWarnings("deprecation")
	@Override
	public String getGroup(Player base) {
		PermissionUser user = manager.getUser(base);
		if(user == null) {
			return null;
		} else {
			return ChatColor.translateAlternateColorCodes('&', user.getGroupsNames()[0]);
		}
	}

	@Override
	public String getPrefix(Player base) {
		PermissionUser user = manager.getUser(base.getName());
		if(user == null) {
			return null;
		} else {
			return ChatColor.translateAlternateColorCodes('&', user.getPrefix(base.getWorld().getName()));
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public String getSuffix(Player base) {
		PermissionUser user = manager.getUser(base.getName());
		if(user == null) {
			return null;
		} else {
			String suffix = "";
			for (PermissionGroup g : user.getGroups())
			{
				String s = g.getSuffix(base.getWorld().getName());
				if (s != null && !s.isEmpty())
				{
					suffix = s;
				}
			}
			return ChatColor.translateAlternateColorCodes('&', suffix);
		}
	}

	@Override
	public String getOwnSuffix(Player base) {
		PermissionUser user = manager.getUser(base.getName());
		if(user == null) {
			return null;
		} else {
			return user.getOwnSuffix();
		}
	}

	@Override
	public boolean hasPermission(Player base, String node){
		PermissionUser user = manager.getUser(base.getName());
		if(user == null) {
			return base.hasPermission(node);
		} else {
			return user.has(node);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public String[] getGroups(Player base) {
		PermissionUser user = manager.getUser(base);
		if(user == null) {
			return null;
		} else {
			return user.getGroupsNames();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isInGroup(Player base, String groupName) {
		PermissionUser user = manager.getUser(base.getName());
		if(user == null) {
			return false;
		} else {
			return user.getAllGroups().containsKey(groupName);
		}
	}

	@Override
	public void removeGroup(Player base, String groupName) {
		PermissionUser user = manager.getUser(base.getName());
		if (user != null) user.removeGroup(groupName);
	}

}

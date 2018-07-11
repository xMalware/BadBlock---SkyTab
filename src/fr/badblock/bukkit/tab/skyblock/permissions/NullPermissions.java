package fr.badblock.bukkit.tab.skyblock.permissions;

import org.bukkit.entity.Player;

public class NullPermissions extends AbstractPermissions {

	@Override
	public String getGroup(Player base){
		return "";
	}

	@Override
	public String getPrefix(Player base) {
		return "";
	}

	@Override
	public String getSuffix(Player base) {
		return "";
	}
	
	@Override
	public String getOwnSuffix(Player base) {
		return "";
	}
	
	@Override
	public boolean hasPermission(Player base, String node){
		return base.hasPermission(node);
	}
	
	@Override
	public String[] getGroups(Player base){
		return null;
	}
	
	@Override
	public boolean isInGroup(Player base, String groupName) {
		return false;
	}

	@Override
	public void removeGroup(Player base, String groupName) {
	}
	
}

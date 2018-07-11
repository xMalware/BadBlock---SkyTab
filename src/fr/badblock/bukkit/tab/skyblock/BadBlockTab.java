package fr.badblock.bukkit.tab.skyblock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.earth2me.essentials.Essentials;

import fr.badblock.bukkit.tab.skyblock.commands.BaillonCommand;
import fr.badblock.bukkit.tab.skyblock.commands.SetAFKCommand;
import fr.badblock.bukkit.tab.skyblock.commands.SlowmodeCommand;
import fr.badblock.bukkit.tab.skyblock.commands.TabBlockCommand;
import fr.badblock.bukkit.tab.skyblock.listeners.AsyncChatListener;
import fr.badblock.bukkit.tab.skyblock.listeners.PermissionEntityListener;
import fr.badblock.bukkit.tab.skyblock.listeners.PlayerDisconnectionListener;
import fr.badblock.bukkit.tab.skyblock.listeners.PlayerJoinListener;
import fr.badblock.bukkit.tab.skyblock.objects.TabPlayer;
import fr.badblock.bukkit.tab.skyblock.permissions.PermissionsExManager;
import ru.tehkode.permissions.PermissionUser;
import us.talabrek.ultimateskyblock.uSkyBlock;
import us.talabrek.ultimateskyblock.api.uSkyBlockAPI;
import us.talabrek.ultimateskyblock.challenge.Challenge;
import us.talabrek.ultimateskyblock.challenge.ChallengeCompletion;
import us.talabrek.ultimateskyblock.player.PlayerInfo;

public class BadBlockTab extends JavaPlugin {

	private static BadBlockTab		 instance;

	public boolean				 baillon		= false;
	public long					 slowmodeTime   = 0L;
	public long					 slowmodeMax    = 0L;
	public String				 slowmodePlayer;

	public Map<String,  String>  teamsPrefix    = new HashMap<>();
	public Map<String,  String>  teamsGroup     = new HashMap<>();
	public PermissionsExManager  permissionsExManager;

	public boolean				 chat;
	public List<String>			 afk 			= new ArrayList<>();
	public uSkyBlockAPI			 api;
	public uSkyBlock			 skyb;

	@Override
	public void onEnable() {
		instance = this;
		permissionsExManager = new PermissionsExManager();
		this.saveDefaultConfig();
		this.reloadConfig();
		PluginManager pluginManager = this.getServer().getPluginManager();
		pluginManager.registerEvents(new PlayerDisconnectionListener(), this);
		pluginManager.registerEvents(new PermissionEntityListener(), this);
		pluginManager.registerEvents(new PlayerJoinListener(), this);
		pluginManager.registerEvents(new AsyncChatListener(), this);
		getCommand("tabblock").setExecutor(new TabBlockCommand());
		getCommand("baillon").setExecutor(new BaillonCommand());
		getCommand("slowmode").setExecutor(new SlowmodeCommand());
		getCommand("setafk").setExecutor(new SetAFKCommand());


		final Plugin plugin = Bukkit.getPluginManager().getPlugin("uSkyBlock");
		if (plugin instanceof uSkyBlockAPI && plugin.isEnabled()) {
			this.api = (uSkyBlockAPI)plugin;
			this.skyb = (uSkyBlock)this.api;
			Bukkit.getScheduler().runTaskTimerAsynchronously((Plugin)this, (Runnable)new Runnable() {
				@Override
				public void run() {
					for (final Player player : Bukkit.getOnlinePlayers()) {
						final PlayerInfo playerInfo = BadBlockTab.this.skyb.getPlayerInfo(player);
						boolean completed = true;
						final List<Challenge> skylordChallenges = (List<Challenge>)BadBlockTab.this.skyb.getChallengeLogic().getChallengesForRank("Skylord");
						final List<String> skylordStringChallenges = new ArrayList<String>();
						skylordChallenges.forEach(challenge -> skylordStringChallenges.add(challenge.getName()));
						for (final ChallengeCompletion challengeCompletion : playerInfo.getChallenges()) {
							if (!skylordStringChallenges.contains(challengeCompletion.getName())) {
								continue;
							}
							if (challengeCompletion.getTimesCompleted() <= 0) {
								completed = false;
								break;
							}
						}
						final boolean okay = playerInfo.getHasIsland() && playerInfo.getIslandInfo().getLevel() >= 5000.0 && completed;
						final PermissionUser permissionUser = BadBlockTab.this.permissionsExManager.manager.getUser(player);
						if (!okay) {
							if (!permissionUser.inGroup("Skylord")) {
								continue;
							}
							permissionUser.removeGroup("Skylord");
							final Essentials ess = (Essentials)Bukkit.getPluginManager().getPlugin("Essentials");
							ess.getUser(player).setNickname(player.getName());
						}
						else {
							if (permissionUser.inGroup("Skylord")) {
								continue;
							}
							permissionUser.addGroup("Skylord");
						}
					}
				}
			}, 0L, 1200L);
		}
	}

	@Override
	public void reloadConfig() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			TabPlayer tabPlayer = TabPlayer.getPlayer(player);
			for (String string : teamsGroup.keySet()) {
				tabPlayer.scoreboard.getTeam(string).unregister();
			}
		}
		super.reloadConfig();
		FileConfiguration config = this.getConfig();
		for (String string : config.getStringList("groupsOrder")) {
			String[] splitter = string.split(":");
			if (splitter[1].equalsIgnoreCase("CTO")) splitter[0] = "B0";
			teamsGroup.put(splitter[0], splitter[1]);
			teamsPrefix.put(splitter[0], splitter[2]);
		}
		if (!config.contains("chat")) {
			config.set("chat", true);
			this.saveConfig();
			chat = true;
		}else chat = config.getBoolean("chat");
		TabPlayer.players.clear();
		for (Player player : this.getServer().getOnlinePlayers())
			TabPlayer.getPlayer(player);
	}

	public static BadBlockTab getInstance() {
		return instance;
	}

}

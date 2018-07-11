package fr.badblock.bukkit.tab.skyblock.objects;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import fr.badblock.bukkit.tab.skyblock.BadBlockTab;

public class TabPlayer {

	public static Map<UUID, TabPlayer>	players		= new HashMap<>();

	public UUID				 uuid;
	public Scoreboard		 scoreboard;
	public String		 	 prefix;
	public String			 group;
	public String		 	 suffix;
	public String		 	 ownSuffix;
	public Map<String, Team> teams   			 	= new HashMap<>();
	public long				 lastMessage;

	public TabPlayer(Player player) {
		this.uuid = player.getUniqueId();
		players.put(uuid, this);
		group = BadBlockTab.getInstance().permissionsExManager.getGroup(player);
		suffix = BadBlockTab.getInstance().permissionsExManager.getSuffix(player);
		ownSuffix = BadBlockTab.getInstance().permissionsExManager.getOwnSuffix(player);
		prefix = BadBlockTab.getInstance().permissionsExManager.getPrefix(player);
		Bukkit.getScheduler().runTaskLater(BadBlockTab.getInstance(), new Runnable() {
			@Override
			public void run() {
				if (player.getScoreboard() != null) {
					scoreboard = player.getScoreboard();
					BadBlockTab instance = BadBlockTab.getInstance();
					for (Entry<String, String> entry : instance.teamsGroup.entrySet()) {
						if (scoreboard.getTeam(entry.getKey()) != null) continue;
						Team team = scoreboard.registerNewTeam(entry.getKey());
						team.setPrefix(ChatColor.translateAlternateColorCodes('&', instance.teamsPrefix.get(entry.getKey())) + " ");
					}
					if (scoreboard.getTeam("0") == null) {
						Team afkTeam = scoreboard.registerNewTeam("0");
						afkTeam.setSuffix(" §4[§cAFK§4]");
					}
				}
				update();
			}
		}, 20);
	}

	@SuppressWarnings("deprecation")
	public void update() {
		Bukkit.getScheduler().runTaskLater(BadBlockTab.getInstance(), new Runnable() {
			@Override
			public void run() {
				//System.out.println(this.name + " > A");
				Player player = Bukkit.getPlayer(uuid);
				if (player == null || !player.isOnline()) return;
				if (player.getScoreboard() != null)
					scoreboard = player.getScoreboard();
				else scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
				//System.out.println(this.name + " > B");
				BadBlockTab instance = BadBlockTab.getInstance();
				String o = "A";
				for (Entry<String, String> entry : instance.teamsGroup.entrySet())
					if (entry.getValue().equals(group))
						o = entry.getKey();
				//System.out.println(this.name + " > C > " + o);
				// envoi d'une update aux scoreboards des autres pour lui
				for (Player plo : Bukkit.getOnlinePlayers()) {
					//	System.out.println(this.name + " > D > " + plo.getName());
					TabPlayer tb = TabPlayer.getPlayer(plo);
					if (tb.scoreboard != null) {
						for (Team team : tb.scoreboard.getTeams()) {
							//	System.out.println(this.name + " > E > " + plo.getName() + " > RM " + team.getName());
							team.removePlayer(player);
						}
						Team team = tb.scoreboard.getTeam(o);
						//System.out.println(this.name + " > F > " + plo.getName() + " > " + team);
						if (team != null) {
							//	System.out.println(this.name + " > G > " + plo.getName() + " > " + team.getName());
							if (!team.hasPlayer(player)) {
								//	System.out.println(this.name + " > H > " + plo.getName() + " > " + team.getName());
								team.addPlayer(player);
							}
						}
						if (BadBlockTab.getInstance().afk.contains(player.getName())) {
							Team afkTeam = tb.scoreboard.getTeam("0");
							afkTeam.addPlayer(player);
						}
					}
				}
			}
		}, 21);
	}

	public void remove() {
		players.remove(uuid);
	}

	public static TabPlayer getPlayer(Player player) {
		if (!players.containsKey(player.getUniqueId())) return new TabPlayer(player);
		else return players.get(player.getUniqueId());
	}

}

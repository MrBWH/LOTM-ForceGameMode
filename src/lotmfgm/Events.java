package lotmfgm;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class Events implements Listener {
	public static ForceGamemode plugin;

	public Events(ForceGamemode instance) {
		plugin = instance;
	}

	public void doForceAll (Player p) {
		if (!plugin.getConfig().getString("GamemodeAll").contains("none")) {
			String gamemode = plugin.getConfig().getString("GamemodeAll");
			if ((gamemode.equalsIgnoreCase("survival"))
					|| (gamemode.equalsIgnoreCase("0"))) {
				if (p.getGameMode() != GameMode.SURVIVAL) {
					p.setGameMode(GameMode.SURVIVAL);
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Messages.ForceSurvival")));
				}
			} else if ((gamemode.equalsIgnoreCase("creative")) 
					|| (gamemode.equalsIgnoreCase("1"))) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					p.setGameMode(GameMode.CREATIVE);
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Messages.ForceCreative")));
				}
			} else if (((gamemode.equalsIgnoreCase("adventure")) 
					|| (gamemode.equalsIgnoreCase("2"))) 
					&& (p.getGameMode() != GameMode.ADVENTURE)) {
				p.setGameMode(GameMode.ADVENTURE);
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Messages.ForceAdventure")));
			} else if (((gamemode.equalsIgnoreCase("spectator"))
					|| (gamemode.equalsIgnoreCase("3"))
					|| (gamemode.equalsIgnoreCase("spectate"))) 
					&& (p.getGameMode() != GameMode.SPECTATOR)) {
				p.setGameMode(GameMode.SPECTATOR);
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Messages.ForceSpectator")));
			}
		}
	}
	
	public void doFGMDatabase (Player p) {
		// This class will be called if we called by either onPlayerMove or onPlayerJoin
    if (plugin.getDatabaseConfig().contains(p.getUniqueId().toString())) {
			String gamemode = plugin.getDatabaseConfig().getString(p.getUniqueId().toString() + ".Gamemode");
			if ((gamemode.equalsIgnoreCase("survival")) 
					|| (gamemode.equalsIgnoreCase("0"))) {
				if (p.getGameMode() != GameMode.SURVIVAL) {
					p.setGameMode(GameMode.SURVIVAL);
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Messages.ForceSurvival")));
				}
			} else if ((gamemode.equalsIgnoreCase("creative")) 
					|| (gamemode.equalsIgnoreCase("1"))) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					p.setGameMode(GameMode.CREATIVE);
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Messages.ForceCreative")));
				}
			} else if (((gamemode.equalsIgnoreCase("adventure")) 
					|| (gamemode.equalsIgnoreCase("2"))) 
					&& (p.getGameMode() != GameMode.ADVENTURE)) {
				p.setGameMode(GameMode.ADVENTURE);
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Messages.ForceAdventure")));
			} else if (((gamemode.equalsIgnoreCase("spectator"))
					|| (gamemode.equalsIgnoreCase("3"))) 
					&& (p.getGameMode() != GameMode.SPECTATOR)) {
				p.setGameMode(GameMode.SPECTATOR);
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Messages.ForceSpectator")));
			}
		}	
		
	}
	
	public void doFGMPerms (Player p) {
		if (p.hasPermission("forcegamemode.gamemode.survival")) {
			if (p.getGameMode() != GameMode.SURVIVAL) {
				p.setGameMode(GameMode.SURVIVAL);
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Messages.ForceSurvival")));
			}
		} else if (p.hasPermission("forcegamemode.gamemode.creative")) {
			if (p.getGameMode() != GameMode.CREATIVE) {
				p.setGameMode(GameMode.CREATIVE);
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Messages.ForceCreative")));
			}
		} else if ((p.hasPermission("forcegamemode.gamemode.adventure"))
				&& (p.getGameMode() != GameMode.ADVENTURE)) {
			p.setGameMode(GameMode.ADVENTURE);
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Messages.ForceAdventure")));
		} else if ((p.hasPermission("forcegamemode.gamemode.spectator")) && (p.getGameMode() != GameMode.SPECTATOR)){
			p.setGameMode(GameMode.SPECTATOR);
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Messages.ForceSpectator")));
		}	
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if ((plugin.getConfig().getString("ForceOn").contains("move"))
				|| (plugin.getConfig().getString("ForceOn").contains("both")))  {
			this.doForceAll(p);
			this.doFGMDatabase(p);
			this.doFGMPerms(p);
			}
	}


	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if ((plugin.getConfig().getString("ForceOn").contains("join")) 
			||	(plugin.getConfig().getString("ForceOn").contains("both"))) {
			this.doForceAll(p);
			this.doFGMDatabase(p);
			this.doFGMPerms(p);
		}
		if (plugin.getConfig().getBoolean("SendCommand")) {
			if (!plugin.getConfig().getString("Command").equals(null)) {
				String command = plugin.getConfig().getString("Command");
				command = command.replace("%player%", p.getName());
				plugin.getServer().dispatchCommand(org.bukkit.Bukkit.getConsoleSender(), "command");
			} else {
				plugin.getServer().getLogger()
						.info(ChatColor.RED + "[ForceGameMode] Please enter a command to execute in the config");
			}
		}
	}
}

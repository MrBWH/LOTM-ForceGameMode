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

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (!plugin.getConfig().getString("GamemodeAll").contains("none")) {
			String gamemode = plugin.getConfig().getString("GamemodeAll");
			if ((gamemode.equalsIgnoreCase("survival")) || (gamemode.equalsIgnoreCase("0"))
					|| (gamemode.equalsIgnoreCase("s"))) {
				if (p.getGameMode() != GameMode.SURVIVAL) {
					p.setGameMode(GameMode.SURVIVAL);
					p.sendMessage(ChatColor.RED + "[ForceGameMode] Gamemode Set to SURVIVAL.");
				}
			} else if ((gamemode.equalsIgnoreCase("creative")) 
					|| (gamemode.equalsIgnoreCase("1"))
					|| (gamemode.equalsIgnoreCase("c"))) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					p.setGameMode(GameMode.CREATIVE);
					p.sendMessage(ChatColor.RED + "[ForceGameMode] Gamemode Set to CREATIVE.");
				}
			} else if (((gamemode.equalsIgnoreCase("adventure")) 
					|| (gamemode.equalsIgnoreCase("2"))
					|| (gamemode.equalsIgnoreCase("a"))) 
					&& (p.getGameMode() != GameMode.ADVENTURE)) {
				p.setGameMode(GameMode.ADVENTURE);
				p.sendMessage(ChatColor.RED + "[ForceGameMode] Gamemode Set to ADVENTURE");
			} else if (((gamemode.equalsIgnoreCase("spectator"))
					|| (gamemode.equalsIgnoreCase("3"))
					|| (gamemode.equalsIgnoreCase("spectate"))) 
					&& (p.getGameMode() != GameMode.SPECTATOR)) {
				p.setGameMode(GameMode.SPECTATOR);
				p.sendMessage(ChatColor.RED + "[ForceGameMode] Gamemode set to SPECTATOR");
			}
		} else if (plugin.getDatabaseConfig().contains(p.getName())) {
			String gamemode = plugin.getDatabaseConfig().getString(p.getName() + ".Gamemode");
			if ((gamemode.equalsIgnoreCase("survival")) 
					|| (gamemode.equalsIgnoreCase("0"))
					|| (gamemode.equalsIgnoreCase("s"))) {
				if (p.getGameMode() != GameMode.SURVIVAL) {
					p.setGameMode(GameMode.SURVIVAL);
					p.sendMessage(ChatColor.RED + "[ForceGameMode] Gamemode Set to SURVIVAL.");
				}
			} else if ((gamemode.equalsIgnoreCase("creative")) 
					|| (gamemode.equalsIgnoreCase("1"))
					|| (gamemode.equalsIgnoreCase("c"))) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					p.setGameMode(GameMode.CREATIVE);
					p.sendMessage(ChatColor.RED + "[ForceGameMode] Gamemode Set to CREATIVE.");
				}
			} else if (((gamemode.equalsIgnoreCase("adventure")) 
					|| (gamemode.equalsIgnoreCase("2"))
					|| (gamemode.equalsIgnoreCase("a"))) 
					&& (p.getGameMode() != GameMode.ADVENTURE)) {
				p.setGameMode(GameMode.ADVENTURE);
				p.sendMessage(ChatColor.RED + "[ForceGameMode] Gamemode Set to ADVENTURE");
			} else if (((gamemode.equalsIgnoreCase("spectator"))
					|| (gamemode.equalsIgnoreCase("3"))
					|| (gamemode.equalsIgnoreCase("spectate"))) 
					&& (p.getGameMode() != GameMode.SPECTATOR)) {
				p.setGameMode(GameMode.SPECTATOR);
				p.sendMessage(ChatColor.RED + "[ForceGameMode] Gamemode Set to SPECTATOR");
			}
		}
	}


	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (p.hasPermission("forcegamemode.gamemode.survival")) {
			if (p.getGameMode() != GameMode.SURVIVAL) {
				p.setGameMode(GameMode.SURVIVAL);
				p.sendMessage(ChatColor.RED + "You have been set to SURVIVAL");
			}
		} else if (p.hasPermission("forcegamemode.gamemode.creative")) {
			if (p.getGameMode() != GameMode.CREATIVE) {
				p.setGameMode(GameMode.CREATIVE);
				p.sendMessage(ChatColor.RED + "You have been set to CREATIVE");
			}
		} else if ((p.hasPermission("forcegamemode.gamemode.adventure")) && (p.getGameMode() != GameMode.ADVENTURE)) {
			p.setGameMode(GameMode.ADVENTURE);
			p.sendMessage(ChatColor.RED + "You have been set to ADVENTURE");
		} else if ((p.hasPermission("forcegamemode.gamemode.spectator")) && (p.getGameMode() != GameMode.SPECTATOR)){
			p.setGameMode(GameMode.SPECTATOR);
			p.sendMessage(ChatColor.RED + "You have been set to Spectator");
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

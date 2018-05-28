package lotmfgm;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ForceGamemode extends JavaPlugin {
	public static ForceGamemode plugin;
	Events events = new Events(this);

	private FileConfiguration DatabaseConfig = null;
	private File DataBaseConfigFile = null;

	public void reloadDatabaseConfig() {
		if (this.DataBaseConfigFile == null)
			this.DataBaseConfigFile = new File(getDataFolder(), "Database.yml");
		this.DatabaseConfig = YamlConfiguration.loadConfiguration(this.DataBaseConfigFile);

		InputStream defConfigStream = getResource("Database.yml");
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(DataBaseConfigFile);
			this.DatabaseConfig.setDefaults(defConfig);
		}
	}

	public FileConfiguration getDatabaseConfig() {
		if (this.DatabaseConfig == null)
			reloadDatabaseConfig();
		return this.DatabaseConfig;
	}

	public void saveDatabaseConfig() {
		if ((this.DatabaseConfig == null) || (this.DataBaseConfigFile == null)) {
			return;
		}
		try {
			this.DatabaseConfig.save(this.DataBaseConfigFile);
		} catch (IOException ex) {
			Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE,
					"Could not save config to " + this.DataBaseConfigFile, ex);
		}
	}

	public void onDisable() {
	}

	public void onEnable() {
		getServer().getPluginManager().registerEvents(this.events, this);
		getConfig().options().copyHeader(true);
		getConfig().options().copyDefaults(true);
		saveConfig();
		getDatabaseConfig().options().copyDefaults(true);
		saveDatabaseConfig();
	}
    public void handleFGMcmd (CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("add")) {
				if (args.length > 1) {
					UUID pName = Bukkit.getServer().getPlayer(args[1]).getUniqueId();
					Player pUUID = Bukkit.getServer().getPlayer(pName);
					if (args.length > 2) {
						String gamemode = args[2];
						if ((gamemode.equalsIgnoreCase("survival")) 
								|| (gamemode.equalsIgnoreCase("0"))
								|| (gamemode.equalsIgnoreCase("creative"))
								|| (gamemode.equalsIgnoreCase("1")) 
								|| (gamemode.equalsIgnoreCase("adventure"))
								|| (gamemode.equalsIgnoreCase("2")) 
								|| (gamemode.equalsIgnoreCase("spectator"))
								|| (gamemode.equalsIgnoreCase("3"))){
							getDatabaseConfig().set(pUUID.getUniqueId().toString(), null);
							getDatabaseConfig().set(pUUID.getUniqueId().toString() + ".Gamemode", gamemode);
							saveDatabaseConfig();
							sender.sendMessage(ChatColor.RED + "[ForceGameMode] Player added.");
						} else {
							sender.sendMessage(ChatColor.RED + "[ForceGameMode] Please use a correct gamemode.");
						}
					} else {
						sender.sendMessage(ChatColor.RED + "[ForceGameMode] Please enter a gamemode to force.");
					}
				} else {
					sender.sendMessage(ChatColor.RED
							+ "[ForceGameMode] Please enter a player name to add to the database");
				} 
			} else if (args[0].equalsIgnoreCase("remove")) {
				if (args.length > 1) {
					UUID pName = Bukkit.getServer().getPlayer(args[1]).getUniqueId();
					Player pUUID = Bukkit.getServer().getPlayer(pName);
					getDatabaseConfig().set(pUUID.getUniqueId().toString() + ".Gamemode", "none");
					saveDatabaseConfig();
					sender.sendMessage(ChatColor.RED + "[ForceGameMode] Player Removed");
				} else {
					sender.sendMessage(ChatColor.RED + "[ForceGameMode] Please enter a name to be removed.");
				}
			} 	
		} else {
			sender.sendMessage("[ForceGameMode] Help:");
			sender.sendMessage(ChatColor.YELLOW + "Commands are /fgm or /forcegamemode");
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "Availabe gamemode Options are: survival, creative, adventure and spectator or use ther number 0,1,2,3");
			sender.sendMessage(ChatColor.YELLOW + "/fgm <player> <gamemode>");
			sender.sendMessage(ChatColor.YELLOW + "Too remove player /fgm remove <player>");
			sender.sendMessage(ChatColor.GREEN + "Permissions will overide commands!!!");
		}

    }    	

    
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	/* This is the listener for the Commands /fgm and /forcegamemode
	   fgm can handles the three 4 game modes
	   fgm can handle fgm <player> <gamemode> temp will only last until next login.
	   fgm can add a player to the database to make pediment
	   fgm can remove player from database
	*/
		if ((sender instanceof Player)) {
			Player s = (Player) sender; 
			if (cmd.getName().equalsIgnoreCase("fgm") || (cmd.getName().equalsIgnoreCase("forcegamemode"))) {
				if (s.hasPermission("forcegamemode.admin")) {
				this.handleFGMcmd(s, cmd, label, args);
				}
				else {
				s.sendMessage(ChatColor.RED + "Sorry you do not have permission");
				}
			}
		}
		else if ((sender instanceof ConsoleCommandSender)) {
			ConsoleCommandSender s = (ConsoleCommandSender) sender; 
			if (cmd.getName().equalsIgnoreCase("fgm") || (cmd.getName().equalsIgnoreCase("forcegamemode"))) {
				this.handleFGMcmd(s, cmd, label, args);
			}
						
			} 
		else {
			sender.sendMessage("[ForceGameMode] Help:");
			sender.sendMessage("Commands are: fgm or forcegamemode");
			sender.sendMessage("Availabe gamemode Options are: survival, creative, adventure and spectator or use ther number 0,1,2,3");
			sender.sendMessage("fmg <player> <gamemode>");
			sender.sendMessage("remove player form forced gamemode: fmg remove <player>");
			sender.sendMessage("Note: Permissions will overide commands");
			sender.sendMessage("Permissions: forcegamemode.gamemode.survival, forcegamemode.gamemode.creative, forcegamemode.gamemode.adventure, forcegamemode.gamemode.spectator");
		}
		return true;
	}
}

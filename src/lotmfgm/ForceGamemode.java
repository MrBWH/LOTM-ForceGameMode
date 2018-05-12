package lotmfgm;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if ((sender instanceof Player)) {
			if (cmd.getName().equalsIgnoreCase("fgm") || (cmd.getName().equalsIgnoreCase("forcegamemode"))) {
				Player p = (Player) sender;
				if (p.hasPermission("forcegamemode.admin")) {
					if (args.length > 0) {
						if (args[0].equalsIgnoreCase("add")) {
							if (args.length > 1) {
								String pname = args[1];
								if (args.length > 2) {
									String gamemode = args[2];
									if ((gamemode.equalsIgnoreCase("survival")) 
											|| (gamemode.equalsIgnoreCase("0"))
											|| (gamemode.equalsIgnoreCase("s"))
											|| (gamemode.equalsIgnoreCase("creative"))
											|| (gamemode.equalsIgnoreCase("1")) 
											|| (gamemode.equalsIgnoreCase("c"))
											|| (gamemode.equalsIgnoreCase("adventure"))
											|| (gamemode.equalsIgnoreCase("2")) 
											|| (gamemode.equalsIgnoreCase("a"))
											|| (gamemode.equalsIgnoreCase("spectator"))
											|| (gamemode.equalsIgnoreCase("3"))
											|| (gamemode.equalsIgnoreCase("spectate"))){
										getDatabaseConfig().set(pname, null);
										getDatabaseConfig().set(pname + ".Gamemode", gamemode);
										saveDatabaseConfig();
										p.sendMessage(ChatColor.RED + "[ForceGameMode] Player added.");
									} else {
										p.sendMessage(ChatColor.RED + "[ForceGameMode] Please use a correct gamemode.");
									}
								} else {
									p.sendMessage(ChatColor.RED + "[ForceGameMode] Please enter a gamemode to force.");
								}
							} else {
								p.sendMessage(ChatColor.RED
										+ "[ForceGameMode] Please enter a player name to add to the database");
							}
						} else if (args[0].equalsIgnoreCase("remove")) {
							if (args.length > 1) {
								String pname = args[1];
								getDatabaseConfig().set(pname + ".Gamemode", "none");
								saveDatabaseConfig();
								p.sendMessage(ChatColor.RED + "[ForceGameMode] Player Removed");
							} else {
								p.sendMessage(ChatColor.RED + "[ForceGameMode] Please enter a name to be removed.");
							}
						}
					} else {
						p.sendMessage("[ForceGameMode] Help:");
						p.sendMessage(ChatColor.YELLOW + "Commands are /fgm or /forcegamemode");
						p.sendMessage(ChatColor.LIGHT_PURPLE + "Availabe gamemode Options are: survival, creative, adventure and spectator or use ther number 0,1,2,3");
						p.sendMessage(ChatColor.YELLOW + "/fgm <player> <gamemode>");
						p.sendMessage(ChatColor.YELLOW + "Too remove player /fgm remove <player>");
						p.sendMessage(ChatColor.GREEN + "Permissions will overide commands!!!");
					}
				} else {
					p.sendMessage(ChatColor.RED + "Sorry you do not have permission");
				}
			} 
		} else {
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

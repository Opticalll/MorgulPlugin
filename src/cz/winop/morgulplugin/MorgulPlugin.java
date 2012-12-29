package cz.winop.morgulplugin;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import cz.winop.morgulplugin.config.Config;
import cz.winop.morgulplugin.database.DataBase;
import cz.winop.morgulplugin.event.CommandEvent;
import cz.winop.morgulplugin.managers.CommandManager;
import cz.winop.morgulplugin.managers.LoginManager;
import cz.winop.morgulplugin.managers.PlayerManager;

public final class MorgulPlugin extends JavaPlugin
{
	private static final String SECTION = "System";
	public static MorgulPlugin thisPlugin;
	private static boolean debugMode;
	private static Logger log;
	public static void log(String text)
	{
		log.info(text);
	}
	
	public static void debug(String text)
	{
		if(debugMode)
			log.info(text);
	}
	
	@Override
	public void onLoad()
	{
		thisPlugin = this;
		log = this.getLogger();
		if(!Config.loadConfig())
			MorgulPlugin.log("Config could not be Loaded.");
		else
		{
			MorgulPlugin.log("Config Loaded.");	
			debugMode = Boolean.parseBoolean(Config.get(SECTION, "debugmode"));
			if(!DataBase.setUp())
				MorgulPlugin.log("Database could not be SetUp.");
			else
				MorgulPlugin.log("Database SetUp.");
			PlayerManager.init();
			MorgulPlugin.log("PlayerManager Init.");
			CommandManager.init();
			MorgulPlugin.log("CommandManager Init.");
			LoginManager.init();
			MorgulPlugin.log("LoginManaget Init.");
		}	
	}
	
	@Override
	public void onEnable()
	{
		this.getServer().getPluginManager().registerEvents(new PlayerManager(), this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		CommandManager.sendCommand(new CommandEvent(sender, command, label, args));
		return true;
	}
}

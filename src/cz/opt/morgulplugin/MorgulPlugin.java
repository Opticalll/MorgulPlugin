package cz.opt.morgulplugin;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.getspout.spoutapi.SpoutManager;

import cz.opt.morgulplugin.commands.Test;
import cz.opt.morgulplugin.config.Config;
import cz.opt.morgulplugin.database.DataBase;
import cz.opt.morgulplugin.entity.MorgPlayer;
import cz.opt.morgulplugin.event.CommandEvent;
import cz.opt.morgulplugin.eventmanager.BlockEventManager;
import cz.opt.morgulplugin.eventmanager.ChatEventManager;
import cz.opt.morgulplugin.eventmanager.EntityEventManager;
import cz.opt.morgulplugin.eventmanager.PlayerEventManager;
import cz.opt.morgulplugin.eventmanager.SpoutEventManager;
import cz.opt.morgulplugin.managers.ChatManager;
import cz.opt.morgulplugin.managers.CoinManager;
import cz.opt.morgulplugin.managers.CommandManager;
import cz.opt.morgulplugin.managers.EconomyManager;
import cz.opt.morgulplugin.managers.HuntManager;
import cz.opt.morgulplugin.managers.LoginManager;
import cz.opt.morgulplugin.managers.PlayerManager;
import cz.opt.morgulplugin.managers.SocketManager;
import cz.opt.morgulplugin.managers.StatManager;
import cz.opt.morgulplugin.structs.CustomItem;

public final class MorgulPlugin extends JavaPlugin
{
	private static final String CONF_FILE = "system.conf";
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
		if(!Config.init())
			MorgulPlugin.log("Config could not be Loaded.");
		else
		{
			MorgulPlugin.log("Config Loaded.");	
			debugMode = Boolean.parseBoolean(Config.get(CONF_FILE, SECTION, "debugmode"));
			if(!DataBase.setUp())
			{
				MorgulPlugin.log("Database could not be SetUp.");
				setEnabled(false);
				return;
			}
			else
				MorgulPlugin.log("Database SetUp.");
			CommandManager.init();
			MorgulPlugin.log("CommandManager Init.");
			PlayerManager.init();
			MorgulPlugin.log("PlayerManager Init.");
			ChatManager.init();
			MorgulPlugin.log("ChatManager Init.");
			MorgPlayer.init();
			MorgulPlugin.log("MorgulPlayer Init.");
			LoginManager.init();
			MorgulPlugin.log("LoginManager Init.");
			EconomyManager.init();
			MorgulPlugin.log("EconomyManager Init.");
			StatManager.init();
			MorgulPlugin.log("StatManager Init.");
			HuntManager.init();
			MorgulPlugin.log("HuntManager Init.");
			SocketManager.init();
			MorgulPlugin.log("SocketManager Init.");
		}	
	}
	
	@Override
	public void onEnable()
	{
		if (!Bukkit.getPluginManager().isPluginEnabled("Spout")) {
			Bukkit.getLogger().log(Level.WARNING, "Could not start: SpoutPlugin not found. SpoutPlugin is required.");
			setEnabled(false);
			return;
		}
		//Item block init must be here.
		CustomItem.init();
		CoinManager.init();
		MorgulPlugin.log("CoinManager Init.");
		CustomItem.setUpCache();
		SpoutManager.getFileManager().addToCache(MorgulPlugin.thisPlugin, new File("Morgul/textures/coinExchange.png"));
		new Test();
		
		
		this.getServer().getPluginManager().registerEvents(new PlayerEventManager(), this);
		this.getServer().getPluginManager().registerEvents(new ChatEventManager(), this);
		this.getServer().getPluginManager().registerEvents(new BlockEventManager(), this);
		this.getServer().getPluginManager().registerEvents(new SpoutEventManager(), this);
		this.getServer().getPluginManager().registerEvents(new EntityEventManager(), this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		return CommandManager.sendCommand(new CommandEvent(sender, command, label, args));
	}
}

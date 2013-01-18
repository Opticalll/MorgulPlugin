package cz.opt.morgulplugin.managers;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import cz.opt.morgulplugin.config.Config;
import cz.opt.morgulplugin.entity.MorgPlayer;
import cz.opt.morgulplugin.event.CommandEvent;
import cz.opt.morgulplugin.listener.CommandListener;

public class LanguageManager implements CommandListener
{
	private static final String CONFIG_FILE = "lang.conf";
	private static List<String> languages;
	
	private static LanguageManager instance;
	private static HashMap<String, HashMap<String, String>> fileMap;
	
	public static void init()
	{
		fileMap = Config.getFileMap(CONFIG_FILE);
		String[] keys = null;
		fileMap.keySet().toArray(keys);
		languages = Arrays.asList(keys);
		instance = new LanguageManager();
		CommandManager.registerListener("lang", instance);
	}
	
	public static void getText(MorgPlayer pl, String textKey)
	{
		
	}
	
	@Override
	public boolean onCommand(CommandEvent e)
	{
		return true;
	}

	@Override
	public boolean isPreLogin() { return false; }

	@Override
	public boolean isCmdCom() { return false; }
}

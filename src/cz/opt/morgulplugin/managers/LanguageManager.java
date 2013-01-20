package cz.opt.morgulplugin.managers;


import java.util.ArrayList;
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
		languages = new ArrayList<String>();
		languages.addAll(fileMap.keySet());
		instance = new LanguageManager();
		CommandManager.registerListener("lang", instance);
	}
	
	public static void sendText(MorgPlayer pl, String textKey, String sufix)
	{
		pl.getPlayer().sendMessage(fileMap.get(pl.getLanguage()).get(textKey) + sufix);
	}
	
	public static void sendText(MorgPlayer pl, String textKey)
	{
		pl.getPlayer().sendMessage(fileMap.get(pl.getLanguage()).get(textKey));
	}
	public static void sendText(MorgPlayer pl, String prefix, String textKey, String sufix)
	{
		pl.getPlayer().sendMessage(prefix + fileMap.get(pl.getLanguage()).get(textKey) + sufix);
	}
	
	@Override
	public boolean onCommand(CommandEvent e)
	{
		if(e.getCommand().getName().equalsIgnoreCase("lang"))
		{
			String avLangs = "";
			for(int i = 0; i < languages.size(); i++)
				avLangs += languages.get(i) + ", ";
			if(e.getArgs().length < 1)
			{
				LanguageManager.sendText(PlayerManager.getPlayer(e.getSender().getName()), "LanguageManager_Lang_NoParams", avLangs);
				return true;
			}
			if(!languages.contains(e.getArgs()[0].toLowerCase()))
			{
				LanguageManager.sendText(PlayerManager.getPlayer(e.getSender().getName()), "LanguageManager_Lang_NoSuchLang", avLangs);
				return true;
			}
			else
			{
				PlayerManager.getPlayer(e.getSender().getName()).setLanguage(e.getArgs()[0].toLowerCase());
				LanguageManager.sendText(PlayerManager.getPlayer(e.getSender().getName()), "LanguageManager_Lang_LangChanged");
				return true;
			}
		}
			
		return true;
	}

	@Override
	public boolean isPreLogin() { return false; }

	@Override
	public boolean isCmdCom() { return false; }
}

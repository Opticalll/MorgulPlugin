package cz.opt.morgulplugin.managers;

import java.util.HashMap;

import org.bukkit.entity.Player;

import cz.opt.morgulplugin.event.CommandEvent;
import cz.opt.morgulplugin.listener.CommandListener;

public class CommandManager
{
	private static HashMap<String, CommandListener> listenerMap;
	
	public static void init()
	{
		listenerMap = new HashMap<String, CommandListener>();
	}
	
	public static synchronized void registerListener(String command, CommandListener listener)
	{
		listenerMap.put(command.toLowerCase(), listener);
	}
	
	public static synchronized void removeListener(CommandListener listener)
	{
		listenerMap.remove(listener);
	}
	
	public static synchronized boolean sendCommand(CommandEvent e)
	{
		if(e.getSender() instanceof Player && !PlayerManager.getPlayer(e.getSender().getName()).isLogged() && listenerMap.get(e.getCommand().getName().toLowerCase()).isPreLogin())
			return listenerMap.get(e.getCommand().getName().toLowerCase()).onCommand(e);
		else if(e.getSender() instanceof Player && PlayerManager.getPlayer(e.getSender().getName()).isLogged())
			return listenerMap.get(e.getCommand().getName().toLowerCase()).onCommand(e);
		else if(listenerMap.get(e.getCommand().getName().toLowerCase()).isCmdCom())
			return listenerMap.get(e.getCommand().getName().toLowerCase()).onCommand(e);
		else
			return false;
	}
}

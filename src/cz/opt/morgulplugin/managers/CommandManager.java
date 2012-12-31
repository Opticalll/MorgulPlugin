package cz.opt.morgulplugin.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

import cz.opt.morgulplugin.event.CommandEvent;
import cz.opt.morgulplugin.listener.CommandListener;

public class CommandManager
{
	//TODO HashMap<String, CommandListener> change to HashMap<String, List<CommandListener>> and remoe loginListeners
	private static HashMap<String, CommandListener> listenerMap;
	private static List<CommandListener> loginListeners;
	
	public static void init()
	{
		listenerMap = new HashMap<String, CommandListener>();
		loginListeners = new ArrayList<CommandListener>();
	}
	
	public static synchronized void registerListener(String command, CommandListener listener)
	{
		listenerMap.put(command.toLowerCase(), listener);
	}
	
	public static synchronized void removeListener(CommandListener listener)
	{
		listenerMap.remove(listener);
	}
	
	public static synchronized void registerLoginListener(CommandListener listener)
	{
		loginListeners.add(listener);
	}
	
	public static synchronized void removeLoginListener(CommandListener listener)
	{
		loginListeners.remove(listener);
	}
	
	public static synchronized boolean sendCommand(CommandEvent e)
	{
		if(e.getSender() instanceof Player && !PlayerManager.getPlayer(e.getSender().getName()).isLogged())
			for(CommandListener cl : loginListeners)
				cl.onCommand(e);
		if(e.getSender() instanceof Player && !PlayerManager.getPlayer(e.getSender().getName()).isLogged() && listenerMap.get(e.getCommand().getName().toLowerCase()).isPreLogin())
			return listenerMap.get(e.getCommand().getName().toLowerCase()).onCommand(e);
		else if(e.getSender() instanceof Player && PlayerManager.getPlayer(e.getSender().getName()).isLogged())
			return listenerMap.get(e.getCommand().getName().toLowerCase()).onCommand(e);
		else if(!(e.getSender() instanceof Player) && listenerMap.get(e.getCommand().getName().toLowerCase()).isCmdCom())
			return listenerMap.get(e.getCommand().getName().toLowerCase()).onCommand(e);
		else
			return true;
	}
}

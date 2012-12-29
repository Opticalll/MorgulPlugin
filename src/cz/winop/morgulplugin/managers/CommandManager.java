package cz.winop.morgulplugin.managers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import cz.winop.morgulplugin.event.CommandEvent;
import cz.winop.morgulplugin.listener.CommandListener;

public class CommandManager
{
	private static List<CommandListener> listenerList;
	private static List<CommandListener> cmdListenerList;
	private static List<CommandListener> preLoginListenerList;
	
	public static void init()
	{
		listenerList = new ArrayList<CommandListener>();
		cmdListenerList = new ArrayList<CommandListener>();
		preLoginListenerList = new ArrayList<CommandListener>();
	}
	
	public static synchronized void registerListener(CommandListener listener)
	{
		listenerList.add(listener);
	}
	
	public static synchronized void removeListener(CommandListener listener)
	{
		listenerList.remove(listener);
	}
	
	public static synchronized void registerPreLoginListener(CommandListener listener)
	{
		preLoginListenerList.add(listener);
	}
	
	public static synchronized void removePreLoginListener(CommandListener listener)
	{
		preLoginListenerList.remove(listener);
	}
	
	public static synchronized void registerCommandListener(CommandListener listener)
	{
		cmdListenerList.add(listener);
	}
	
	public static synchronized void removeCommadListener(CommandListener listener)
	{
		cmdListenerList.remove(listener);
	}
	
	public static synchronized void sendCommand(CommandEvent e)
	{
		for(int i = 0; i < preLoginListenerList.size(); i++)
			preLoginListenerList.get(i).onCommand(e);
		if(e.getSender() instanceof Player)
			if(PlayerManager.getPlayer(e.getSender().getName()).isLogged())
				for(int i = 0; i < listenerList.size(); i++)
					listenerList.get(i).onCommand(e);
	}
}

package cz.opt.morgulplugin.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

import cz.opt.morgulplugin.event.CommandEvent;
import cz.opt.morgulplugin.listener.CommandListener;

public class CommandManager
{
	private static HashMap<String, List<CommandListener>> listenerMap;
	
	public static void init()
	{
		listenerMap = new HashMap<String, List<CommandListener>>();
	}
	
	public static synchronized void registerListener(String command, CommandListener listener)
	{
		if(listenerMap.get(command) == null)
		{
			List<CommandListener> temp = new ArrayList<CommandListener>();
			temp.add(listener);
			listenerMap.put(command.toLowerCase(), temp);
		}
		else
			listenerMap.get(command).add(listener);
	}
	
	public static synchronized void removeListener(String command, CommandListener listener)
	{
		if(listenerMap.get(command) != null)
			listenerMap.get(command).remove(listener);
	}
	
	public static synchronized boolean sendCommand(CommandEvent e)
	{
		if(listenerMap.get(e.getCommand().getName().toLowerCase()) == null)
			return false;
		if(e.getSender() instanceof Player && !PlayerManager.getPlayer(e.getSender().getName()).isLogged())
			 for(CommandListener prl : listenerMap.get(e.getCommand().getName().toLowerCase()))
				 if(prl.isPreLogin())
					 prl.onCommand(e);
		else if(e.getSender() instanceof Player && PlayerManager.getPlayer(e.getSender().getName()).isLogged())
			for(CommandListener com : listenerMap.get(e.getCommand().getName().toLowerCase()))
					 com.onCommand(e);
		else if(!(e.getSender() instanceof Player))
			for(CommandListener cmd : listenerMap.get(e.getCommand().getName().toLowerCase()))
				if(cmd.isCmdCom())
					cmd.onCommand(e);
		else
			return false;
		return true;
	}
}

package cz.opt.morgulplugin.eventmanager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.getspout.spoutapi.event.spout.SpoutCraftEnableEvent;

import cz.opt.morgulplugin.managers.PlayerManager;

public class SpoutEventManager implements Listener
{

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onSpoutCraftEnable(SpoutCraftEnableEvent event) 
	{
		if(!event.getPlayer().isSpoutCraftEnabled())
		{
			event.getPlayer().kickPlayer("Spout is needed.");
			return;
		}
		PlayerManager.getPlayer(event.getPlayer().getName()).setSpoutPlayer(event.getPlayer());
		PlayerManager.getPlayer(event.getPlayer().getName()).spoutInit();
	}
	
}

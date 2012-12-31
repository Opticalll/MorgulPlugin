package cz.opt.morgulplugin.eventmanager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import cz.opt.morgulplugin.managers.PlayerManager;

public class PlayerEventManager implements Listener
{
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerMove(PlayerMoveEvent event) 
	{
		if(event.getPlayer().hasMetadata("NPC")) return; // Check if this player is a Citizens NPC
		
		//Player Joined(didn't entered password)
		if(!PlayerManager.getPlayer(event.getPlayer().getName()).isLogged())
			event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerLogin(PlayerLoginEvent event) 
	{
		if(event.getPlayer().hasMetadata("NPC")) return; // Check if this player is a Citizens NPC
		
		PlayerManager.onPlayerLogin(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDisconnect(PlayerQuitEvent event)
	{
		if(event.getPlayer().hasMetadata("NPC")) return; // Check if this player is a Citizens NPC
		
		PlayerManager.onPlayerDisconnect(event);
	}
}

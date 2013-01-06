package cz.opt.morgulplugin.eventmanager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import cz.opt.morgulplugin.managers.ChatManager;
import cz.opt.morgulplugin.managers.PlayerManager;

public class ChatEventManager implements Listener
{
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChat(AsyncPlayerChatEvent e)
	{ 
		e.setCancelled(true);
		if(PlayerManager.getPlayer(e.getPlayer().getName()).isLogged())
			ChatManager.onPlayerChatEvent(e);
	}
}

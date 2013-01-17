package cz.opt.morgulplugin.eventmanager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import cz.opt.morgulplugin.managers.HuntManager;

public class EntityEventManager implements Listener
{
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDeath(EntityDeathEvent e)
	{
		HuntManager.onEntityDeath(e);
	}
}

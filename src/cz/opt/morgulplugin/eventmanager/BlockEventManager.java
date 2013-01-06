package cz.opt.morgulplugin.eventmanager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import cz.opt.morgulplugin.managers.StatManager;

public class BlockEventManager implements Listener
{
	 @EventHandler(priority = EventPriority.HIGHEST)
	 public void onBlockDestroy(BlockBreakEvent e)
	 {
		 StatManager.onBlockDestroy(e);
	 }
}

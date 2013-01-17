package cz.opt.morgulplugin.managers;

import java.util.HashMap;

import org.bukkit.event.entity.EntityDeathEvent;

import cz.opt.morgulplugin.MorgulPlugin;
import cz.opt.morgulplugin.config.Config;

public class HuntManager
{
	private static final String CONFIGFILE = "hunt.conf";
	private static HashMap<String, String> rewards;
	
	public static void init()
	{
		HashMap<String, HashMap<String, String>> fileMap = Config.getFileMap(CONFIGFILE);
		rewards = fileMap.get("Rewards");
	}
	
	public static void onEntityDeath(EntityDeathEvent e)
	{
		String entityName = e.getEntityType().toString().toLowerCase();
		String name = rewards.get(entityName);
		MorgulPlugin.debug("Entity Killed: " + entityName);
		if(name == null)
			return;
		CoinManager.dropCoins(e.getEntity().getLocation(), Integer.parseInt(rewards.get(entityName)));
	}
}

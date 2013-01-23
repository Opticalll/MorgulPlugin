package cz.opt.morgulplugin.managers;

import java.util.HashMap;

import org.bukkit.event.entity.EntityDeathEvent;

import cz.opt.morgulplugin.MorgulPlugin;
import cz.opt.morgulplugin.config.Config;
import cz.opt.morgulplugin.utils.Utils;

public class HuntManager
{
	private static final String CONFIGFILE = "hunt.conf";
	private static HashMap<String, String> rewards;
	private static double nightMultipler;
	
	public static void init()
	{
		HashMap<String, HashMap<String, String>> fileMap = Config.getFileMap(CONFIGFILE);
		rewards = fileMap.get("Rewards");
		nightMultipler = Config.get(CONFIGFILE, "Settings", "NightMultipler", 1.25);
	}
	
	public static void onEntityDeath(EntityDeathEvent e)
	{
		String entityName = e.getEntityType().toString().toLowerCase();
		String name = rewards.get(entityName);
		MorgulPlugin.debug("Entity Killed: " + entityName);
		if(name == null)
			return;
		int reward = Integer.parseInt(rewards.get(entityName));
		if(Utils.isNight(e.getEntity().getWorld().getName()))
			reward = (int) Math.round(reward*nightMultipler);
		CoinManager.dropCoins(e.getEntity().getLocation(), reward);
	}
}

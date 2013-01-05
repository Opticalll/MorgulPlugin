package cz.opt.morgulplugin.managers;

import org.bukkit.event.block.BlockBreakEvent;

import cz.opt.morgulplugin.MorgulPlugin;
import cz.opt.morgulplugin.config.Config;
import cz.opt.morgulplugin.entity.MorgPlayer;
import cz.opt.morgulplugin.structs.Stat;

public class StatManager
{
	private static final String SECTION = "Stats";
	
	public static void init()
	{
		
	}
	
	public static void onBlockDestroy(BlockBreakEvent e)
	{
		String senderName = e.getPlayer().getName();
		MorgPlayer player = PlayerManager.getPlayer(senderName);
		String destoyedBlock = e.getBlock().getType().toString();
		
		String confData = Config.get(SECTION, destoyedBlock);
		MorgulPlugin.debug("Block destroyed: " + destoyedBlock);
		
		if(player != null)
		{
			if(confData != null)
			{
				Stat miningStat = player.getStatByName("mining");
				int xp = Integer.parseInt(confData);
				miningStat.setXP(miningStat.getXP() + xp);
				
				player.setStat("mining");
			}
		}
	}
}

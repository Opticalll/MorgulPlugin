package cz.opt.morgulplugin.managers;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import cz.opt.morgulplugin.MorgulPlugin;
import cz.opt.morgulplugin.config.Config;
import cz.opt.morgulplugin.entity.MorgPlayer;
import cz.opt.morgulplugin.event.CommandEvent;
import cz.opt.morgulplugin.listener.CommandListener;
import cz.opt.morgulplugin.structs.Stat;

public class StatManager implements CommandListener
{
	private static final String SECTION = "Stats";
	public static final String CMD_STATS = "stats";
	private static StatManager instance;
	
	public static void init()
	{
		instance = new StatManager();
		CommandManager.registerListener(CMD_STATS, instance);
	}
	
	public static void onBlockDestroy(BlockBreakEvent e)
	{
		String senderName = e.getPlayer().getName();
		MorgPlayer player = PlayerManager.getPlayer(senderName);
		String destoyedBlock = e.getBlock().getType().toString().toLowerCase();
		
		String confData = Config.get(SECTION, destoyedBlock);
		MorgulPlugin.debug("Block destroyed: " + destoyedBlock);
		
		if(player != null)
		{
			if(confData != null)
			{
				Stat miningStat = player.getStatByName("mining");
				int xp = Integer.parseInt(confData);
				miningStat.setXP(miningStat.getXP() + xp);
				
				player.setStat("mining", miningStat);
				MorgulPlugin.debug("Added " + xp + " xp to player's mining stat.");
			}
		}
	}

	@Override
	public boolean onCommand(CommandEvent e)
	{
		String cmdName = e.getCommand().getName();
		if(!(e.getSender() instanceof Player))
			return false;
		
		MorgPlayer player = PlayerManager.getPlayer(((Player) e.getSender()).getName());
		if(cmdName.equalsIgnoreCase(CMD_STATS))
		{
			 Stat[] _values = null;
			 _values = player.getStats().values().toArray(_values); // TODO: failing here
			 for(int i = 0; i < _values.length; i++)
			 {
				 player.getPlayer().sendMessage("Stat: " + cmdName + ", XP: " + player.getStatByName(cmdName).getXP() + ", LVL: " + player.getStatByName(cmdName).getLevel());
			 }
			 return true;
		}
		
		return false;
	}

	@Override
	public boolean isPreLogin() { return false; }

	@Override
	public boolean isCmdCom() { return false; }
}

package cz.opt.morgulplugin.managers;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import cz.opt.morgulplugin.MorgulPlugin;
import cz.opt.morgulplugin.config.AltConfig;
import cz.opt.morgulplugin.config.Config;
import cz.opt.morgulplugin.entity.MorgPlayer;
import cz.opt.morgulplugin.event.CommandEvent;
import cz.opt.morgulplugin.listener.CommandListener;
import cz.opt.morgulplugin.structs.Stat;
import cz.opt.morgulplugin.utils.Utils;

public class StatManager implements CommandListener
{
	private static final String CONF_FILE = "stats.conf";
	public static final String CMD_STATS = "stats";
	private static final String[] SECTIONS = {"Mining", "Harvesting", "Digging"};
	private static StatManager instance;
	
	public static void init()
	{
		instance = new StatManager();
		CommandManager.registerListener(CMD_STATS, instance);
		MorgulPlugin.debug("Printed out using AltConfig: dirt=" + AltConfig.stats.get("Digging", "dirt"));
	}
	
	public static void onBlockDestroy(BlockBreakEvent e)
	{
		String senderName = e.getPlayer().getName();
		MorgPlayer player = PlayerManager.getPlayer(senderName);
		String destoyedBlock = e.getBlock().getType().toString().toLowerCase();
		String confData = null;
		String activeSection = null;
		
		MorgulPlugin.debug("Block destroyed: " + destoyedBlock);
		player.addMinedBlocks(1);
		
		for(int i = 0; i < SECTIONS.length; i++)
		{
			confData = Config.get(CONF_FILE, SECTIONS[i], destoyedBlock);
			if(confData != null)
			{
				activeSection = SECTIONS[i].toLowerCase();
				break;
			}
		}
		
		
		if(player != null)
		{
			if(confData != null)
			{
				Stat miningStat = player.getStatByName(activeSection);
				int xp = Integer.parseInt(confData);
				miningStat.setXP(miningStat.getXP() + xp);
				
				player.setStat(activeSection, miningStat);
				MorgulPlugin.debug("Added " + xp + " xp to" + player.getName() + "'s " + activeSection + " stat.");
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
			List<Stat> stats = Utils.mapToList(player.getStats());
			for(Stat s : stats)
				player.getPlayer().sendMessage("Stat: " + s.getName() + " | XP: " + s.getXP() + ", LVL: " + s.getLevel());
			LanguageManager.sendText(PlayerManager.getPlayer(player.getPlayer().getName()), "StatManager_Stats_DestroyedBlocks"," " + player.getMinedBlocks());
			return true;
		}
		
		return false;
	}

	@Override
	public boolean isPreLogin() { return false; }

	@Override
	public boolean isCmdCom() { return false; }
}

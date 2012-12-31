package cz.opt.morgulplugin.managers;

import org.bukkit.entity.Player;

import cz.opt.morgulplugin.event.CommandEvent;
import cz.opt.morgulplugin.listener.CommandListener;

public class EconomyManager implements CommandListener
{
	private static EconomyManager instance;

	public static void init()
	{
		instance = new EconomyManager();
		CommandManager.registerListener("economy", instance);
	}
	
	public static EconomyManager getInstance()
	{
		return instance;
	}
	
	@Override
	public boolean onCommand(CommandEvent e)
	{
		if(e.getCommand().getName().equalsIgnoreCase("economy") && e.getSender() instanceof Player)
		{
			if(e.getArgs().length <= 0)
			{
				e.getSender().sendMessage("Economy.");
				return true;
			}
			if(e.getArgs()[0].equalsIgnoreCase("status"))
			{
				e.getSender().sendMessage("Morgul Coins: " + PlayerManager.getPlayer(e.getSender().getName()).getM_coins());
				return true;
			}
			else if(e.getArgs()[0].equalsIgnoreCase("pay"))
			{
				if(e.getArgs().length != 3)
				{
					e.getSender().sendMessage("Syntaxe je /economy pay [kolik] [komu]");
					return true;
				}
				else
				{
					if(PlayerManager.getPlayer(e.getArgs()[3]) == null)
					{
						e.getSender().sendMessage("Hrac neni online.");
						//TODO: sending money to offline users.
						return true;
					}
					else
					{
						PlayerManager.getPlayer(e.getArgs()[3]).addM_coins(Integer.parseInt(e.getArgs()[2]));
						PlayerManager.getPlayer(e.getSender().getName()).removeM_coins(Integer.parseInt(e.getArgs()[2]));
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean isPreLogin(){ return false; }
	@Override
	public boolean isCmdCom(){ return false; }
}

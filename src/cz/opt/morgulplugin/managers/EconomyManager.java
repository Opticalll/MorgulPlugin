package cz.opt.morgulplugin.managers;

import org.bukkit.entity.Player;

import cz.opt.morgulplugin.MorgulPlugin;
import cz.opt.morgulplugin.database.DataBase;
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
						if(PlayerManager.playerExist(e.getArgs()[3]))
						{
							try
							{
								PlayerManager.getPlayer(e.getSender().getName()).removeM_coins(Integer.parseInt(e.getArgs()[2]));
								modM_coinstoOffline(e.getArgs()[3], Integer.parseInt(e.getArgs()[2]));
								return true;
							} catch(NumberFormatException ex) {
								MorgulPlugin.log(ex.getMessage());
								e.getSender().sendMessage("Syntaxe je /economy pay [kolik] [komu]");
								return true;
							}
						}
						else
						{
							e.getSender().sendMessage("Hrac neexistuje.");
							return true;
						}
					}
					else
					{
						try
						{
							PlayerManager.getPlayer(e.getArgs()[3]).addM_coins(Integer.parseInt(e.getArgs()[2]));
							PlayerManager.getPlayer(e.getSender().getName()).removeM_coins(Integer.parseInt(e.getArgs()[2]));
							return true;
						} catch(NumberFormatException ex) {
							MorgulPlugin.log(ex.getMessage());
							e.getSender().sendMessage("Syntaxe je /economy pay [kolik] [komu]");
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	public void modM_coinstoOffline(String name, int sum)
	{
		int playerid = PlayerManager.getPlayerId(name);
		int M_Coins = Integer.parseInt(DataBase.query("Select * from player_accounts WHERE id='" + playerid + "'").get(1).get("morgul_coins"));
		M_Coins += sum;
		DataBase.update("UPDATE player_accounts SET morgul_coins='" + M_Coins + "' WHERE id='" + playerid + "'");
	}

	@Override
	public boolean isPreLogin(){ return false; }
	@Override
	public boolean isCmdCom(){ return false; }
}

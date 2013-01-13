package cz.opt.morgulplugin.commands;

import cz.opt.morgulplugin.event.CommandEvent;
import cz.opt.morgulplugin.gui.popout.CoinExchange;
import cz.opt.morgulplugin.listener.CommandListener;
import cz.opt.morgulplugin.managers.CommandManager;
import cz.opt.morgulplugin.managers.PlayerManager;

public class Test implements CommandListener
{
	public Test()
	{
		CommandManager.registerListener("test", this);
	}

	@Override
	public boolean onCommand(CommandEvent e)
	{
		new CoinExchange(PlayerManager.getPlayer(e.getSender().getName()).getSpoutPlayer());
		return false;
		
	}

	@Override
	public boolean isPreLogin()
	{
		return false;
	}

	@Override
	public boolean isCmdCom()
	{
		return false;
	}

}

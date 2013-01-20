package cz.opt.morgulplugin.managers;

import cz.opt.morgulplugin.event.CommandEvent;
import cz.opt.morgulplugin.listener.CommandListener;

public class FractionManager implements CommandListener
{
	//Thinking
	@Override
	public boolean onCommand(CommandEvent e)
	{
		return true;
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

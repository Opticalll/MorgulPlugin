package cz.opt.morgulplugin.listener;

import cz.opt.morgulplugin.event.CommandEvent;

public interface CommandListener
{
	public boolean onCommand(CommandEvent e);
	public boolean isPreLogin();
	public boolean isCmdCom();
}

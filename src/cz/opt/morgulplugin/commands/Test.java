package cz.opt.morgulplugin.commands;

import org.getspout.spoutapi.gui.GenericButton;

import cz.opt.morgulplugin.MorgulPlugin;
import cz.opt.morgulplugin.event.CommandEvent;
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
		GenericButton but = new GenericButton();
		but.setText("Test");
		but.setX(100);
		but.setY(100);
		but.setWidth(50);
		but.setHeight(20);
		PlayerManager.getPlayer(e.getSender().getName()).getSpoutPlayer().getMainScreen().attachWidget(MorgulPlugin.thisPlugin, but);
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

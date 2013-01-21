package cz.opt.morgulplugin.commands;

import cz.opt.morgulplugin.MorgulPlugin;
import cz.opt.morgulplugin.event.SocketEvent;
import cz.opt.morgulplugin.listener.SocketListener;
import cz.opt.morgulplugin.managers.SocketManager;

public class SocketControl implements SocketListener
{
	private static SocketControl instance;
	
	public static void init()
	{
		instance = new SocketControl();
		SocketManager.addSocketListener("C", instance);
	}

	@Override
	public void onSocketInputEvent(SocketEvent e)
	{
		MorgulPlugin.debug("Socekt Control COmmand Accepted - " + e);
	}

}

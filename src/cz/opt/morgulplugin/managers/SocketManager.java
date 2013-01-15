package cz.opt.morgulplugin.managers;

import java.util.ArrayList;

import cz.opt.morgulplugin.listener.SocketListener;
import cz.opt.morgulplugin.network.ThreadServer;

public class SocketManager
{
	static private ArrayList<SocketListener> listeners;
	
	static public void init()
	{
		listeners = new ArrayList<SocketListener>();
		new Thread(new ThreadServer(20)).start();
	}
	
	static public synchronized void fireSocketInputEvent(String input)
	{
		for(int i = 0; i < listeners.size(); i++)
			listeners.get(i).onSocketInputEvent(input);
	}
	
	static public synchronized void addSocketListener(SocketListener soc)
	{
		if(!listeners.contains(soc))
			listeners.add(soc);
	}
	
	static public synchronized void removeSocketListener(SocketListener soc)
	{
		if(listeners.contains(soc))
			listeners.remove(soc);
	}
}

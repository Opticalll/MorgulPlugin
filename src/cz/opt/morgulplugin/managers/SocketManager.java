package cz.opt.morgulplugin.managers;

import java.util.ArrayList;
import java.util.Hashtable;

import cz.opt.morgulplugin.event.SocketEvent;
import cz.opt.morgulplugin.listener.SocketListener;
import cz.opt.morgulplugin.network.ThreadServer;

public class SocketManager
{
	static private Hashtable<String, ArrayList<SocketListener>> listeners;
	
	static public void init()
	{
		listeners = new Hashtable<String, ArrayList<SocketListener>> ();
		new Thread(new ThreadServer(5)).start();
	}
	
	static public synchronized void fireSocketInputEvent(SocketEvent input)
	{
		if(listeners.get(input.getCmdName()) != null)
			for(int i = 0; i < listeners.get(input.getCmdName()).size(); i++)
				listeners.get(input.getCmdName()).get(i).onSocketInputEvent(input);
	}
	
	static public synchronized void addSocketListener(String cmdName, SocketListener soc)
	{
		if(listeners.get(cmdName) != null)
		{
			ArrayList<SocketListener> temp = new ArrayList<SocketListener>();
			temp.add(soc);
			listeners.put(cmdName, temp);
		}
		else
			if(!listeners.get(cmdName).contains(soc))
				listeners.get(cmdName).add(soc);
				
	}
	
	static public synchronized void removeSocketListener(String cmdName, SocketListener soc)
	{
		if(listeners.get(cmdName) != null)
			if(listeners.get(cmdName).contains(soc))
				listeners.get(cmdName).remove(soc);
	}
}

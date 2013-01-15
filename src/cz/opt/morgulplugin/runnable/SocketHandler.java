package cz.opt.morgulplugin.runnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import cz.opt.morgulplugin.MorgulPlugin;
import cz.opt.morgulplugin.managers.SocketManager;

public class SocketHandler implements Runnable
{
	private Socket socket;
	private InputStreamReader input = null;
	private BufferedReader inRead = null;
	private boolean running = true;
	
	public SocketHandler(Socket socket)
	{
		try {
			this.socket = socket;
			input = new InputStreamReader(this.socket.getInputStream());
			inRead = new BufferedReader(input);
		} catch (IOException e) {
			MorgulPlugin.log("" + e.toString());
			running = false;
		}
	}
	
	@Override
	public void run()
	{
		while(running)
		{
			if(socket.isClosed())
			{
				running = false;
				break;
			}
			try
			{
				String msg = "";
				String line;
				while((line = inRead.readLine()) != null)
					msg += line + "\n";
				SocketManager.fireSocketInputEvent(msg);
			} catch (IOException e)
			{
				MorgulPlugin.log("" + e);
				running = false;
			}
		}
		try 
		{
			inRead.close();
			input.close();
			socket.close();
		} catch (IOException e) {
			MorgulPlugin.log("" + e.toString());
		}
	}
}

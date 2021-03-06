package cz.opt.morgulplugin.runnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.opt.morgulplugin.MorgulPlugin;
import cz.opt.morgulplugin.event.SocketEvent;
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
		if(!running)
			return;
		try
		{
			String msg = "";
			String line;
			while((line = inRead.readLine()) != null)
			{
				msg += line;
				if(line.endsWith("{OP}"))
				{
					msg = msg.substring(0, line.length() - 4);
					List<String> parts = new ArrayList<String>();
					parts = Arrays.asList(msg.split(" "));
					String name = parts.get(0).toLowerCase();
					String[] args = new String[parts.size() - 1];
					for(int i = 1; i < parts.size(); i++)
						args[i-1] = parts.get(i);
					MorgulPlugin.debug("Socket Incoming --" + line + "--");
					SocketManager.fireSocketInputEvent(new SocketEvent(this.socket, name, args));
					msg = "";
				}
				MorgulPlugin.debug("Readed");
			}
		} catch (IOException e) {
			MorgulPlugin.log("" + e);
		}
		try 
		{
			inRead.close();
			input.close();
			socket.close();
		} catch (IOException e) {
			MorgulPlugin.log("" + e.toString());
		}

		MorgulPlugin.debug("SocketHandler " + socket.getRemoteSocketAddress() + " Shutting down.");
	}
}

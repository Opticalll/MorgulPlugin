package cz.opt.morgulplugin.event;

import java.net.Socket;
public class SocketEvent
{
	private Socket socket;
	private String cmdName;
	private String[] args;
	
	public SocketEvent(Socket soc, String cmdName, String[] args)
	{
		this.socket = soc;
		this.cmdName = cmdName;
		this.args = args;
	}

	/**
	 * @return the socket
	 */
	public Socket getSocket()
	{
		return socket;
	}

	/**
	 * @return the cmdName
	 */
	public String getCmdName()
	{
		return cmdName;
	}

	/**
	 * @return the args
	 */
	public String[] getArgs()
	{
		return args;
	}
	
	
}

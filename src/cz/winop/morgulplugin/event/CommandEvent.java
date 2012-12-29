package cz.winop.morgulplugin.event;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandEvent
{
	private CommandSender sender;
	private Command command;
	private String label;
	private String[] args;
	public CommandEvent(CommandSender sender, Command command, String label, String[] args)
	{
		this.sender = sender;
		this.command = command;
		this.label = label;
		this.args = args;
	}
	/**
	 * @return the sender
	 */
	public CommandSender getSender()
	{
		return sender;
	}
	/**
	 * @return the command
	 */
	public Command getCommand()
	{
		return command;
	}
	/**
	 * @return the label
	 */
	public String getLabel()
	{
		return label;
	}
	/**
	 * @return the args
	 */
	public String[] getArgs()
	{
		return args;
	}
	
}

package cz.opt.morgulplugin.structs;

import java.util.ArrayList;

import org.bukkit.ChatColor;

import cz.opt.morgulplugin.config.Config;
import cz.opt.morgulplugin.entity.MorgPlayer;
import cz.opt.morgulplugin.managers.ChatManager;
import cz.opt.morgulplugin.managers.LanguageManager;

public class ChatChannel
{
	private static final String CONF_FILE = "chat.conf"; 
	private static final String SECTION = "ChatChannel"; 
	private MorgPlayer channelManager;
	private ArrayList<MorgPlayer> players;
	private int maxPlayers;
	private ChatColor color;
	private String password;
    private String name;
	public ChatChannel(MorgPlayer pl, String channelName)
	{
		name = channelName;
		channelManager = pl;
		/*if(pl.vip)
		 * 	maxPlayers = Integer.parseInt(Config.get(SECTION, plCountVip));
		 * else
		 *  maxPlayers = Integer.parseInt(Config.get(SECTION, plCountBasic));
		 */
		maxPlayers = Config.get(CONF_FILE, SECTION, "plCountBasic", 3);
		players = new ArrayList<MorgPlayer>();
		color = ChatColor.WHITE;
		password = "";
		pl.joinChannel(this);
		ChatManager.addChannel(name, this);
	}
	
	public ChatChannel()
	{
		//Constructing general Chat;
		name = "World";
		channelManager = null;
		maxPlayers = Config.get(CONF_FILE, SECTION, "worldPlCount", 20);
		players = new ArrayList<MorgPlayer>();
		color = ChatColor.WHITE;
		ChatManager.addChannel(name, this);
	}
	
	public void changeChatColor(MorgPlayer pl, String arg) throws IllegalArgumentException
	{
		if(channelManager == pl)
			color = ChatColor.valueOf(arg);
		else
			LanguageManager.sendText(pl, "ChatChannel_Channel_NotOwner");
	}
	
	public void deleteChannel(MorgPlayer pl)
	{
		if(channelManager == pl)
		{
			ChatManager.removeChannel(name);
			for(int i = 0; i < players.size(); i++)
				pl.removeChannel(this);
		}
		else
			LanguageManager.sendText(pl, "ChatChannel_Channel_NotOwner");
	}
	
	public void sendMsg(String msg)
	{
		for(int i = 0; i < players.size(); i++)
			players.get(i).getPlayer().sendMessage(color + "[" + name + "]" + msg);
	}
	
	public void disconnect(MorgPlayer pl)
	{
		players.remove(pl);
		pl.removeChannel(this);
	}
	
	public void connect(MorgPlayer pl)
	{
		if(players.size() < maxPlayers)
		{
			if(players.contains(pl))
				LanguageManager.sendText(pl, "ChatChannel_Channel_ContainPl");
			else
			{
				players.add(pl);
				pl.addChannel(this);
			}
		}
		else
			LanguageManager.sendText(pl, "ChatChannel_Channel_Full");
	}
	
	public String getNameWithColor()
	{
		return color + name;
	}

	public String getName()
	{
		return name;
	}

	public String getPassword()
	{
		return password;
	}
	
	public boolean isPassword()
	{
		if(password.equals(""))
			return false;
		else
			return true;
	}
	
	public void setChannelManager(MorgPlayer pl)
	{
		this.channelManager = pl;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}
}

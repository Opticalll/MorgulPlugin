package cz.opt.morgulplugin.managers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import cz.opt.morgulplugin.config.Config;
import cz.opt.morgulplugin.database.DataBase;
import cz.opt.morgulplugin.entity.MorgPlayer;
import cz.opt.morgulplugin.event.CommandEvent;
import cz.opt.morgulplugin.listener.CommandListener;
import cz.opt.morgulplugin.structs.ChatChannel;

public class ChatManager implements CommandListener
{
	private static final String CONF_FILE = "chat.conf";
	private static final String SECTION = "Chat";
	private static int MAX_QUED_MESSEGES;
	private static Hashtable<String, ChatChannel> channels;
	private static ChatChannel worldChannel;
	public static ChatManager instance;
	
	public static void init()
	{
		instance = new ChatManager();
		channels = new Hashtable<String, ChatChannel>();
		worldChannel = new ChatChannel();
		MAX_QUED_MESSEGES = Config.get(CONF_FILE, SECTION, "Max_QuedMesseges", 5);
		CommandManager.registerListener("w", instance);
		CommandManager.registerListener("msg", instance);
		CommandManager.registerListener("whisper", instance);
		CommandManager.registerListener("chat", instance);
		CommandManager.registerListener("channel", instance);
	}
	
	public static void onPlayerChatEvent(AsyncPlayerChatEvent e)
	{
		ArrayList<ChatChannel> tempCha = PlayerManager.getPlayer(e.getPlayer().getName()).getChatChannels();
		if(PlayerManager.getPlayer(e.getPlayer().getName()).getChatStatusChannel() == "")
		{
			for(int i = 0; i < tempCha.size(); i++)
			{
				String msg = "[" + e.getPlayer().getName() + "]: " + e.getMessage();
				tempCha.get(i).sendMsg(msg);
			}
		}
		else
		{
			if(PlayerManager.getPlayer(e.getPlayer().getName()).getChatChannels().contains(channels.get(PlayerManager.getPlayer(e.getPlayer().getName()).getChatStatusChannel().toLowerCase())))
			{
				channels.get(PlayerManager.getPlayer(e.getPlayer().getName()).getChatStatusChannel().toLowerCase()).sendMsg("[" + e.getPlayer().getName() + "]: " + e.getMessage());
			}
		}
	}
	
	public static void addChannel(String name, ChatChannel e)
	{
		channels.put(name.toLowerCase(), e);
	}
	
	public static void removeChannel(String name)
	{
		channels.remove(name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandEvent e)
	{
		String senderName = "";
		if(e.getSender() instanceof Player)
			senderName = "[" + e.getSender().getName() + "]: ";
		else
			senderName = "[System]: ";
		if(e.getCommand().getName().equalsIgnoreCase("w") || e.getCommand().getName().equalsIgnoreCase("msg") || e.getCommand().getName().equalsIgnoreCase("whisper"))
		{
			if(e.getArgs().length < 2)
			{
				LanguageManager.sendText(PlayerManager.getPlayer(e.getSender().getName()), "ChatManager_Message_Syntax");
				return true;
			}
			if(PlayerManager.playerExist(e.getArgs()[0]))
			{
				String msg = "";
				for (int i = 1; i < e.getArgs().length; i++)
				    msg += e.getArgs()[i] + " "; 
				if(msg.length() > 242)
				{
					LanguageManager.sendText(PlayerManager.getPlayer(e.getSender().getName()), "ChatManager_Message_MsgTooLong");
					return true;
				}
				e.getSender().sendMessage(senderName + msg);
				sendMsg(e.getSender(), e.getArgs()[0], senderName + msg);
				return true;
			}
			else
			{
				LanguageManager.sendText(PlayerManager.getPlayer(e.getSender().getName()), "ChatManager_Message_PlayerDoesntExist", e.getArgs()[0]);
				return true;
			}
		}
		else if(e.getCommand().getName().equalsIgnoreCase("chat") && e.getSender() instanceof Player)
		{
			if(e.getArgs().length >= 1)
			{
				if(!e.getArgs()[0].toLowerCase().equalsIgnoreCase("all") && channels.get(e.getArgs()[0].toLowerCase()) == null)
				{
					e.getSender().sendMessage("Kanal neexistuje.");
					LanguageManager.sendText(PlayerManager.getPlayer(e.getSender().getName()), "ChatManager_Chat_ChannelDoesntExist");
					return true;
				}
				if(e.getArgs()[0].toLowerCase().equalsIgnoreCase("all") || PlayerManager.getPlayer(e.getSender().getName()).getChatChannels().contains(channels.get(e.getArgs()[0].toLowerCase())))
				{
					if(e.getArgs()[0].toLowerCase().equalsIgnoreCase("all"))
					{
						PlayerManager.getPlayer(e.getSender().getName()).setChatStatusChannel("");
						return true;
					}
					else
					{
						PlayerManager.getPlayer(e.getSender().getName()).setChatStatusChannel(channels.get(e.getArgs()[0].toLowerCase()).getName());
						return true;
					}
				}
				else
				{
					LanguageManager.sendText(PlayerManager.getPlayer(e.getSender().getName()), "ChatManager_Chat_NotConnected", e.getArgs()[0]);
					return true;
				}
			}
			else
			{
				LanguageManager.sendText(PlayerManager.getPlayer(e.getSender().getName()), "ChatManager_Chat_Syntax", e.getArgs()[0]);
				return true;
			}
		}
		else if(e.getCommand().getName().equalsIgnoreCase("channel") && e.getSender() instanceof Player)
		{
			if(e.getArgs().length < 1)
			{
				LanguageManager.sendText(PlayerManager.getPlayer(e.getSender().getName()), "ChatManager_Channel_Commands", " Create Delete Join Disconnect List.");
				return true;
			}
			else if(e.getArgs()[0].equalsIgnoreCase("join"))
			{
				if(e.getArgs().length < 2)
				{
					LanguageManager.sendText(PlayerManager.getPlayer(e.getSender().getName()), "ChatManager_Channel_Join_Syntax");
					return true;
				}
				if(channels.get(e.getArgs()[1].toLowerCase()) == null)
				{
					LanguageManager.sendText(PlayerManager.getPlayer(e.getSender().getName()), "ChatManager_Channel_Join_ChannelDoesntExist");
					return true;
				}
				if(channels.get(e.getArgs()[1].toLowerCase()).isPassword())
				{
					if(e.getArgs().length < 3)
					{
						LanguageManager.sendText(PlayerManager.getPlayer(e.getSender().getName()), "ChatManager_Channel_Join_ChannelHasPassword");
						return true;
					}
					if(!channels.get(e.getArgs()[1].toLowerCase()).getPassword().equals(LoginManager.hashMD5(e.getArgs()[2])))
					{
						LanguageManager.sendText(PlayerManager.getPlayer(e.getSender().getName()), "ChatManager_Channel_Join_WrongPassword");
						return true;
					}
				}
				PlayerManager.getPlayer(e.getSender().getName()).joinChannel(channels.get(e.getArgs()[1].toLowerCase()));
				LanguageManager.sendText(PlayerManager.getPlayer(e.getSender().getName()), "ChatManager_Channel_Join_Joined");
				return true;
			}
			else if(e.getArgs()[0].equalsIgnoreCase("disconnect"))
			{
				if(e.getArgs().length < 2)
				{
					e.getSender().sendMessage("Syntaxe je /channel disconnect [kanal]");
					return true;
				}
				if(!PlayerManager.getPlayer(e.getSender().getName()).getChatChannels().contains(channels.get(e.getArgs()[1].toLowerCase())))
				{
					e.getSender().sendMessage("V tomto kanalu nejste.");
					return true;
				}
				PlayerManager.getPlayer(e.getSender().getName()).disconnectChannel(channels.get(e.getArgs()[1].toLowerCase()));
				e.getSender().sendMessage("Byl jste odpojen.");
				return true;
			}
			else if(e.getArgs()[0].equalsIgnoreCase("list"))
			{
				String msg = "Channels: ";
				ArrayList<ChatChannel> list = PlayerManager.getPlayer(e.getSender().getName()).getChatChannels();
				for(int i = 0; i < list.size(); i++)
				{
					if(i != 0)
						msg += "             ";
					msg += list.get(i).getNameWithColor() + "\n";
				}
				e.getSender().sendMessage(msg);
				return true;
			}
			else if(e.getArgs()[0].equalsIgnoreCase("create"))
			{
				if(e.getArgs().length < 2)
				{
					e.getSender().sendMessage("Syntaxe je /channel create [kanal] [heslo]");
					return true;
				}
				if(channels.get(e.getArgs()[1].toLowerCase()) != null)
				{
					e.getSender().sendMessage("Tento kanal uz existuje.");
					return true;
				}
				if(e.getArgs()[1].equalsIgnoreCase("all") || e.getArgs()[1].equalsIgnoreCase("world"))
				{
					e.getSender().sendMessage("Takto se kanal jmenovat nemuze.");
					return true;
				}
				channels.put(e.getArgs()[1].toLowerCase(), new ChatChannel(PlayerManager.getPlayer(e.getSender().getName()), e.getArgs()[1]));
				if(e.getArgs().length > 2)
					channels.get(e.getArgs()[1].toLowerCase()).setPassword(LoginManager.hashMD5(e.getArgs()[2]));
				e.getSender().sendMessage("Kanal vytvoren");
				return true;
			}
			else if(e.getArgs()[0].equalsIgnoreCase("delete"))
			{
				if(e.getArgs().length < 2)
				{
					e.getSender().sendMessage("Syntaxe je /channel delete [kanal]");
					return true;
				}
				if(channels.get(e.getArgs()[1].toLowerCase()) == null)
				{
					e.getSender().sendMessage("Kanal neexistuje");
					return true;
				}
				channels.get(e.getArgs()[1].toLowerCase()).deleteChannel(PlayerManager.getPlayer(e.getSender().getName()));
				e.getSender().sendMessage("Kanal vymazan");
				return true;
			}
			else if(e.getArgs()[0].equalsIgnoreCase("modify"))
			{
				if(e.getArgs().length < 3)
				{
					e.getSender().sendMessage("Syntaxe je /channel modify [kanal] [function] [arguments]");
					return true;
				}
				if(channels.get(e.getArgs()[1].toLowerCase()) == null)
				{
					e.getSender().sendMessage("Kanal neexistuje");
					return true;
				}
				if(e.getArgs()[2].equalsIgnoreCase("color"))
				{
					if(e.getArgs().length < 4)
					{
						e.getSender().sendMessage("Syntaxe je /channel modify [kanal] color [color]");
						return true;
					}
					try
					{
						channels.get(e.getArgs()[1].toLowerCase()).changeChatColor(PlayerManager.getPlayer(e.getSender().getName()), e.getArgs()[3].toUpperCase());
						e.getSender().sendMessage("Zmena barvy se provedla.");
					} catch(IllegalArgumentException ex) {
						e.getSender().sendMessage("Barva neni Podporovana.");
					}
					return true;
				}
				else if(e.getArgs()[2].equalsIgnoreCase("changepass"))
				{
					if(e.getArgs().length < 4)
					{
						channels.get(e.getArgs()[1].toLowerCase()).setPassword("");
						e.getSender().sendMessage("Kanal jiz neni zaheslovan");
						return true;
					}
					else
					{
						channels.get(e.getArgs()[1].toLowerCase()).setPassword(LoginManager.hashMD5(e.getArgs()[3]));
						e.getSender().sendMessage("Heslo bylo zmeneno.");
						return true;
					}
				}
				else if(e.getArgs()[2].equalsIgnoreCase("changemanager"))
				{
					if(e.getArgs().length < 4)
					{
						e.getSender().sendMessage("Syntaxe je /channel modify [kanal] changemanager [playername]");
						return true;
					}
					if(PlayerManager.playerExist(e.getArgs()[3]))
					{
						e.getSender().sendMessage("Hrac neexistuje.");
						return true;
					}
					if(PlayerManager.isLoggedIn(e.getArgs()[3]))
					{
						e.getSender().sendMessage("Hrac neni online.");
						return true;
					}
					channels.get(e.getArgs()[1].toLowerCase()).setChannelManager(PlayerManager.getPlayer(e.getArgs()[3]));
					e.getSender().sendMessage("ChannelManager byl zmenen");
					return true;
				}
				else
				{
					e.getSender().sendMessage("Syntaxe je /channel modify [kanal] [function] [arguments]");
					return true;
				}
			}
			else
			{
				e.getSender().sendMessage("Commands Are Channel Create Delete Join Disconnect List Modify.");
				return true;
			}
				
		}
		return false;
	}
	
	public static boolean sendMsg(CommandSender sender, String name, String msg)
	{
		if(PlayerManager.getPlayer(name) != null)
		{
			PlayerManager.getPlayer(name).getPlayer().sendMessage(msg);
			return true;
		}
		else
		{
			//Player doesnt exist or is offline
			if(PlayerManager.playerExist(name))
			{
				//Save Qued msg.
				saveQuedMsg(sender, name, msg);
				return true;
			}
			else
			{
				sender.sendMessage("Hrac Jmenem " + name + " neexistuje.");
				return false;
			}
		}
	}
	
	private static void saveQuedMsg(CommandSender sender, String plName, String msg)
	{
		if(DataBase.query("Select * from qued_msg WHERE id='" + PlayerManager.getPlayerId(plName) + "'").keySet().size() >= MAX_QUED_MESSEGES)
		{
			sender.sendMessage("Hrac Jmenem " + plName + " presahl pocet cekajicich zprav. Vase zprava nebude dorucena.");
			return;
		}
		msg = new SimpleDateFormat("[dd.MM|HH:mm]").format(Calendar.getInstance().getTime()) + msg;
		DataBase.update("INSERT INTO qued_msg (id, msg) VALUES ('" + PlayerManager.getPlayerId(plName) + "', '" + msg + "')");
		
		sender.sendMessage("Hrac dostane zpravu a jse pripoji znovu do hry.");
	}
	
	public static void acceptQuedMsgs(MorgPlayer pl)
	{
		HashMap<Integer, HashMap<String, String>> rs = DataBase.query("Select * from qued_msg WHERE id='" + PlayerManager.getPlayerId(pl.getName()) + "'");
		for(int i = 1; i < rs.keySet().size() + 1; i++)
			pl.getPlayer().sendMessage(rs.get(i).get("msg"));
		DataBase.update("DELETE From qued_msg WHERE id='" + PlayerManager.getPlayerId(pl.getName()) + "'");
	}
	
	@Override
	public boolean isPreLogin(){ return false; }
	@Override
	public boolean isCmdCom(){ return true; }

	public static ChatChannel getWorldChannel()
	{
		return worldChannel;
	}
}

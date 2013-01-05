package cz.opt.morgulplugin.managers;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import cz.opt.morgulplugin.MorgulPlugin;
import cz.opt.morgulplugin.database.DataBase;
import cz.opt.morgulplugin.entity.MorgPlayer;

public class PlayerManager
{
	private static Hashtable<String, MorgPlayer> playerMap;
	private static List<LoginManager> loginList;
	
	public static void init()
	{
		playerMap = new Hashtable<String, MorgPlayer>();
		loginList = new ArrayList<LoginManager>();
	}

	public static void removeLoginManager(LoginManager log)
	{
		loginList.remove(log);
	}

	public static int getPlayerId(String name)
	{
		return Integer.parseInt(DataBase.query("SELECT * FROM players WHERE playername='" + name.toLowerCase() + "'").get(1).get("id"));
	}

	public static MorgPlayer getPlayer(String name)
	{
		return playerMap.get(name.toLowerCase());
	}
	
	public static boolean playerExist(String name)
	{
		if(playerMap.get(name.toLowerCase()) != null || DataBase.query("SELECT * FROM players WHERE playername='" + name.toLowerCase() + "'").keySet().size() > 0)
			return true;
		else
			return false;
	}

	public static boolean isLoggedIn(String name)
	{
		return playerMap.get(name.toLowerCase()).isLogged();
	}

	public static void onPlayerLogin(PlayerLoginEvent event) 
	{
		//Player Joined(didn't entered password)
		MorgulPlugin.debug("Player Joined " + event.getPlayer().getName().toLowerCase());
		playerMap.put(event.getPlayer().getName().toLowerCase(), new MorgPlayer(event.getPlayer().getName().toLowerCase(), event.getPlayer(), false));
		loginList.add(new LoginManager(event.getPlayer()));
	}

	public static void onPlayerDisconnect(PlayerQuitEvent event)
	{
		//Player Disconnected(clear memory)
		getPlayer(event.getPlayer().getName().toLowerCase()).disconnected();
		playerMap.remove(event.getPlayer().getName().toLowerCase());
	}
}

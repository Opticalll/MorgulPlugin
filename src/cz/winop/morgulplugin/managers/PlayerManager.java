package cz.winop.morgulplugin.managers;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import cz.winop.morgulplugin.MorgulPlugin;
import cz.winop.morgulplugin.entity.MorgPlayer;

public class PlayerManager implements Listener
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
		return playerMap.get(name).getId();
	}

	public static MorgPlayer getPlayer(String name)
	{
		return playerMap.get(name);
	}

	public static boolean isLoggedIn(String name)
	{
		return playerMap.get(name).isLogged();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerLogin(PlayerLoginEvent event) 
	{
		//Player Joined(didn't entered password)
		MorgulPlugin.debug("Player Joined " + event.getPlayer().getName());
		playerMap.put(event.getPlayer().getName(), new MorgPlayer(event.getPlayer().getName(), event.getPlayer(), false));
		loginList.add(new LoginManager(event.getPlayer()));
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDisconnect(PlayerQuitEvent event)
	{
		//Player Disconnected(clear memory)
		getPlayer(event.getPlayer().getName()).disconnected();
		playerMap.remove(event.getPlayer().getName());
	}
}

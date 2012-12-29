package cz.winop.morgulplugin.managers;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import cz.winop.morgulplugin.MorgulPlugin;
import cz.winop.morgulplugin.config.Config;
import cz.winop.morgulplugin.database.DataBase;
import cz.winop.morgulplugin.event.CommandEvent;
import cz.winop.morgulplugin.listener.CommandListener;

public class LoginManager implements CommandListener, Listener
{	
	private static final String SECTION = "Login";
	private static int LOGIN_TIMEOUT_SECONDS;
	private Player logingPlayer;
	
	public static void init()
	{
		LOGIN_TIMEOUT_SECONDS = Integer.parseInt(Config.get(SECTION, "timeout"));
	}
	
	public static String hashMD5(String pass)
	{
		String hashWord = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(pass.getBytes());
			BigInteger hash = new BigInteger(1, md5.digest());
			hashWord = hash.toString(16);
		} catch (NoSuchAlgorithmException nsae){}
		return hashWord;
	}
	
	public LoginManager(Player pl)
	{
		logingPlayer = pl;
		
		CommandManager.registerPreLoginListener(this);
		MorgulPlugin.thisPlugin.getServer().getPluginManager().registerEvents(this, MorgulPlugin.thisPlugin);
		
		if((PlayerManager.getPlayer(pl.getName()).getLastPlayed() + (LOGIN_TIMEOUT_SECONDS * 1000)) > System.currentTimeMillis())
		{
			PlayerManager.getPlayer(logingPlayer.getName()).setLogged(true);
			PlayerManager.removeLoginManager(this);
			MorgulPlugin.debug("Has been loged in " + LOGIN_TIMEOUT_SECONDS + " Sec.");
		}
		MorgulPlugin.debug("Login Manager Constructed.");
	}
	
	private boolean checkPassword(String pass)
	{
		ResultSet rs = null;
		rs = DataBase.query("SELECT * FROM players WHERE playername='" + logingPlayer.getName() + "'");
		try
		{
			if(rs.next())
			{
				if(rs.getString("pass").isEmpty())
				{
					setNewPass(pass);
					logingPlayer.sendMessage("Heslo Nastaveno.");
					return true;
				}
				if(pass.equals(rs.getString("pass")))
					return true;
				else
					return false;
			}
			else
				return false;
		} catch (SQLException e) {
			MorgulPlugin.log(e.getMessage());
			return false;
		} finally {
			try
			{
				rs.close();
			} catch (SQLException e)
			{
				MorgulPlugin.log("Database SQL Error: " + e.getMessage());
			}
		}
	}
	
	private void setNewPass(String pass)
	{
		DataBase.update("UPDATE players SET pass='" + pass + "' WHERE playername='" + logingPlayer.getName() + "'");
		MorgulPlugin.debug("New Pass Set.");
	}
	
	@Override
	public void onCommand(CommandEvent e)
	{
		if(e.getCommand().getName().equalsIgnoreCase("login") && e.getSender() == logingPlayer)
		{
			if(e.getArgs().length > 1)
			{
				logingPlayer.sendMessage("Heslo nesmi obsahovat mezery.");
				return;
			}
			else if(e.getArgs().length < 1)
			{
				logingPlayer.sendMessage("Syntaxe je /login [Heslo]");
				return;
			}
			MorgulPlugin.debug("Player login.");
			if(checkPassword(LoginManager.hashMD5(e.getArgs()[0])))
			{
				logingPlayer.sendMessage("Loged.");
				PlayerManager.getPlayer(logingPlayer.getName()).setLogged(true);
				PlayerManager.removeLoginManager(this);
				MorgulPlugin.debug("Player Loged.");
			}
			else
				logingPlayer.sendMessage("Heslo neni spravne.");
		}
	}
	
	
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerMove(PlayerMoveEvent event) 
	{
		//Player Joined(didn't entered password)
		if(!PlayerManager.getPlayer(event.getPlayer().getName()).isLogged())
			event.setCancelled(true);
		
	}
	
}

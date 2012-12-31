package cz.opt.morgulplugin.managers;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import org.bukkit.entity.Player;

import cz.opt.morgulplugin.MorgulPlugin;
import cz.opt.morgulplugin.config.Config;
import cz.opt.morgulplugin.database.DataBase;
import cz.opt.morgulplugin.event.CommandEvent;
import cz.opt.morgulplugin.listener.CommandListener;
import cz.opt.morgulplugin.runnable.AcceptQuedMessages;

public class LoginManager implements CommandListener
{	
	private static final String SECTION = "Login";
	private static int LOGIN_TIMEOUT_SECONDS;
	private Player logingPlayer;
	
	private boolean preLogin = true;
	private boolean cmdCom = false;
	
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
		
		CommandManager.registerLoginListener(this);
		MorgulPlugin.debug("Login Manager Constructed.");
		if((PlayerManager.getPlayer(pl.getName()).getLastPlayed() + (LOGIN_TIMEOUT_SECONDS * 1000)) > System.currentTimeMillis())
		{
			PlayerManager.getPlayer(logingPlayer.getName()).setLogged(true);
			PlayerManager.removeLoginManager(this);
			CommandManager.removeLoginListener(this);
			MorgulPlugin.debug("Has been loged in " + LOGIN_TIMEOUT_SECONDS + " Sec.");
			MorgulPlugin.thisPlugin.getServer().getScheduler().scheduleSyncDelayedTask(MorgulPlugin.thisPlugin, new AcceptQuedMessages(PlayerManager.getPlayer(logingPlayer.getName())), 100L);
		}
	}
	
	private boolean checkPassword(String pass)
	{
		HashMap<Integer, HashMap<String, String>> rs = DataBase.query("SELECT * FROM players WHERE playername='" + logingPlayer.getName() + "'");
		if(rs.get(1).get("pass").isEmpty())
		{
			setNewPass(pass);
			logingPlayer.sendMessage("Heslo Nastaveno.");
			return true;
		}
		if(pass.equals(rs.get(1).get("pass")))
			return true;
		else
			return false;
	}
	
	private void setNewPass(String pass)
	{
		DataBase.update("UPDATE players SET pass='" + pass + "' WHERE playername='" + logingPlayer.getName() + "'");
		MorgulPlugin.debug("New Pass Set.");
	}
	
	@Override
	public boolean onCommand(CommandEvent e)
	{
		if(e.getCommand().getName().equalsIgnoreCase("login") && e.getSender() == logingPlayer)
		{
			if(e.getArgs().length > 1)
			{
				logingPlayer.sendMessage("Heslo nesmi obsahovat mezery.");
				return true;
			}
			if(e.getArgs().length < 1)
			{
				logingPlayer.sendMessage("Syntaxe je /login [Heslo]");
				return true;
			}
			MorgulPlugin.debug("Player login.");
			if(checkPassword(LoginManager.hashMD5(e.getArgs()[0])))
			{
				logingPlayer.sendMessage("Loged.");
				PlayerManager.getPlayer(logingPlayer.getName()).setLogged(true);
				PlayerManager.removeLoginManager(this);
				CommandManager.removeLoginListener(this);
				MorgulPlugin.debug("Player Loged.");
				ChatManager.acceptQuedMsgs(PlayerManager.getPlayer(logingPlayer.getName()));
				MorgulPlugin.debug("Player Accept Qued Msgs.");
				return true;
			}
			else
			{
				logingPlayer.sendMessage("Heslo neni spravne.");
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isPreLogin() { return preLogin; }

	@Override
	public boolean isCmdCom() { return cmdCom; }
}

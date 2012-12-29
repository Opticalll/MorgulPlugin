package cz.winop.morgulplugin.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import cz.winop.morgulplugin.MorgulPlugin;
import cz.winop.morgulplugin.database.DataBase;
import cz.winop.morgulplugin.utils.Utils;

public class MorgPlayer
{
	private int id;
	private String name;
	private Player pl;
	private boolean logged;
	private Location logLoc;
	private long lastPlayed;
	
	public MorgPlayer(String name, Player pl, boolean logged)
	{
		this.name = name;
		this.logged = logged;
		this.pl = pl;
		loadUserData();
		pl.teleport(logLoc);
	}
	
	private void loadUserData()
	{
		ResultSet rs = null;
		try
		{	
			if((rs = DataBase.query("SELECT * FROM players WHERE playername='" + name + "'")).next())
			{
				id = rs.getInt("id");
				logLoc = Utils.getLocFromString(rs.getString("location"), pl.getWorld());
				lastPlayed = rs.getLong("lastplayed");
				//store user info.
			}
			else
				newPlayer();
			MorgulPlugin.debug("Player Data Loaded.");
		} catch (SQLException e) {
			MorgulPlugin.log("Database SQL Error: " + e.getMessage());
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
	
	private void newPlayer()
	{
		DataBase.update("INSERT INTO players (playername, location) VALUES('" + this.name + "', '" + Utils.getLocAsString(getPlayer().getLocation()) + "')");
		MorgulPlugin.debug("New Player Created.");
		loadUserData();
	}
	
	public void disconnected()
	{
		DataBase.update("UPDATE players SET location='" + Utils.getLocAsString(getPlayer().getLocation()) + "', lastplayed='" + System.currentTimeMillis() + "' WHERE id='" + id + "'");
	}
	
	/**
	 * @return the logged
	 */
	public boolean isLogged()
	{
		return logged;
	}
	/**
	 * @param logged the logged to set
	 */
	public void setLogged(boolean logged)
	{
		this.logged = logged;
	}
	/**
	 * @return the id
	 */
	public int getId()
	{
		return id;
	}
	
	public Player getPlayer()
	{
		return pl;
	}

	public long getLastPlayed()
	{
		return lastPlayed;
	}
}

package cz.winop.morgulplugin.runnable;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Teleport implements Runnable
{
	private Player pl;
	private Location loc;
	
	public Teleport(Player pl, Location loc)
	{
		this.pl = pl;
		this.loc = loc;
	}
	@Override
	public void run()
	{
		pl.teleport(loc);
	}

}

package cz.winop.morgulplugin.utils;

import org.bukkit.Location;
import org.bukkit.World;

public class Utils
{
	public static String getLocAsString(Location loc)
	{
		return loc.getX() + "~" + loc.getY() + "~" + loc.getBlockZ() + "~" + loc.getYaw() + "~" + loc.getPitch();
	}
	
	public static Location getLocFromString(String input, World wrld)
	{
		String[] values = input.split("~");
		return new Location(wrld, Double.parseDouble(values[0]), Double.parseDouble(values[1]), Double.parseDouble(values[2]), Float.parseFloat(values[3]), Float.parseFloat(values[4]));
	}
}

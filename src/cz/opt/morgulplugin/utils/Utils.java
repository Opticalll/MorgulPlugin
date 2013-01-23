package cz.opt.morgulplugin.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
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
	
	public static <T, P> List<P> mapToList(Map<T, P> map)
	{
		List<P> list = new ArrayList<P>();
		
		Iterator<P> i = map.values().iterator();
		while(i.hasNext())
		{
			P next = i.next();
			list.add(next);
		}
		
		return list;
	}
	
	public static boolean isNight(String worldName)
	{
		Long now = Bukkit.getWorld(worldName).getTime();
		if(now > 12000 || now < 0)
			return true;
		else
			return false;
	}
	
	public static boolean isDay(String worldName)
	{
		Long now = Bukkit.getWorld(worldName).getTime();
		if(now < 12000 || now > 0)
			return true;
		else
			return false;
	}
}

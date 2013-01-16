package cz.opt.morgulplugin.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.bukkit.Material;
import org.getspout.spoutapi.inventory.SpoutItemStack;

import cz.opt.morgulplugin.config.Config;
import cz.opt.morgulplugin.item.Coin;

public class CoinManager
{
	private static final String CONFIGFILE = "coins.conf";
	
	public static ArrayList<Coin> coinList;
	
	public static Coin getCoinByMaterial(String mat)
	{
		for(int i = 0; i < coinList.size(); i++)
		{
			if(coinList.get(i).getName().equalsIgnoreCase(mat))
				return coinList.get(i);
		}
		return null;
	}
	
	public static void init()
	{
		coinList = new ArrayList<Coin>();
		HashMap<String, HashMap<String, String>> map = Config.getFileMap(CONFIGFILE);
		Object[] keys = map.keySet().toArray();
		for(int i = 0; i < keys.length; i++)
		{
			HashMap<String, String> values = map.get(keys[i]);
			coinList.add(new Coin(values.get("name"), values.get("texturename"), Integer.parseInt(values.get("value"))));
		}
		Collections.sort(coinList, new Comparator<Coin>()
		{
			   public int compare(Coin b1, Coin b2) 
			   {
				   if(b1.getValue() > b2.getValue())
					   return +1;
				   else if(b1.getValue() < b2.getValue())
					   return -1;
				   return 0;
			   }
		}
		);
	}
}

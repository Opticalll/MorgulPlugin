package cz.opt.morgulplugin.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.bukkit.Location;
import org.getspout.spoutapi.inventory.SpoutItemStack;

import cz.opt.morgulplugin.MorgulPlugin;
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
	
	public static void dropCoins(Location loc, int value)
	{
		ArrayList<SpoutItemStack> coins = new ArrayList<SpoutItemStack>();
		for(int i = coinList.size() - 1; i > -1; i--)
		{
			int coinNumber = value/coinList.get(i).getValue();
			MorgulPlugin.debug(coinList.get(i).getValue() + "=" + coinNumber);
			if(coinNumber == 0)
				continue;
			else
			{
				SpoutItemStack coin = new SpoutItemStack(coinList.get(i));
				coin.setAmount(coinNumber);
				coins.add(coin);
				value = value%coinList.get(i).getValue();
			}
		}
		for(int i = 0; i < coins.size(); i++)
			loc.getWorld().dropItem(loc, coins.get(i));
	}
}

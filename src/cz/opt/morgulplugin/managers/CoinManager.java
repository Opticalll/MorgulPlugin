package cz.opt.morgulplugin.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import org.getspout.spoutapi.inventory.SpoutShapelessRecipe;

import cz.opt.morgulplugin.MorgulPlugin;
import cz.opt.morgulplugin.config.Config;
import cz.opt.morgulplugin.item.Coin;

public class CoinManager
{
	private static final String CONFIGFILE = "coins.conf";
	
	public static ArrayList<Coin> coinList;
	
	public static void sortCoinList()
	{
		ArrayList<Coin> tempList = new ArrayList<Coin>(); 
		int min_Value = 0;
		int index = 0;
		for(int i = 0; i < coinList.size(); i++)
		{
			min_Value = coinList.get(i).getValue();
			index = i;
			for(int z = 0; z < coinList.size(); z++)
			{
				if(min_Value > coinList.get(z).getValue())
				{
					min_Value = coinList.get(z).getValue();
					index = z;
				}
			}
			tempList.add(coinList.get(index));
			coinList.remove(index);
			index = 0;
		}
		coinList = tempList;
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
		setRecipes();
	}
	
	private static void setRecipes()
	{
		for(int i = 0; i < coinList.size(); i++)
		{
			if(i != coinList.size() - 1)
			{
				Coin lowerValue = coinList.get(i);
				MorgulPlugin.debug(lowerValue.getName());
				Coin higherValue = coinList.get(i + 1);
				MorgulPlugin.debug(higherValue.getName());
				SpoutItemStack higherValueCoin = new SpoutItemStack(higherValue, 1);
				SpoutItemStack lowerValueCoin = new SpoutItemStack(lowerValue, 10);
			    SpoutShapelessRecipe higherValueCoinRecipe = new SpoutShapelessRecipe(higherValueCoin);
			    higherValueCoinRecipe.addIngredient(lowerValueCoin.getMaterial());
			    SpoutManager.getMaterialManager().registerSpoutRecipe(higherValueCoinRecipe);
			}
		}
	}
}

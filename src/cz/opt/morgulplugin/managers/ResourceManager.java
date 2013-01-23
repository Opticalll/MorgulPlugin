package cz.opt.morgulplugin.managers;

import java.io.File;
import java.util.ArrayList;

import org.getspout.spoutapi.SpoutManager;

import cz.opt.morgulplugin.MorgulPlugin;
import cz.opt.morgulplugin.gui.popout.CoinExchange;
import cz.opt.morgulplugin.structs.CustomItem;

public class ResourceManager
{
	private static ArrayList<String> moreTextures;
	
	public static void init()
	{
		moreTextures = new ArrayList<String>();
		moreTextures.add(CoinExchange.getTextureName());
		
		CustomItem.init();
	}
	
	
	public static void setUpCache()
	{
		for(int i = 0; i < moreTextures.size(); i++)
			SpoutManager.getFileManager().addToCache(MorgulPlugin.thisPlugin, new File(moreTextures.get(i)));
		CustomItem.setUpCache();
	}
}

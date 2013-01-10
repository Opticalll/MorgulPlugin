package cz.opt.morgulplugin.structs;

import java.io.File;
import java.util.ArrayList;

import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.material.item.GenericCustomItem;

import cz.opt.morgulplugin.MorgulPlugin;
import cz.opt.morgulplugin.item.CopperCoin;
import cz.opt.morgulplugin.item.GoldCoin;
import cz.opt.morgulplugin.item.MorgulCoin;
import cz.opt.morgulplugin.item.SilverCoin;


public class CustomItem extends GenericCustomItem
{
	private static final String TEXTUREDIRECTORYPATH = "Morgul/textures/";
	private static ArrayList<CustomItem> itemList;
	
	
	public static void init()
	{
		itemList = new ArrayList<CustomItem>();
		new GoldCoin();
		new MorgulCoin();
		new SilverCoin();
		new CopperCoin();
		for(int i = 0; i < itemList.size(); i++)
		{
			MorgulPlugin.debug("" + itemList.get(i).getTexturePath());
			SpoutManager.getFileManager().addToCache(MorgulPlugin.thisPlugin, new File(itemList.get(i).getTexturePath()));
		}
	}
	
	protected String name;
	protected String texFileName;

	public CustomItem(String name, String texFileName)
	{
		super(MorgulPlugin.thisPlugin, name, TEXTUREDIRECTORYPATH + texFileName);
		this.name = name;
		this.texFileName = texFileName;
		itemList.add(this);
	}
	
	public String getTexturePath()
	{
		return TEXTUREDIRECTORYPATH + texFileName;
	}
}

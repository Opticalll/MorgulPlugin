package cz.opt.morgulplugin.structs;

import java.io.File;
import java.util.ArrayList;

import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.material.item.GenericCustomItem;

import cz.opt.morgulplugin.MorgulPlugin;


public class CustomItem extends GenericCustomItem
{
	private static final String TEXTUREDIRECTORYPATH = "Morgul/textures/";
	private static ArrayList<CustomItem> itemList;
	
	
	public static void init()
	{
		itemList = new ArrayList<CustomItem>();
	}
	
	public static void setUpCache()
	{
		for(int i = 0; i < itemList.size(); i++)
			SpoutManager.getFileManager().addToCache(MorgulPlugin.thisPlugin, new File(itemList.get(i).getTexturePath()));
	}
	
	protected String texFileName;

	public CustomItem(String name, String texFileName)
	{
		super(MorgulPlugin.thisPlugin, name, TEXTUREDIRECTORYPATH + texFileName);
		this.texFileName = texFileName;
		itemList.add(this);
	}
	
	public String getTexturePath()
	{
		return TEXTUREDIRECTORYPATH + texFileName;
	}
}

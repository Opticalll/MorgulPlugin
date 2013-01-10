package cz.opt.morgulplugin.structs;

import org.getspout.spoutapi.material.item.GenericCustomItem;

import cz.opt.morgulplugin.MorgulPlugin;


public class CustomItem extends GenericCustomItem
{
	private static final String TEXTUREDIRECTORYPATH = "/Morgul/textures/";

	protected String name;
	protected String texFileName;

	public CustomItem(String name, String texFileName)
	{
		super(MorgulPlugin.thisPlugin, name, TEXTUREDIRECTORYPATH + texFileName);
		this.name = name;
		this.texFileName = texFileName;
	}
	
	public String getTexturePath()
	{
		return TEXTUREDIRECTORYPATH + texFileName;
	}
}

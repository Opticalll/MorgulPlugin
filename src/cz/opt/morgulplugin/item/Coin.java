package cz.opt.morgulplugin.item;

import cz.opt.morgulplugin.structs.CustomItem;

public class Coin extends CustomItem
{
	private int value;

	public Coin(String name, String texFileName, int value)
	{
		super(name, texFileName);
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}
}

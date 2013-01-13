package cz.opt.morgulplugin.gui.widget;

import org.getspout.spoutapi.gui.GenericLabel;

public class Label extends GenericLabel
{
	public Label(int x, int y, String text)
	{
		super(text);
		this.setMargin(y, 0, 0, x);
		this.setX(x);
		this.setY(y);
	}
}

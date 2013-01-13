package cz.opt.morgulplugin.gui.widget.extend;

import java.awt.Point;
import java.util.ArrayList;

import org.getspout.spoutapi.event.screen.ScreenCloseEvent;
import org.getspout.spoutapi.gui.Widget;

public interface InventoryViewExtention
{
	public Widget getBackground();
	public ArrayList<Widget> getWidgets();
	public Point getInventoryOffSet();
	public void onScreenClose(ScreenCloseEvent e);
}

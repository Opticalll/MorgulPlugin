package cz.opt.morgulplugin.gui.widget;

import java.util.ArrayList;

import org.getspout.spoutapi.event.screen.ButtonClickEvent;
import org.getspout.spoutapi.gui.GenericButton;

import cz.opt.morgulplugin.listener.ActionButtonListener;

public class ActionButton extends GenericButton
{
	private ArrayList<ActionButtonListener> listeners;
	
	public ActionButton(int x, int y)
	{
		super();
		this.setMargin(y, 0, 0, x);
		this.setX(x);
		this.setY(y);
		listeners = new ArrayList<ActionButtonListener>();
	}
	
	public void addListener(ActionButtonListener listener)
	{
		listeners.add(listener);
	}

	public void removeListener(ActionButtonListener listener)
	{
		listeners.remove(listener);
	}
	
	@Override
	public void onButtonClick(ButtonClickEvent event)
	{
		for(int i = 0; i < listeners.size(); i++)
			listeners.get(i).onButtonClicked(event);
	}
}

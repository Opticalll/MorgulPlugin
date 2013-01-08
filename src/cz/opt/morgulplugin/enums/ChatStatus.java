package cz.opt.morgulplugin.enums;

import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.GenericButton;

public class ChatStatus
{
	private GenericButton button;
	private String channel;
	private Color color;
	private boolean all;
	
	public ChatStatus(GenericButton button)
	{
		this.button = button;
		channel = "";
		all = true;
		updateButton();
	}
	
	private void updateButton()
	{
		if(!all)
			button.setText(channel);
		else
		{
			button.setText("All");
			color = new Color("White");
		}
		button.setTextColor(color);
	}
	
	public void setChannel(String channel, Color color)
	{
		this.channel = channel;
		if(channel.isEmpty())
			all = true;
		this.color = color;
		updateButton();
	}
}

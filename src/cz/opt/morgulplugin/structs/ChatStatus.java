package cz.opt.morgulplugin.structs;

import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.Screen;

import cz.opt.morgulplugin.MorgulPlugin;

public class ChatStatus
{
	private GenericButton button;
	private String channel;
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
		Screen sc = button.getScreen();
		sc.removeWidget(button);
		if(!all)

			button.setText(channel);
		else
			button.setText("All");
		sc.attachWidget(MorgulPlugin.thisPlugin, button);
	}
	
	public void setChannel(String channel)
	{
		this.channel = channel;
		if(channel.isEmpty())
			all = true;
		else
			all = false;
		updateButton();
	}

	public String getChannel()
	{
		return channel;
	}
	
}

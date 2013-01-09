package cz.opt.morgulplugin.structs;

import org.getspout.spoutapi.gui.GenericButton;

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
		if(!all)
			button.setText(channel);
		else
			button.setText("All");
		button.setDirty(true);
		button.onTick();
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

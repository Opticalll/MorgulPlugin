package cz.opt.morgulplugin.enums;

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
	}
	
	private void updateButton()
	{
		if(!all)
			button.setText(channel);
		else
			button.setText("All");
	}
}

package cz.opt.morgulplugin.structs;

import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.GenericLabel;

import cz.opt.morgulplugin.config.Config;

public class ChatStatus
{
	private static final String CONF_FILE = "gui.conf";
	private static final String SECTION = "ChatStatus";
	private final int WIDTH = Config.get(CONF_FILE, SECTION, "ChatStatus_Width", 75);
	private final int HEIGHT = Config.get(CONF_FILE, SECTION, "ChatStatus_Height", 10);
	private final int X = Config.get(CONF_FILE, SECTION, "ChatStatus_X", 0);
	private final int Y = Config.get(CONF_FILE, SECTION, "ChatStatus_Y", 214);
	private GenericButton button;
	private GenericLabel label;
	private String channel;
	private boolean all;
	
	public ChatStatus()
	{
		GenericButton statusButton = new GenericButton();
		statusButton.setX(X);
		statusButton.setY(Y);
		statusButton.setWidth(WIDTH);
		statusButton.setHeight(HEIGHT);
		statusButton.setText("");
		this.button = statusButton;
		this.label = new GenericLabel("All");
		this.label.setWidth(WIDTH);
		this.label.setHeight(HEIGHT);
		channel = "";
		all = true;
		update();
	}
	
	private void update()
	{
		if(!all)
			label.setText(channel);
		else
			label.setText("All");
		this.button.setWidth(WIDTH);
		this.button.setHeight(HEIGHT);
		if(GenericLabel.getStringWidth(label.getText()) > button.getWidth())
			button.setWidth(GenericLabel.getStringWidth(label.getText()) + 2);
		if(GenericLabel.getStringHeight(label.getText()) + 2 > button.getHeight())
			button.setHeight(GenericLabel.getStringHeight(label.getText()) + 4);
		label.setX(button.getX() + button.getWidth()/2 - GenericLabel.getStringWidth(label.getText())/2);
		label.setY(button.getY() + button.getHeight()/2 - GenericLabel.getStringHeight(label.getText())/2);
		label.setDirty(true);
		label.onTick();
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
		update();
	}

	public String getChannel()
	{
		return channel;
	}
	
	public GenericButton getButton()
	{
		return button;
	}
	
	public GenericLabel getLabel()
	{
		return label;
	}
	
}

package cz.opt.morgulplugin.gui.popout;

import java.awt.Point;
import java.util.ArrayList;

import org.bukkit.Material;
import org.getspout.spoutapi.event.screen.ScreenCloseEvent;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.Widget;
import org.getspout.spoutapi.player.SpoutPlayer;

import cz.opt.morgulplugin.config.Config;
import cz.opt.morgulplugin.gui.widget.InventorySlot;
import cz.opt.morgulplugin.gui.widget.InventoryWidget;
import cz.opt.morgulplugin.gui.widget.extend.InventoryViewExtention;

public class CoinExchange implements InventoryViewExtention
{
	private static final String CONF_FILE = "gui.conf";
	private static final String SECTION = "CoinExchanger";
	InventoryWidget invWidget;
	ArrayList<Widget> widgets;
	ArrayList<InventorySlot> slots;
	
	public CoinExchange(SpoutPlayer pl)
	{
		invWidget = new InventoryWidget(pl, Integer.parseInt(Config.get(CONF_FILE, SECTION, "CoinExchanger_Width")), Integer.parseInt(Config.get(CONF_FILE, SECTION, "CoinExchanger_Height")), this);
		widgets = new ArrayList<Widget>();
		slots = new ArrayList<InventorySlot>();
		GenericLabel label = new GenericLabel("Coin Exchanger");
		label.setX(15).setY(15);
		widgets.add(label);
		InventorySlot slot = new InventorySlot(Integer.parseInt(Config.get(CONF_FILE, SECTION, "Slot_Input1_X")), Integer.parseInt(Config.get(CONF_FILE, SECTION, "Slot_Input1_Y")), invWidget);
		widgets.add(slot);
		slots.add(slot);
		slot = new InventorySlot(Integer.parseInt(Config.get(CONF_FILE, SECTION, "Slot_Input2_X")), Integer.parseInt(Config.get(CONF_FILE, SECTION, "Slot_Input2_X")), invWidget);
		widgets.add(slot);
		slots.add(slot);
		slot = new InventorySlot(Integer.parseInt(Config.get(CONF_FILE, SECTION, "Slot_Output1_X")), Integer.parseInt(Config.get(CONF_FILE, SECTION, "Slot_Output1_X")), invWidget);
		slot.setReadOnly(true);
		widgets.add(slot);
		slots.add(slot);
		slot = new InventorySlot(Integer.parseInt(Config.get(CONF_FILE, SECTION, "Slot_Output2_X")), Integer.parseInt(Config.get(CONF_FILE, SECTION, "Slot_Output2_X")), invWidget);
		slot.setReadOnly(true);
		widgets.add(slot);
		slots.add(slot);
		invWidget.initGUI();
	}

	@Override
	public Widget getBackground()
	{
		return new GenericTexture("Morgul/texture/" + Config.get(CONF_FILE, SECTION, "CoinExchanger_BackGround"));
	}

	@Override
	public ArrayList<Widget> getWidgets()
	{
		return widgets;
	}

	@Override
	public Point getInventoryOffSet()
	{
		return new Point(Integer.parseInt(Config.get(CONF_FILE, SECTION, "Inventory_PlayerInventory_X")), Integer.parseInt(Config.get(CONF_FILE, SECTION, "Inventory_PlayerInventory_Y")));
	}

	@Override
	public void onScreenClose(ScreenCloseEvent e)
	{
		for(int i = 0; i < slots.size(); i++)
		{
			InventorySlot slot = slots.get(i);
			if(slot.getItem().getType() != Material.AIR)
				invWidget.eject(slot.getItem());
		}
	}
}
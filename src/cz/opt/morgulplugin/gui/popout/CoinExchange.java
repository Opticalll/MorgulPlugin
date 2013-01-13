package cz.opt.morgulplugin.gui.popout;

import java.awt.Point;
import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.event.screen.ScreenCloseEvent;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.Widget;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import org.getspout.spoutapi.player.SpoutPlayer;

import cz.opt.morgulplugin.config.Config;
import cz.opt.morgulplugin.gui.widget.ActionSlot;
import cz.opt.morgulplugin.gui.widget.InventoryWidget;
import cz.opt.morgulplugin.gui.widget.extend.InventoryViewExtention;
import cz.opt.morgulplugin.item.Coin;
import cz.opt.morgulplugin.listener.ActionSlotListener;
import cz.opt.morgulplugin.managers.CoinManager;

public class CoinExchange implements InventoryViewExtention, ActionSlotListener
{
	private static final String CONF_FILE = "gui.conf";
	private static final String SECTION = "CoinExchanger";
	InventoryWidget invWidget;
	ArrayList<Widget> widgets;
	ArrayList<ActionSlot> slots;
	
	public CoinExchange(SpoutPlayer pl)
	{
		invWidget = new InventoryWidget(pl, Integer.parseInt(Config.get(CONF_FILE, SECTION, "CoinExchanger_Width")), Integer.parseInt(Config.get(CONF_FILE, SECTION, "CoinExchanger_Height")), this);
		widgets = new ArrayList<Widget>();
		slots = new ArrayList<ActionSlot>();
		GenericLabel label = new GenericLabel("Coin Exchanger");
		label.setX(15).setY(15);
		widgets.add(label);
		ActionSlot slot1 = new ActionSlot(Integer.parseInt(Config.get(CONF_FILE, SECTION, "Slot_Input1_X")), Integer.parseInt(Config.get(CONF_FILE, SECTION, "Slot_Input1_Y")), this);
		addSlot(slot1);
		ActionSlot slot2 = new ActionSlot(Integer.parseInt(Config.get(CONF_FILE, SECTION, "Slot_Input2_X")), Integer.parseInt(Config.get(CONF_FILE, SECTION, "Slot_Input2_Y")), this);
		addSlot(slot2);
		ActionSlot slot3 = new ActionSlot(Integer.parseInt(Config.get(CONF_FILE, SECTION, "Slot_Output1_X")), Integer.parseInt(Config.get(CONF_FILE, SECTION, "Slot_Output1_Y")), this);
		slot3.setReadOnly(true);
		addSlot(slot3);
		ActionSlot slot4 = new ActionSlot(Integer.parseInt(Config.get(CONF_FILE, SECTION, "Slot_Output2_X")), Integer.parseInt(Config.get(CONF_FILE, SECTION, "Slot_Output2_Y")), this);
		slot4.setReadOnly(true);
		addSlot(slot4);
		invWidget.initGUI();
	}
	
	private void addSlot(ActionSlot slot)
	{
		widgets.add(slot);
		slots.add(slot);
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
			ActionSlot slot = slots.get(i);
			if(slot.getItem().getType() != Material.AIR && !slots.get(i).isReadOnly())
				invWidget.eject(slot.getItem());
		}
	}

	@Override
	public boolean onItemExchange(ItemStack current, ItemStack cursor, ActionSlot source)
	{
		if(source.isReadOnly())
			return false;
		return true;
	}

	@Override
	public boolean onItemPut(ItemStack item, ActionSlot source)
	{
		if(source.isReadOnly())
			return false;
		if(source == slots.get(0) || source == slots.get(1))
		{
			ItemStack in1 = slots.get(0).getItem();
			ItemStack in2 = slots.get(1).getItem();
			SpoutItemStack workingStack;
			if(in1.getType() != Material.AIR)
				workingStack = new SpoutItemStack(in1);
			else if(in2.getType() != Material.AIR && in1.getType() == Material.AIR)
				workingStack = new SpoutItemStack(in2);
			else
				return true;
			
			ArrayList<Coin> coinList = CoinManager.coinList;
			int index = 0;
			boolean coin = false;
			for(int i = 0; i < coinList.size(); i++)
			{
				if(workingStack.isSimilar(new SpoutItemStack(coinList.get(i))))
				{
					coin = true;
					index = i;
				}
			}
			if(!coin)
				return false;
		
			if(in1.getType() == in2.getType())
				workingStack.setAmount(workingStack.getAmount() + in2.getAmount());
			

				if(workingStack.getAmount() > coinList.get(index + 1).getValue()/coinList.get(index).getValue() && index != coinList.size() - 2)
				{
					ActionSlot outputSlot = slots.get(2);
				    outputSlot.setItem(new SpoutItemStack(coinList.get(index + 1)));
				}
				else if(index > 0)
				{
					ActionSlot outputSlot1 = slots.get(2);
					ActionSlot outputSlot2 = slots.get(3);
					if(coinList.get(index).getValue()/coinList.get(index - 1).getValue() > 64)
					{
						SpoutItemStack stack = new SpoutItemStack(coinList.get(index - 1));
						stack.setAmount(64);
						outputSlot1.setItem(stack);
						SpoutItemStack stack2 = new SpoutItemStack(coinList.get(index - 1));
						stack.setAmount(coinList.get(index).getValue()/coinList.get(index - 1).getValue() - 64);
						outputSlot2.setItem(stack2);
					}
				}
				else
					return true;
		}
		return true;
	}

	@Override
	public boolean onItemTake(ItemStack item, ActionSlot source)
	{
		return true;
	}

	@Override
	public void onItemShiftClicked(ActionSlot source)
	{
		
	}
}
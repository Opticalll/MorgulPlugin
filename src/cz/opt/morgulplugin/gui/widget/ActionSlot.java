package cz.opt.morgulplugin.gui.widget;

import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.gui.GenericSlot;

import cz.opt.morgulplugin.listener.ActionSlotListener;

public class ActionSlot extends GenericSlot
{
	ActionSlotListener listener;
	
	public ActionSlot(ActionSlotListener listener)
	{
		super();
		this.listener = listener;
	}

	@Override
	public boolean onItemExchange(ItemStack current, ItemStack cursor)
	{
		return listener.onItemExchange(current, cursor, this);
	}

	@Override
	public boolean onItemPut(ItemStack item)
	{
		return listener.onItemPut(item, this);
	}

	@Override
	public void onItemShiftClicked()
	{
		listener.onItemShiftClicked(this);
	}

	@Override
	public boolean onItemTake(ItemStack item)
	{
		return listener.onItemTake(item, this);
	}
	
	
}

package cz.opt.morgulplugin.gui.widget;

import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.gui.GenericSlot;

import cz.opt.morgulplugin.listener.ActionSlotListener;

public class ActionSlot extends GenericSlot
{
	private ActionSlotListener listener;
	private boolean readOnly;
	
	public ActionSlot(int x, int y, ActionSlotListener listener)
	{
		super();
		setMargin(y, 0, 0, x);
		setX(x);
		setY(y);
		setFixed(true).setWidth(16).setHeight(16);
		this.listener = listener;
	}

	public void setReadOnly(boolean st)
	{
		readOnly = st;
	}
	
	public boolean isReadOnly()
	{
		return readOnly;
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

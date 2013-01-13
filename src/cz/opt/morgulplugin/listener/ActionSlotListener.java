package cz.opt.morgulplugin.listener;

import org.bukkit.inventory.ItemStack;

import cz.opt.morgulplugin.gui.widget.ActionSlot;

public interface ActionSlotListener
{
	public boolean onItemExchange(ItemStack current, ItemStack cursor, ActionSlot source);
	public boolean onItemPut(ItemStack item, ActionSlot source);
	public boolean onItemTake(ItemStack item, ActionSlot source);
	public void onItemShiftClicked(ActionSlot source);
}

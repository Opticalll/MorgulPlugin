package cz.opt.morgulplugin.gui.widget;

import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.gui.GenericSlot;
import org.getspout.spoutapi.gui.Slot;

public class InventorySlot extends GenericSlot
{
	private boolean readonly = false;
	private InventoryWidget gui;
	

	public InventorySlot(int xpos, int ypos, InventoryWidget gui) {
		this.gui = gui;

		setMargin(ypos, 0, 0, xpos);
		setX(xpos);
		setY(ypos);
		setFixed(true).setWidth(16).setHeight(16);
	}

	public boolean isReadOnly() {
		return readonly;
	}

	public InventorySlot setReadOnly(boolean readonly) {
		this.readonly = readonly;
		return this;
	}

	@Override
	public boolean onItemTake(ItemStack i) {
		return true;
	}

	@Override
	public boolean onItemPut(ItemStack i) {
		if(readonly) 
			return false;
		return true;
	}

	@Override
	public boolean onItemExchange(ItemStack i, ItemStack j) {
		if(readonly)
			return false;
		return true;
	}

	@Override
	public Slot setItem(ItemStack is) {
		return setItem(is, true);
	}

	public Slot setItem(ItemStack is, boolean update) {
		if(update) 
			gui.updateContents(this, is);
		return super.setItem(is);
	}
}

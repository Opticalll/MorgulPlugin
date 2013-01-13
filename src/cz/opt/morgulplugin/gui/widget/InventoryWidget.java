package cz.opt.morgulplugin.gui.widget;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.event.screen.ScreenCloseEvent;
import org.getspout.spoutapi.gui.Container;
import org.getspout.spoutapi.gui.ContainerType;
import org.getspout.spoutapi.gui.GenericContainer;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.PopupScreen;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.player.SpoutPlayer;

import cz.opt.morgulplugin.MorgulPlugin;
import cz.opt.morgulplugin.gui.widget.extend.InventoryViewExtention;

public class InventoryWidget extends InventoryView
{
	private Inventory inventory;
	private SpoutPlayer pl;
	private Container cont;
	
	private int width;
	private int height;
	
	private InventoryViewExtention extend;

	private List<InventorySlot> slots = new ArrayList<InventorySlot>();

	public InventoryWidget(SpoutPlayer p, int width, int height, InventoryViewExtention ext) 
	{
		this.width = width;
		this.height = height;
		this.extend = ext;
		pl = p;
		inventory = p.getInventory();
		cont = new GenericContainer();
		for(int i = 0; i < 36; i++) {
			Point loc = getSlotLocation(i);
			InventorySlot s = new InventorySlot((int) loc.getX(), (int) loc.getY(), this);
			s.setItem(inventory.getItem(i), false);
			addSlot(s);
		}
	}
	
	public boolean initGUI()
	{
		InventoryOpenEvent ioe = new InventoryOpenEvent(this);
		Bukkit.getPluginManager().callEvent(ioe);
		if(ioe.isCancelled()) 
			return false;
		cont.setLayout(ContainerType.OVERLAY).setAnchor(WidgetAnchor.CENTER_CENTER).setWidth(width).setHeight(height).setX(-width/2).setY(-height/2);
		cont.addChild(extend.getBackground().setPriority(RenderPriority.High));
		for(int i = 0; i < slots.size(); i++) 
			cont.addChild(slots.get(i));
		for(int z = 0; z < extend.getWidgets().size(); z++)
			cont.addChild(extend.getWidgets().get(z));
		pl.getMainScreen().attachPopupScreen((PopupScreen) new GenericPopup() 
		{
			@Override
			public void onScreenClose(ScreenCloseEvent e)
			{
				onClose(e);
			}
			

			@Override
			public void handleItemOnCursor(ItemStack is) {
				if(is.getTypeId() == 0 || is.getAmount() == 0) return;
				eject(is);
			}

			@Override
			public void onTick() {
				super.onTick();
				InventoryWidget.this.onTick();
			}
		}.attachWidget(MorgulPlugin.thisPlugin, cont));
		return true;
	}
	
	public void onClose(ScreenCloseEvent e)
	{
		extend.onScreenClose(e);
	}
	
	public void onTick()
	{
		
	}

	public void eject(ItemStack is)
	{
		Location l = pl.getLocation();
		l.getWorld().dropItem(l, is);
	}
	
	public void updateContents(InventorySlot slot, ItemStack contents) 
	{
		int id = slots.indexOf(slot);
		if(id >= 0) updateContents(id, contents);
	}
	
	public void updateContents(int id, ItemStack is) {
		if(id < 36) 
		{
			if(is == null) 
				inventory.clear(id);
			else 
				inventory.setItem(id, is);
		} 
		else 
		{
			//TODO call update content of different inventory.
		}
	}
	
	protected int addSlot(InventorySlot slot) 
	{
		if(slots.contains(slot)) 
			throw new IllegalArgumentException("You can't register a slot twice!");
		slots.add(slot);
		return slots.indexOf(slot) + 35;
	}
	
	private Point getSlotLocation(int index)
	{
		//calculate square in the inventory.
		int ix;
		int iy;
		ix = index%9;
		if(index<9) 
			iy=3;
		else 
			iy = ( (index-ix) / 9) - 1;

		//calculate exact coordinates in the inventory.
		int ax,ay;
		ax = 18*ix;
		if(iy!=3) 
			ay=18*iy;
		else 
			ay = 18*3 + 4;
		Point result = extend.getInventoryOffSet();
		result.translate(ax, ay);
		return result;
	}
	
	@Override
	public Inventory getBottomInventory()
	{
		return pl.getInventory();
	}

	@Override
	public HumanEntity getPlayer()
	{
		return pl;
	}

	@Override
	public Inventory getTopInventory()
	{
		return inventory;
	}

	@Override
	public InventoryType getType()
	{
		return InventoryType.CHEST;
	}

}

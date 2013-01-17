package cz.opt.morgulplugin.gui.popout;

import java.awt.Point;
import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.event.screen.ButtonClickEvent;
import org.getspout.spoutapi.event.screen.ScreenCloseEvent;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.Widget;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import org.getspout.spoutapi.player.SpoutPlayer;

import cz.opt.morgulplugin.MorgulPlugin;
import cz.opt.morgulplugin.config.Config;
import cz.opt.morgulplugin.gui.widget.ActionButton;
import cz.opt.morgulplugin.gui.widget.ActionSlot;
import cz.opt.morgulplugin.gui.widget.InventoryWidget;
import cz.opt.morgulplugin.gui.widget.Label;
import cz.opt.morgulplugin.gui.widget.extend.InventoryViewExtention;
import cz.opt.morgulplugin.item.Coin;
import cz.opt.morgulplugin.listener.ActionButtonListener;
import cz.opt.morgulplugin.listener.ActionSlotListener;
import cz.opt.morgulplugin.managers.CoinManager;

public class CoinExchange implements InventoryViewExtention, ActionSlotListener, ActionButtonListener
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
		Label label = new Label(Integer.parseInt(Config.get(CONF_FILE, SECTION, "Label_PopupName_X")), Integer.parseInt(Config.get(CONF_FILE, SECTION, "Label_PopupName_Y")), Config.get(CONF_FILE, SECTION, "Label_PopupName_Text"));
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
		ActionButton but = new ActionButton(Integer.parseInt(Config.get(CONF_FILE, SECTION, "Button_Exchange_X")), Integer.parseInt(Config.get(CONF_FILE, SECTION, "Button_Exchange_Y")));
		but.setText(Config.get(CONF_FILE, SECTION, "Button_Exchange_Text"));
		but.setWidth(Integer.parseInt(Config.get(CONF_FILE, SECTION, "Button_Exchange_Width")));
		but.setHeight(Integer.parseInt(Config.get(CONF_FILE, SECTION, "Button_Exchange_Height")));
		but.setFixed(true);
		but.addListener(this);
		widgets.add(but);
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

	@Override
	public void onButtonClicked(ButtonClickEvent ev)
	{
		SpoutItemStack stack1 = new SpoutItemStack(slots.get(0).getItem());
		SpoutItemStack stack2 = new SpoutItemStack(slots.get(1).getItem());
		Coin coin1 = CoinManager.getCoinByMaterial(stack1.getMaterial().getName());
		Coin coin2 = CoinManager.getCoinByMaterial(stack2.getMaterial().getName());
		
		SpoutItemStack workingStack;
		
		if(coin1 == null && coin2 == null)
			return;
		if(coin1 != null)
		{
			workingStack = stack1;
			if(coin1 == coin2)
				workingStack.setAmount(workingStack.getAmount() + stack2.getAmount());
		}
		else
		{
			workingStack = stack2;
		}
		
		int currentCoinIndex = CoinManager.coinList.indexOf(coin1 == null ? coin2 : coin1);
		int coinsToNextTier = -1;
		int coinsToLowerTier = -1;
		if(currentCoinIndex != CoinManager.coinList.size() - 1)
			coinsToNextTier = CoinManager.coinList.get(currentCoinIndex + 1).getValue()/(coin1 == null ? coin2.getValue() : coin1.getValue());
		if(currentCoinIndex >= 1)
			coinsToLowerTier = (coin1 == null ? coin2.getValue() : coin1.getValue())/CoinManager.coinList.get(currentCoinIndex - 1).getValue();
		if(coinsToLowerTier == -1 && coinsToNextTier == -1)
			return;
		int currentAmount = workingStack.getAmount();
		MorgulPlugin.debug("Coins to higher - " + coinsToNextTier + "| Coins to lower - " + coinsToLowerTier + "| Coin Index - " + currentCoinIndex + "| Coin amount - " + currentAmount);
		if(coinsToNextTier != -1 && coinsToNextTier <= currentAmount)
		{
			int outputCoins = workingStack.getAmount()/coinsToNextTier;
			workingStack.setAmount(workingStack.getAmount()%coinsToNextTier);
			slots.get(0).setItem(workingStack);
			slots.get(1).setItem(new ItemStack(Material.AIR));
			SpoutItemStack newCoins = new SpoutItemStack(CoinManager.coinList.get(currentCoinIndex + 1));
			newCoins.setAmount(outputCoins);
			slots.get(2).setItem(newCoins);
		}
		else if(coinsToLowerTier != -1 && (coinsToNextTier > currentAmount || coinsToNextTier == -1))
		{
			int outputCoins = CoinManager.coinList.get(currentCoinIndex).getValue()/CoinManager.coinList.get(currentCoinIndex - 1).getValue();
			workingStack.setAmount(workingStack.getAmount() - 1);
			SpoutItemStack secondInput = new SpoutItemStack(new ItemStack(Material.AIR));
			if(workingStack.getAmount() > 64)
			{
				secondInput = workingStack;
				secondInput.setAmount(workingStack.getAmount() - 64);
				workingStack.setAmount(64);
			}
			if(workingStack.getAmount() == 0)
				workingStack = new SpoutItemStack(new ItemStack(Material.AIR));

			
			SpoutItemStack newCoins = new SpoutItemStack(CoinManager.coinList.get(currentCoinIndex - 1));
			newCoins.setAmount(outputCoins);
			
			SpoutItemStack secondOutput = new SpoutItemStack(new ItemStack(Material.AIR));
			if(newCoins.getAmount() > 64)
			{
				secondOutput = newCoins;
				secondOutput.setAmount(newCoins.getAmount() - 64);
				newCoins.setAmount(64);
			}
			
			slots.get(0).setItem(workingStack);
			slots.get(1).setItem(secondInput);
			slots.get(2).setItem(newCoins);
			slots.get(3).setItem(secondOutput);
		}
		else 
			return;
	}
}
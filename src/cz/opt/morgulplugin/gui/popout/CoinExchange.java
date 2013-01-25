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
		invWidget = new InventoryWidget(pl, Config.get(CONF_FILE, SECTION, "CoinExchanger_Width", 176), Config.get(CONF_FILE, SECTION, "CoinExchanger_Height", 122), this);
		widgets = new ArrayList<Widget>();
		slots = new ArrayList<ActionSlot>();
		Label label = new Label(Config.get(CONF_FILE, SECTION, "Label_PopupName_X", 24), Config.get(CONF_FILE, SECTION, "Label_PopupName_Y", 14), Config.get(CONF_FILE, SECTION, "Label_PopupName_Text", "Coin Exchange"));
		widgets.add(label);
		ActionSlot slot1 = new ActionSlot(Config.get(CONF_FILE, SECTION, "Slot_Input1_X", 25), Config.get(CONF_FILE, SECTION, "Slot_Input1_Y", 18), this);
		addSlot(slot1);
		ActionSlot slot2 = new ActionSlot(Config.get(CONF_FILE, SECTION, "Slot_Input2_X", 43), Config.get(CONF_FILE, SECTION, "Slot_Input2_Y", 18), this);
		addSlot(slot2);
		ActionSlot slot3 = new ActionSlot(Config.get(CONF_FILE, SECTION, "Slot_Output1_X", 115), Config.get(CONF_FILE, SECTION, "Slot_Output1_Y", 17), this);
		slot3.setReadOnly(true);
		addSlot(slot3);
		ActionSlot slot4 = new ActionSlot(Config.get(CONF_FILE, SECTION, "Slot_Output2_X", 133), Config.get(CONF_FILE, SECTION, "Slot_Output2_Y", 17), this);
		slot4.setReadOnly(true);
		addSlot(slot4);
		ActionButton but = new ActionButton(Config.get(CONF_FILE, SECTION, "Button_Exchange_X", 75), Config.get(CONF_FILE, SECTION, "Button_Exchange_Y", 10));
		but.setText(Config.get(CONF_FILE, SECTION, "Button_Exchange_Text", "Exchanger"));
		but.setWidth(Config.get(CONF_FILE, SECTION, "Button_Exchange_Width", 34));
		but.setHeight(Config.get(CONF_FILE, SECTION, "Button_Exchange_Height", 10));
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
		return new GenericTexture("Morgul/textures/" + Config.get(CONF_FILE, SECTION, "CoinExchanger_BackGround", "coinExchanger.png"));
	}

	public static String getTextureName()
	{
		return "Morgul/textures/" + Config.get(CONF_FILE, SECTION, "CoinExchanger_BackGround", "coinExchanger.png");
	}
	
	@Override
	public ArrayList<Widget> getWidgets()
	{
		return widgets;
	}

	@Override
	public Point getInventoryOffSet()
	{
		return new Point(Config.get(CONF_FILE, SECTION, "Inventory_PlayerInventory_X", 8), Config.get(CONF_FILE, SECTION, "Inventory_PlayerInventory_Y", 40));
	}

	@Override
	public void onScreenClose(ScreenCloseEvent e)
	{
		for(int i = 0; i < slots.size(); i++)
		{
			ActionSlot slot = slots.get(i);
			if(slot.getItem().getType() != Material.AIR)
				invWidget.eject(slot.getItem());
		}
		MorgulPlugin.log("Exchanger Closed.");
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
			slots.get(1).setItem(new ItemStack(Material.AIR));
		}
		
		boolean ejected = false;
		
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
			if(workingStack.getAmount() == 0)
				slots.get(0).setItem(new ItemStack(Material.AIR));
			slots.get(0).setItem(workingStack);
			if(!new SpoutItemStack(slots.get(1).getItem()).getMaterial().getName().equalsIgnoreCase(workingStack.getMaterial().getName()))
				slots.get(1).setItem(slots.get(1).getItem());
			else
				slots.get(1).setItem(new ItemStack(Material.AIR));
			SpoutItemStack newCoins = new SpoutItemStack(CoinManager.coinList.get(currentCoinIndex + 1));
			newCoins.setAmount(outputCoins);
			if(!new SpoutItemStack(slots.get(2).getItem()).getMaterial().getName().equalsIgnoreCase(newCoins.getMaterial().getName()))
			{
				invWidget.eject(slots.get(2).getItem());
				ejected = true;
			}
			else
				newCoins.setAmount(slots.get(2).getItem().getAmount() + newCoins.getAmount());
			slots.get(2).setItem(newCoins);
		}
		else if(coinsToLowerTier != -1 && (coinsToNextTier > currentAmount || coinsToNextTier == -1))
		{
			int outputCoins = CoinManager.coinList.get(currentCoinIndex).getValue()/CoinManager.coinList.get(currentCoinIndex - 1).getValue();
			workingStack.setAmount(workingStack.getAmount() - 1);
			if(!new SpoutItemStack(slots.get(1).getItem()).getMaterial().getName().equalsIgnoreCase(workingStack.getMaterial().getName()))
				slots.get(1).setItem(slots.get(1).getItem());
			else
				slots.get(1).setItem(new ItemStack(Material.AIR));
			SpoutItemStack secondInput = new SpoutItemStack(slots.get(1).getItem());
			
			if(workingStack.getAmount() > 64)
			{
				secondInput = new SpoutItemStack(workingStack);
				secondInput.setAmount(workingStack.getAmount() - 64);
				workingStack.setAmount(64);
			}
			if(workingStack.getAmount() == 0)
				workingStack = new SpoutItemStack(new ItemStack(Material.AIR));

			
			SpoutItemStack newCoins = new SpoutItemStack(CoinManager.coinList.get(currentCoinIndex - 1));
			newCoins.setAmount(0);
			if(newCoins.getMaterial().getName().equalsIgnoreCase(new SpoutItemStack(slots.get(2).getItem()).getMaterial().getName()))
				newCoins.setAmount(slots.get(2).getItem().getAmount());
			else if(slots.get(2).getItem().getType() != Material.AIR)
			{
				invWidget.eject(slots.get(2).getItem());
				ejected = true;
				slots.get(2).setItem(new ItemStack(Material.AIR));
			}
			newCoins.setAmount(outputCoins + newCoins.getAmount());
			
			SpoutItemStack secondOutput = new SpoutItemStack(new ItemStack(Material.AIR));
			if(newCoins.getAmount() > 64)
			{
				secondOutput = new SpoutItemStack(newCoins);
				secondOutput.setAmount(0);
				if(new SpoutItemStack(slots.get(3).getItem()).getMaterial().getName().equalsIgnoreCase(secondOutput.getMaterial().getName()))
					secondOutput.setAmount(slots.get(3).getItem().getAmount());
				secondOutput.setAmount(newCoins.getAmount() - 64 + secondOutput.getAmount());
				newCoins.setAmount(64);
			}
			
			if(secondOutput.getAmount() > 64)
			{
				SpoutItemStack eject = new SpoutItemStack(secondOutput);
				eject.setAmount(secondOutput.getAmount() - 64);
				secondOutput.setAmount(64);
				invWidget.eject(eject);
				ejected = true;
			}
			
			
			
			slots.get(0).setItem(workingStack);
			slots.get(1).setItem(secondInput);
			slots.get(2).setItem(newCoins);
			slots.get(3).setItem(secondOutput);
		}
		else 
			return;
		if(ejected)
		{
			this.onScreenClose(new ScreenCloseEvent(invWidget.getPopup().getPlayer(), invWidget.getPopup().getScreen(), invWidget.getPopup().getScreenType()));
			this.invWidget.getPopup().close();
		}
	}
}
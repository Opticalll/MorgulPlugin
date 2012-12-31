package cz.opt.morgulplugin.runnable;

import cz.opt.morgulplugin.MorgulPlugin;
import cz.opt.morgulplugin.entity.MorgPlayer;
import cz.opt.morgulplugin.managers.ChatManager;

public class AcceptQuedMessages implements Runnable
{
 	private MorgPlayer pl;

	public AcceptQuedMessages(MorgPlayer pl)
	{
		this.pl = pl;
	}
	
	@Override
	public void run()
	{
		ChatManager.acceptQuedMsgs(pl);
		MorgulPlugin.debug("Player Accept Qued Msgs.");
	}

}

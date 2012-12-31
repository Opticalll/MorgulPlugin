package cz.opt.morgulplugin.managers;

import cz.opt.morgulplugin.database.DataBase;

public class PermissionManager
{
	static void addPermission(String playername, String permission)
	{
		PermissionManager.addPermission(PlayerManager.getPlayerId(playername), permission);
	}

	static void addPermission(int id, String permission)
	{
		DataBase.query("INSERT INTO permissions ('id', 'permission') VALUES ('" + id + "', '" + permission + "'");
	}
	
	static void removePermission(String playername, String permission)
	{
		PermissionManager.removePermission(PlayerManager.getPlayerId(playername), permission);
	}
	
	static void removePermission(int id, String permission)
	{
		DataBase.query("DELETE * FROM permissions WHERE id='" + id + "' AND permission='" + permission + "'");
	}

	static boolean hasPermission(String playername, String permission)
	{
		return PermissionManager.hasPermission(PlayerManager.getPlayerId(playername), permission);
	}
	
	static boolean hasPermission(int id, String permission)
	{
		if(DataBase.query("SELECT * FROM permissions WHERE id='" + id + "' AND permission='" + permission + "'").keySet().size() == 0) 
			return false;
		else 
			return true;
	}
}

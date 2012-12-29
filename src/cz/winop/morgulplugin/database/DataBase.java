package cz.winop.morgulplugin.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import cz.winop.morgulplugin.MorgulPlugin;
import cz.winop.morgulplugin.config.Config;

public class DataBase
{
	static final String SECTION = "DataBase";
	static Connection con;
	static String url;
	static String host;
	static String pass;
	static String user;
	static String port;
	static String database;
	
	public static boolean setUp()
	{
		DataBase.host = Config.get(SECTION, "host");
		DataBase.port = Config.get(SECTION, "port");
		DataBase.database = Config.get(SECTION, "database");
		DataBase.pass = Config.get(SECTION, "pass");
		DataBase.user = Config.get(SECTION, "user");
		url = "jdbc:mysql://" + host + ":" + port +"/"+ database;
		try
		{
			con = DriverManager.getConnection(url, user, pass);
		} catch (SQLException e) {
			MorgulPlugin.log("Database SQL Error: " + e.getMessage());
			return false;
		}
		return true;
	}
	
	public static ResultSet query(String sqlQuery)
	{
		Statement st = null;
		ResultSet rs = null;
		try 
		{
            st = con.createStatement();
            rs = st.executeQuery(sqlQuery);
        } catch (SQLException ex) {
			 MorgulPlugin.log("Database SQL Error: " + ex.getMessage());
			 return null;
        }
		return rs;
	}
	
	public static void update(String sqlQuery)
	{
		Statement st = null;
		try 
		{
            st = con.createStatement();
            st.executeUpdate(sqlQuery);
        } catch (SQLException ex) {
			 MorgulPlugin.log("Database SQL Error: " + ex.getMessage());
			 MorgulPlugin.log(sqlQuery);
        } finally {
        	try
			{
				st.close();
			} catch (SQLException ex) {
				MorgulPlugin.log("Database SQL Error: " + ex.getMessage());
			}
        }
	}
	
	public static int getRowCount(ResultSet set)
	{
		int rowCount;
		try 
		{
			int currentRow = set.getRow();    
			rowCount = set.last() ? set.getRow() : 0;
			if (currentRow == 0)                      
				set.beforeFirst();                     
			else                                      
				set.absolute(currentRow);
		} catch (SQLException ex) {
			MorgulPlugin.log("Database SQL Error: " + ex.getMessage());
			return 0;
		}             
		return rowCount;
	}
}

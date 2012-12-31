package cz.opt.morgulplugin.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import cz.opt.morgulplugin.MorgulPlugin;
import cz.opt.morgulplugin.config.Config;

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
	
	public static HashMap<Integer, HashMap<String, String>> query(String sqlQuery)
	{
		Statement st = null;
		ResultSet rs = null;
		HashMap<Integer, HashMap<String, String>> rows = new HashMap<Integer, HashMap<String, String>>();
		try 
		{
            st = con.createStatement();
            rs = st.executeQuery(sqlQuery);
            while(rs.next())
            {
            	HashMap<String, String> colums = new HashMap<String, String>();
            	for(int i = 1; i < rs.getMetaData().getColumnCount() + 1; i++)
            		colums.put(rs.getMetaData().getColumnName(i), rs.getString(i));
            	rows.put(rs.getRow(), colums);
            }
            rs.close();
            st.close();
        } catch (SQLException ex) {
			 MorgulPlugin.log("Database SQL Error: " + ex.getMessage());
			 return null;
        }
		return rows;
	}
	
	public static void update(String sqlQuery)
	{
		Statement st = null;
		try 
		{
            st = con.createStatement();
            st.executeUpdate(sqlQuery);
            st.close();
        } catch (SQLException ex) {
			 MorgulPlugin.log("Database SQL Error: " + ex.getMessage());
			 MorgulPlugin.log(sqlQuery);
        }
	}
}

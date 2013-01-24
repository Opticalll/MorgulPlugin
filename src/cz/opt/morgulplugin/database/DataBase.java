package cz.opt.morgulplugin.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
	static final String SQL_FILE = "/sql/tables.sql";
	static final String CONF_FILE = "system.conf";
	static final String SECTION = "DataBase";
	static Connection con;
	static String url;
	static String host;
	static String pass;
	static String user;
	static int port;
	static String database;
	//1st commit
	
	public static boolean setUp()
	{
		DataBase.host = Config.get(CONF_FILE, SECTION, "host", "localhost");
		DataBase.port = Config.get(CONF_FILE, SECTION, "port", 3306);
		DataBase.database = Config.get(CONF_FILE, SECTION, "database", "morgul");
		DataBase.pass = Config.get(CONF_FILE, SECTION, "pass", "password");
		DataBase.user = Config.get(CONF_FILE, SECTION, "user", "morgulUser");
		url = "jdbc:mysql://" + host + ":" + port +"/"+ database;
		try
		{
			con = DriverManager.getConnection(url, user, pass);
			setUpTables();
		} catch (SQLException e) {
			MorgulPlugin.log("Database SQL Error: " + e.getMessage());
			return false;
		} catch (IOException e)
		{
			MorgulPlugin.log("SQL File setup Error: " + e.getMessage());
		}
		return true;
	}
	
	private static void setUpTables() throws IOException
	{
		String sql;
		StringBuffer sb = new StringBuffer();
		BufferedReader br = new BufferedReader(new InputStreamReader(DataBase.class.getResourceAsStream(SQL_FILE)));
		while((sql = br.readLine()) != null)
			sb.append(sql);
		br.close();
		
		String[] statements = sb.toString().split(";");
		
		for(int i = 0; i < statements.length; i++)
		{
			
			if(!statements[i].trim().equals(""))
			{
				DataBase.update(statements[i]);
			}
		}
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

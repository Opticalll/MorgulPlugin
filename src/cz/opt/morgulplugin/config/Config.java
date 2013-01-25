package cz.opt.morgulplugin.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import cz.opt.morgulplugin.MorgulPlugin;

public class Config
{
	static final String CONFIG_FOLDER = "Morgul/config/";

	static ArrayList<String> fileList;
	static HashMap<String, HashMap<String, HashMap<String, String>>> configMap = null;
	
	public static boolean init()
	{
		fileList = new ArrayList<String>();
		fileList.add("system.conf");
		fileList.add("chat.conf");
		fileList.add("stats.conf");
		fileList.add("player.conf");
		fileList.add("coins.conf");
		fileList.add("gui.conf");
		fileList.add("hunt.conf");
		fileList.add("lang.conf");
		configMap = new HashMap<String, HashMap<String, HashMap<String, String>>>();
		createConfigs();
		if(!loadConfig())
			return false;
		return true;
	}
	
	private static void createConfigs()
	{
		for(int z = 0; z < fileList.size(); z++)
		{
			String filePath = CONFIG_FOLDER + fileList.get(z);
			File conf = new File(filePath);
			if(!conf.exists())
			{
				try
				{
					String[] folders = filePath.split("/");
					for(int i = 0; i < folders.length - 1; i++)
					{
						String folderpath = "";
						for(int g = 0; g < i; g++)
							folderpath += folders[g] + "/";
						File folder = new File(folderpath + folders[i]);
						if(folder.exists())
							continue;
						else
							if(!folder.mkdir())
								MorgulPlugin.log("Folder " + folders[i] + " could not be Created");
					}
					if(conf.createNewFile())
					{
						MorgulPlugin.log("Config " + fileList.get(z) + " Created.");
						FileWriter fstream = new FileWriter(conf);
						BufferedWriter bufOut = new BufferedWriter(fstream);

						Scanner confScan = new Scanner(Config.class.getResourceAsStream("/configs/" + fileList.get(z)));
						while(confScan.hasNextLine())
							bufOut.write(confScan.nextLine() + "\n");

						confScan.close();
						bufOut.close();
					}
					else
						MorgulPlugin.log("Config File could not be created.");
				} catch (IOException e) {
					MorgulPlugin.log(e.getMessage());
					return;
				}
			}
		}
	}

	public static boolean loadConfig()
	{
		File dir = new File(CONFIG_FOLDER);
		File[] files = dir.listFiles(new FilenameFilter() { public boolean accept(File dir, String filename) { return filename.endsWith(".conf"); } } );

		for(int g = 0; g < files.length; g++)
		{
			Scanner scan;
			try
			{
				scan = new Scanner(files[g]);
			} catch (FileNotFoundException e)
			{
				MorgulPlugin.log(e.getMessage());
				return false;
			}

			String input = "";
			while(scan.hasNextLine())
			{
				String tmp = scan.nextLine();
				if(tmp.startsWith("#"))
					continue;
				input += tmp + "\n";
			}

			scan.close();

			if(input == "")
			{
				MorgulPlugin.log("Config file Error");
				return false;
			}
			input += "[";
			input = input.substring(input.indexOf("[") + 1);
			List<String> inputChunks = new ArrayList<String>();
			while(input.contains("["))
			{
				inputChunks.add(input.substring(0, input.indexOf("[")));
				input = input.substring(input.indexOf("[") + 1);
			}

			List<String> strings = inputChunks;
			HashMap<String, HashMap<String, String>> mapList = new HashMap<String, HashMap<String, String>>();
			for(int i = 0; i < strings.size(); i++)
			{
				String[] lines = strings.get(i).split("\n");
				String mapName = null;
				HashMap<String, String> variableMap = new HashMap<String, String>();
				for(int z = 0; z < lines.length; z++)
				{
					if(lines[z] != "")
					{
						if(z == 0)
						{
							if(lines[z].contains("]"))
								lines[z] = lines[z].substring(0, lines[z].lastIndexOf("]"));
							mapName = lines[z]; 
						}
						else
						{
							String[] record = lines[z].split("=");
							if(record.length >= 2)
								variableMap.put(record[0], record[1]);
							else
								variableMap.put(record[0], "");
						}
					}

				}
				mapList.put(mapName, variableMap);
			}
			configMap.put(files[g].getName(), mapList);
		}
		return true;
	}

	public static String get(String file, String section, String key, String def)
	{
		HashMap<String, HashMap<String, String>> fileMap = configMap.get(file);
		if(fileMap == null)
		{
			MorgulPlugin.log("[Warning]: Returning def Value [File]=" + file + " [Section]=" + section + " [Key]=" + key + " [Default_Value]=" + def);
			return def;
		}
		HashMap<String, String> tileMap = fileMap.get(section); 
		if(tileMap == null)
		{
			MorgulPlugin.log("[Warning]: Returning def Value [File]=" + file + " [Section]=" + section + " [Key]=" + key + " [Default_Value]=" + def);
			return def;
		}
		String out = tileMap.get(key);
		if(out == null)
		{
			MorgulPlugin.log("[Warning]: Returning def Value [File]=" + file + " [Section]=" + section + " [Key]=" + key + " [Default_Value]=" + def);
			return def;
		}
		else
			return out;
	}
	
	public static int get(String file, String section, String key, int def)
	{
		HashMap<String, HashMap<String, String>> fileMap = configMap.get(file);
		if(fileMap == null)
		{
			MorgulPlugin.log("[Warning]: Returning def Value [File]=" + file + " [Section]=" + section + " [Key]=" + key + " [Default_Value]=" + def);
			return def;
		}
		HashMap<String, String> tileMap = fileMap.get(section); 
		if(tileMap == null)
		{
			MorgulPlugin.log("[Warning]: Returning def Value [File]=" + file + " [Section]=" + section + " [Key]=" + key + " [Default_Value]=" + def);
			return def;
		}
		String out = tileMap.get(key);
		if(out == null)
		{
			MorgulPlugin.log("[Warning]: Returning def Value [File]=" + file + " [Section]=" + section + " [Key]=" + key + " [Default_Value]=" + def);
			return def;
		}
		else
		{
			try
			{
				return Integer.parseInt(out);
			} catch(NumberFormatException e) {
				MorgulPlugin.log("[Warning]: Returning def Value [File]=" + file + " [Section]=" + section + " [Key]=" + key + " [Default_Value]=" + def);
				return def;
			}
		}
	}
	
	public static float get(String file, String section, String key, float def)
	{
		HashMap<String, HashMap<String, String>> fileMap = configMap.get(file);
		if(fileMap == null)
		{
			MorgulPlugin.log("[Warning]: Returning def Value [File]=" + file + " [Section]=" + section + " [Key]=" + key + " [Default_Value]=" + def);
			return def;
		}
		HashMap<String, String> tileMap = fileMap.get(section); 
		if(tileMap == null)
		{
			MorgulPlugin.log("[Warning]: Returning def Value [File]=" + file + " [Section]=" + section + " [Key]=" + key + " [Default_Value]=" + def);
			return def;
		}
		String out = tileMap.get(key);
		if(out == null)
		{
			MorgulPlugin.log("[Warning]: Returning def Value [File]=" + file + " [Section]=" + section + " [Key]=" + key + " [Default_Value]=" + def);
			return def;
		}
		else
		{
			try
			{
				return Float.parseFloat(out);
			} catch(NumberFormatException e) {
				MorgulPlugin.log("[Warning]: Returning def Value [File]=" + file + " [Section]=" + section + " [Key]=" + key + " [Default_Value]=" + def);
				return def;
			}
		}
	}
	
	public static double get(String file, String section, String key, double def)
	{
		HashMap<String, HashMap<String, String>> fileMap = configMap.get(file);
		if(fileMap == null)
		{
			MorgulPlugin.log("[Warning]: Returning def Value [File]=" + file + " [Section]=" + section + " [Key]=" + key + " [Default_Value]=" + def);
			return def;
		}
		HashMap<String, String> tileMap = fileMap.get(section); 
		if(tileMap == null)
		{
			MorgulPlugin.log("[Warning]: Returning def Value [File]=" + file + " [Section]=" + section + " [Key]=" + key + " [Default_Value]=" + def);
			return def;
		}
		String out = tileMap.get(key);
		if(out == null)
		{
			MorgulPlugin.log("[Warning]: Returning def Value [File]=" + file + " [Section]=" + section + " [Key]=" + key + " [Default_Value]=" + def);
			return def;
		}
		else
		{
			try
			{
				return Double.parseDouble(out);
			} catch(NumberFormatException e) {
				MorgulPlugin.log("[Warning]: Returning def Value [File]=" + file + " [Section]=" + section + " [Key]=" + key + " [Default_Value]=" + def);
				return def;
			}
		}
	}
	
	public static boolean get(String file, String section, String key, boolean def)
	{
		HashMap<String, HashMap<String, String>> fileMap = configMap.get(file);
		if(fileMap == null)
		{
			MorgulPlugin.log("[Warning]: Returning def Value [File]=" + file + " [Section]=" + section + " [Key]=" + key + " [Default_Value]=" + def);
			return def;
		}
		HashMap<String, String> tileMap = fileMap.get(section); 
		if(tileMap == null)
		{
			MorgulPlugin.log("[Warning]: Returning def Value [File]=" + file + " [Section]=" + section + " [Key]=" + key + " [Default_Value]=" + def);
			return def;
		}
		String out = tileMap.get(key);
		if(out == null)
		{
			MorgulPlugin.log("[Warning]: Returning def Value [File]=" + file + " [Section]=" + section + " [Key]=" + key + " [Default_Value]=" + def);
			return def;
		}
		else
		{
			try
			{
				return Boolean.parseBoolean(out);
			} catch(NumberFormatException e) {
				MorgulPlugin.log("[Warning]: Returning def Value [File]=" + file + " [Section]=" + section + " [Key]=" + key + " [Default_Value]=" + def);
				return def;
			}
		}
	}
	
	public static HashMap<String, HashMap<String, String>> getFileMap(String file)
	{
		return configMap.get(file);
	}
}

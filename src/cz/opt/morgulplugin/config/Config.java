package cz.opt.morgulplugin.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import cz.opt.morgulplugin.MorgulPlugin;

public class Config
{
	static final String CONFIG_FILE = "Morgul/config/morgulConfig.conf";

	static HashMap<String, HashMap<String, String>> configMap = null;

	public static boolean loadConfig()
	{
		File conf = new File(CONFIG_FILE);
		if(!conf.exists())
		{
			try
			{
				String[] folders = CONFIG_FILE.split("/");
				for(int i = 0; i < folders.length; i++)
				{
					File folder = new File(folders[i]);
					if(folder.exists())
						continue;
					else
						if(!folder.mkdir())
							MorgulPlugin.log("Folder " + folders[i] + " could not be Created");
				}
				if(conf.createNewFile())
				{
					MorgulPlugin.log("Config File Created setup pls MorgulPlugin");
					//TODO: Write into file Basic configuration.
				}
				else
					MorgulPlugin.log("Config File could not be created.");
				return false;
			} catch (IOException e) {
				MorgulPlugin.log(e.getMessage());
				return false;
			}
		}	

		Scanner scan;
		try
		{
			scan = new Scanner(conf);
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
				tmp = "";
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
		configMap = mapList;
		return true;
	}

	public static String get(String section, String key)
	{
		HashMap<String, String> tile = configMap.get(section);
		return (String) tile.get(key);
	}
}

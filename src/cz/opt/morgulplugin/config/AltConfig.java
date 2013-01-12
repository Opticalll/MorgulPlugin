package cz.opt.morgulplugin.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import cz.opt.morgulplugin.MorgulPlugin;

// Vole nerikej mi ze takhle to neni daleko prehlednejsi a jednodussi.
// U toho tvyho musis stejne do ty funkce get() posilat jmeno configu, takze to je jedno.
// Proste lepsi! :D
public class AltConfig
{
	private final String CONFIG_FILE;
	private static final String CONFIG_FOLDER = "Morgul/config/";
	HashMap<String, HashMap<String, String>> configMap = null;

	public static final AltConfig system = new AltConfig("system.conf");
	public static final AltConfig chat = new AltConfig("chat.conf");
	public static final AltConfig stats = new AltConfig("stats.conf");
	public static final AltConfig player = new AltConfig("player.conf");
	public static final AltConfig coins = new AltConfig("coins.conf");
	public static final AltConfig gui = new AltConfig("gui.conf");
	
	private AltConfig(String path)
	{
		CONFIG_FILE = CONFIG_FOLDER + path;
		loadConfig();
	}
	
	public boolean loadConfig()
	{
		File conf = new File(CONFIG_FILE);
		if(!conf.exists())
		{
			try
			{
				String[] folders = CONFIG_FILE.split("/");
				for(int i = 0; i < folders.length - 1; i++)
				{
					String folderpath = null;
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
					MorgulPlugin.log("Config File Created setup pls MorgulPlugin");
					FileWriter fstream = new FileWriter(conf);
					BufferedWriter bufOut = new BufferedWriter(fstream);

					Scanner confScan = new Scanner(Config.class.getResourceAsStream("/morgulConfig.yml"));
					while(confScan.hasNextLine())
						bufOut.write(confScan.nextLine());

					confScan.close();
					bufOut.close();
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
		configMap = mapList;
		return true;
	}

	public String get(String section, String key)
	{
		HashMap<String, String> tile = configMap.get(section);
		return (String) tile.get(key);
	}
}

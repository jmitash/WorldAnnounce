package com.knoxcorner.worldannounce.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import com.knoxcorner.worldannounce.WorldAnnounce;



public class ConfigManager
{
	private WorldAnnounce wa;
	private HashMap<String, Integer> intervals;
	
	public ConfigManager(WorldAnnounce wa)
	{
		this.wa = wa;
		intervals = new HashMap<String, Integer>();
	}
	
	public void load()
	{
		File configFile = new File(wa.getDataFolder(), "config.yml");
		if(!configFile.exists())
		{
			wa.saveResource("config.yml", false);
		}
		
		try
		{
			wa.getConfig().load(configFile);
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (InvalidConfigurationException e)
		{
			e.printStackTrace();
		}
		FileConfiguration fc = wa.getConfig();
		List<String> lines = fc.getStringList("worlds");
		for(String line : lines)
		{
			String parts[] = line.split(" ");
			if(parts.length < 2)
			{
				wa.getLogger().warning("\"" + line + "\": No interval defined; messages will not be broadcast in this world");
				continue;
			}
			int interval = -1;
			try
			{
				interval = Integer.parseInt(parts[1]);
			} catch (NumberFormatException nfe)
			{
				wa.getLogger().warning('"' + parts[1] + "\": Not a valid integer");
				wa.getLogger().warning("\"" + line + "\": Interval not properly defined; messages will not be broadcast in this world");
				continue;
			}
			
			this.intervals.put(parts[0], interval);
		}
	}
	
	public HashMap<String, Integer> getIntervals()
	{
		return this.intervals;
	}
	

}

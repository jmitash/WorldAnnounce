package com.knoxcorner.worldannounce.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.bukkit.ChatColor;

import com.knoxcorner.worldannounce.WorldAnnounce;

public class AnnouncementsManager
{
	private WorldAnnounce wa;
	
	private File worldFolder;
	
	private HashMap<String, List<String>> worldList;
	
	public AnnouncementsManager(WorldAnnounce wa)
	{
		this.wa = wa;
		
		worldList = new HashMap<String, List<String>>();
		
		worldFolder = new File(wa.getDataFolder(), "Worlds");
		if(!worldFolder.exists())
		{
			worldFolder.mkdirs();

			File exampleFile = new File(worldFolder, "world_example.txt");
			try
			{
				exampleFile.createNewFile();
			} catch (IOException e)
			{
				wa.getLogger().warning("Failed to create example file.");
				e.printStackTrace();
				return;
			}
			
			try
			{
				BufferedWriter out = new BufferedWriter(new FileWriter(exampleFile));
				out.write("&cThis is an example message.");
				out.newLine();
				out.write("&cMake sure the file name is the name of the world.");
				out.newLine();
				out.write("&cConfigure how often these are broadcast in the config.yml");
				out.newLine();
				out.close();
			} catch (IOException e)
			{
				wa.getLogger().warning("Error writing to example file.");
				e.printStackTrace();
				return;
			}
		}
		
	}
	
	public void load(List<String> worldsTemp)
	{
		List<File> worlds = new ArrayList<File>(worldsTemp.size());
		
		for(String world : worldsTemp)
		{
			File f = new File(worldFolder, world + ".txt");
			if(!f.exists())
			{
				try
				{
				f.createNewFile();
				} catch (IOException e)
				{
					wa.getLogger().severe("Could not create " + f.getName());
					e.printStackTrace();
					continue;
				}
			}
			else
				worlds.add(f);
		}
		
		for(File world : worlds)
		{
			List<String> lines = new ArrayList<String>();
			
			try
			{
				Scanner sc = new Scanner(world);
				while(sc.hasNextLine())
				{
					lines.add(sc.nextLine().replaceAll("&", Character.toString(ChatColor.COLOR_CHAR)));
				}
				sc.close();
			} catch (FileNotFoundException e)
			{
				wa.getLogger().severe("Could not read from " + world.getName());
				e.printStackTrace();
				continue;
			}
			
			worldList.put(world.getName().replaceFirst(".txt", ""), lines);
		}
	}
	
	public HashMap<String, List<String>> getMessages()
	{
		return this.worldList;
	}

}

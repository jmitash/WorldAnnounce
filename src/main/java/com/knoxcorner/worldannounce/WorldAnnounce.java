package com.knoxcorner.worldannounce;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.knoxcorner.worldannounce.io.AnnouncementsManager;
import com.knoxcorner.worldannounce.io.ConfigManager;

public class WorldAnnounce extends JavaPlugin
{
	

	private ConfigManager configManager;
	private AnnouncementsManager ancMgr;
	
	@Override
	public void onEnable()
	{
		configManager = new ConfigManager(this);
		configManager.load();
		
		ancMgr = new AnnouncementsManager(this);		
		
		List<String> worlds = new ArrayList<String>();
		
		Iterator it = configManager.getIntervals().entrySet().iterator();
	    while (it.hasNext())
	    {
	    	Entry<String, Integer> entry = (Entry<String, Integer>) it.next();
	    	worlds.add(entry.getKey());
	    }
	    
	    ancMgr.load(worlds);
	    
	    worlds.clear(); //Clear, so we only get worlds that loaded properly
	    
	    it = ancMgr.getMessages().entrySet().iterator();
	    while (it.hasNext())
	    {
	    	Entry<String, List<String>> entry = (Entry<String, List<String>>) it.next();
	    	worlds.add(entry.getKey());
	    }
	    
	    for(String worldStr : worlds)
	    {
	    	if(worldStr.equalsIgnoreCase("world_example") || worldStr.equalsIgnoreCase("world_example_nether"))
	    		continue;
	    	World world = this.getServer().getWorld(worldStr);
	    	if(world == null && !worldStr.equalsIgnoreCase("world_example") && !worldStr.equalsIgnoreCase("world_example_nether"))
	    	{
	    		getLogger().warning(worldStr + " isn't a world! Will be skipped.");
	    	}
	    	Messenger msg = new Messenger(world, ancMgr.getMessages().get(worldStr));
	    	this.getServer().getScheduler().scheduleSyncRepeatingTask(this,
	    			msg,
	    			configManager.getIntervals().get(worldStr) * 20,
	    			configManager.getIntervals().get(worldStr) * 20);
	    }  
	}
	
	@Override
	public void onDisable()
	{
		this.getServer().getScheduler().cancelTasks(this);
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args)
	{
		if(cmd.getName().equalsIgnoreCase("wa"))
		{
			if(args.length == 0)
			{
				s.sendMessage(ChatColor.YELLOW + "/wa reload" + ChatColor.GOLD + ": Reload all files");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("reload"))
			{
				onDisable();
				onEnable();
				s.sendMessage(ChatColor.YELLOW + "WorldAnnounce reloaded!");
				return true;
			}
		}
		return true;
	}
}

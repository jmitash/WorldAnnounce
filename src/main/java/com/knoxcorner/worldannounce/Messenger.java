package com.knoxcorner.worldannounce;

import java.util.List;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Messenger extends BukkitRunnable
{
	
	private World world;
	private List<String> messages;
	private int msgNum;
	
	public Messenger(World world, List<String> messages)
	{
		this.world = world;
		this.messages = messages;
		msgNum = 0;
	}

	public void run()
	{
		if(messages.isEmpty())
			return;
		for(Player pl : world.getPlayers())
		{
			pl.sendMessage(messages.get(msgNum));
		}
		
		if(++msgNum == messages.size())
			msgNum = 0;
	}

}

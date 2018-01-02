package me.pro.dmg.PrevailPots.Core;

import org.bukkit.command.CommandSender;

public abstract class AdminSubCommand {
	
	public abstract void onCommand(CommandSender sender, String[] args);
	
	public abstract String name();
	
	public abstract String info();
	
	

}

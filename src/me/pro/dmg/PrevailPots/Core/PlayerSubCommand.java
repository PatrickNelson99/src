package me.pro.dmg.PrevailPots.Core;

import org.bukkit.entity.Player;

public abstract class PlayerSubCommand {
	
	public abstract void onCommand(Player p, String[] args);
	
	public abstract String name();
	
	public abstract String info();
	
	

}

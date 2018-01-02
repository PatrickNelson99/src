package me.pro.dmg.PrevailPots.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SpawnSign implements Listener {
	
	@EventHandler
	public void onChange(SignChangeEvent e) {
		
		if(e.getPlayer().isOp()) {
			
			if(e.getLine(0).equals("SpawnTP")) {
				
				e.setLine(0, ChatColor.GREEN + "Right Click");
				e.setLine(1, "to return to");
				e.setLine(2, "spawn");
				
				
				
			}
			
			
			
		}
		
		
		
	}

}

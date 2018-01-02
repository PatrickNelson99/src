package me.pro.dmg.PrevailPots.Matchmaking;

import org.bukkit.inventory.ItemStack;

public class GameType {
	
	private String name;
	private ItemStack[] inventoryContents;
	private ItemStack[] armourContents;
	
	
	public GameType(String name, ItemStack[] inventoryContents, ItemStack[] armourContents) {
		
		this.name = name;
		this.inventoryContents = inventoryContents;
		this.armourContents = armourContents;
		
	}
	
	public String getName() {
		return name;
	}
	
	public ItemStack[] getInventoryContents() {
		return inventoryContents;
	}
	
	public ItemStack[] getArmourContents() {
		return armourContents;
	}
	
	

}

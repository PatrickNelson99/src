package me.pro.dmg.PrevailPots.Kits;

import org.bukkit.inventory.ItemStack;

public class Kit {

	private String name;
	private ItemStack[] inventoryContents;
	private ItemStack[] armourContents;

	public Kit(String name, ItemStack[] inventoryContents, ItemStack[] armourContents) {

		this.name = name;
		this.inventoryContents = inventoryContents;
		this.armourContents = armourContents;

	}

	public String getName() {
		return this.name;

	}

	public ItemStack[] getInventoryContents() {

		return this.inventoryContents;
	}

	public ItemStack[] getArmourContents() {

		return this.armourContents;
	}

}

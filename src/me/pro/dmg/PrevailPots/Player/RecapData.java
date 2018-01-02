package me.pro.dmg.PrevailPots.Player;

import java.util.ArrayList;


import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;

public class RecapData {

	private String name;
	private ItemStack[] inventoryContents;
	private ItemStack[] armourContents;
	private ArrayList<PotionEffect> potionEffects;
	private int health;
	private int food;

	public RecapData(String name, ItemStack[] inventoryContents, ItemStack[] armourContents,
			ArrayList<PotionEffect> potionEffects, double health, int food) {

		this.name = name;
		this.inventoryContents = inventoryContents;
		this.armourContents = armourContents;
		this.potionEffects = potionEffects;
		this.health = (int) health;
		this.food = food;

	}

	public String getName() {
		return name;
	}

	public ItemStack[] getInventory() {
		return inventoryContents;
	}

	public ItemStack[] getArmour() {
		return armourContents;
	}

	public ItemStack getPotions() {

		ItemStack potion = new ItemStack(Material.POTION, 1);
		ItemMeta potionMeta = potion.getItemMeta();
		potionMeta.setDisplayName(ChatColor.GREEN + "Active Effects");

		ArrayList<String> lore = new ArrayList<String>();
		lore.add("");

		if (potionEffects.isEmpty()) {
			potionMeta.setDisplayName(ChatColor.RED + "No Active Effects");
		}

		else {

			for (PotionEffect effect : potionEffects) {

				String type = ChatColor.GOLD + effect.getType().getName().replace("_", " ");
				int amp = effect.getAmplifier();
				double durationMod = effect.getType().getDurationModifier();

				lore.add(ChatColor.DARK_GRAY + "-=-=-=-=-=-=-=-=-=-");
				lore.add(ChatColor.GREEN + "Type: " + ChatColor.GOLD + type);
				lore.add(ChatColor.GREEN + "Amplifier: " + ChatColor.RED + amp);
				lore.add(ChatColor.GREEN + "Duration Mod: " + ChatColor.RED + durationMod);

			}

		}
		potionMeta.setLore(lore);
		potion.setItemMeta(potionMeta);
		return potion;

	}

	public ItemStack getHealth() {

		ItemStack skullItem = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta meta = (SkullMeta) skullItem.getItemMeta();
		meta.setOwner(name);

		if (health == 0) {
			meta.setDisplayName(ChatColor.RED + "Player Dead");
		}

		else {
			meta.setDisplayName(ChatColor.GOLD + "Health: " + health);
		}

		skullItem.setItemMeta(meta);
		return skullItem;

	}

	public ItemStack getFood() {

		ItemStack foodItem = new ItemStack(Material.COOKED_BEEF, food);
		ItemMeta foodMeta = foodItem.getItemMeta();
		foodMeta.setDisplayName(ChatColor.GREEN + "Food Level");
		foodItem.setItemMeta(foodMeta);
		return foodItem;
	}
}

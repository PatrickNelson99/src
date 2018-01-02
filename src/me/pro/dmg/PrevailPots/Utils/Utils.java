package me.pro.dmg.PrevailPots.Utils;

import org.bukkit.inventory.ItemStack;

public class Utils {

	public static boolean hasDisplayName(ItemStack is, String s) {

		if (is != null) {
			if (is.hasItemMeta()) {
				if (is.getItemMeta().hasDisplayName()) {
					if (is.getItemMeta().getDisplayName().equals(s)) {
						return true;
					}
				}
			}
		}

		return false;
	}



}

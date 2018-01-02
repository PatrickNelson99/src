package me.pro.dmg.PrevailPots.Core;

import org.bukkit.entity.Player;

import me.pro.dmg.PrevailPots.Player.Dueler;

public class PrevailPotsAPI {
	
	static Cache cache = Cache.getInstance();
	
	public static Dueler getDueler(Player name) {
		Dueler dapi = cache.getDueler(name.getPlayer());
				return dapi;
	}
	
	public static String getRank(Player name) {
		Dueler dapi = cache.getDueler(name.getPlayer());
		String rank = dapi.getColouredRank();
				return rank;
	}
	
	public static String getTier(Player name) {
		Dueler dapi = cache.getDueler(name.getPlayer());
		String tier = dapi.getNumeral();
				return tier;
	}
}

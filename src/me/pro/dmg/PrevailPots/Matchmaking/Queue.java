package me.pro.dmg.PrevailPots.Matchmaking;

import java.util.HashSet;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pro.dmg.PrevailPots.Core.Cache;
import me.pro.dmg.PrevailPots.Player.Dueler;

public class Queue {

	Cache cache = Cache.getInstance();

	private GameType gameType;
	private HashSet<Dueler> duelers;

	public Queue(GameType gameType, HashSet<Dueler> duelers) {

		this.gameType = gameType;
		this.duelers = duelers;

	}

	public GameType getType() {

		return gameType;
	}

	public HashSet<Dueler> getDuelers() {

		return duelers;
	}

	public void removeFromQueue(Player p) {

		duelers.remove(cache.getDueler(p));

	}

	public void addDueler(Player p) {
		duelers.add(cache.getDueler(p));
		p.getInventory().clear();
	

		ItemStack is = new ItemStack(Material.REDSTONE, 1);
		ItemMeta meta = is.getItemMeta();

		meta.setDisplayName(ChatColor.RED + "Leave Queue");
		is.setItemMeta(meta);
		p.getInventory().setItem(0, is);
		p.updateInventory();

	}

}

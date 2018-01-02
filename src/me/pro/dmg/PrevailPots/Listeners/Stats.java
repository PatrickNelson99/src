package me.pro.dmg.PrevailPots.Listeners;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import me.pro.dmg.PrevailPots.Core.Cache;
import me.pro.dmg.PrevailPots.Core.Main;
import me.pro.dmg.PrevailPots.Player.Dueler;
import me.pro.dmg.PrevailPots.Teams.Team;
import me.pro.dmg.PrevailPots.Utils.Utils;

public class Stats implements Listener {

	Cache cache = Cache.getInstance();

	public void openStatInventory(Player p) {

		Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, ChatColor.BOLD + "Stats");
		Dueler d = cache.getDueler(p);

		int wins = d.getWins();
		int losses = d.getLosses();
		String rank = d.getRank();
		int tier = d.getTier();
		int points = d.getPoints();
		int elo = d.getElo();
		boolean isInPromos = d.promosEnabled();

		ItemStack info = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
		ItemStack ranked = new ItemStack(Material.DIAMOND_SWORD, 1);

		ItemMeta rankedMeta = ranked.getItemMeta();
		ItemMeta glassMeta = glass.getItemMeta();
		SkullMeta skullMeta = (SkullMeta) info.getItemMeta();
		skullMeta.setOwner(p.getName());

		ArrayList<String> lore = new ArrayList<String>();

		lore.add("");
		lore.add(ChatColor.GOLD + "Rank: " + ChatColor.GREEN + rank);
		lore.add(ChatColor.GOLD + "Tier: " + ChatColor.GREEN + tier);
		lore.add(ChatColor.GOLD + "Points: " + ChatColor.GREEN + points);

		if (d.hasFinishedPlacements()) {
			lore.add(ChatColor.GOLD + "Elo: " + ChatColor.GREEN + elo);
		}

		else {
			lore.add(ChatColor.GOLD + "Elo: " + ChatColor.GREEN + 0);
		}

		lore.add("");
		lore.add(ChatColor.GOLD + "Wins: " + ChatColor.GREEN + wins);
		lore.add(ChatColor.GOLD + "Losses: " + ChatColor.GREEN + losses);
		lore.add("");
		lore.add(ChatColor.GOLD + "Promos: " + ChatColor.GREEN + isInPromos);

		if (isInPromos) {

			int promoWins = d.getPromoWins();
			int promoLosses = d.getPromoLosses();
			lore.add(ChatColor.GOLD + "Promo Wins: " + ChatColor.GREEN + promoWins);
			lore.add(ChatColor.GOLD + "Promo Losses: " + ChatColor.GREEN + promoLosses);
		}

		if (!d.hasFinishedPlacements()) {

			int placementWins = d.getPlacementWins();
			int placementLosses = d.getPlacementLosses();
			lore.add("");
			lore.add(ChatColor.GOLD + "Placement Wins: " + ChatColor.GREEN + placementWins);
			lore.add(ChatColor.GOLD + "Placement Losses: " + ChatColor.GREEN + placementLosses);

		}
		if (d.isInTeam()) {

			Team t = cache.getTeam(p);
			ArrayList<String> rl = new ArrayList<String>();
			rankedMeta.setDisplayName(ChatColor.RED + t.getName());

			rl.add("");
			rl.add(ChatColor.GOLD + "Leader: " + ChatColor.GREEN + t.getLeaderName());
			rl.add("");
			rl.add(ChatColor.GOLD + "Members: ");

			for (String name : t.getMemberNames()) {

				if (!name.equals(t.getLeaderName())) {
					rl.add(ChatColor.DARK_GRAY + "-> " + ChatColor.GREEN + name);

				}

			}

			rl.add("");
			rl.add(ChatColor.GOLD + "Roster: ");

			for (String name : t.getRoster()) {

				rl.add(ChatColor.DARK_GRAY + "-> " + ChatColor.GREEN + name);

			}

			rl.add("");
			rl.add(ChatColor.GOLD + "Rank: " + ChatColor.GREEN + t.getRank());
			rl.add(ChatColor.GOLD + "Tier: " + ChatColor.GREEN + t.getNumeral());
			rl.add(ChatColor.GOLD + "Points: " + ChatColor.GREEN + t.getPoints());

			if (t.hasFinishedPlacements()) {
				rl.add(ChatColor.GOLD + "Elo: " + ChatColor.GREEN + t.getElo());

			}

			else {
				rl.add(ChatColor.GOLD + "Elo: " + ChatColor.GREEN + 0);
			}

			rl.add("");
			rl.add(ChatColor.GOLD + "Wins: " + ChatColor.GREEN + t.getWins());
			rl.add(ChatColor.GOLD + "Losses: " + ChatColor.GREEN + t.getLosses());
			rl.add("");

			if (t.isInPromos()) {

				int promoWins = t.getPromoWins();
				int promoLosses = t.getPromoLosses();
				rl.add(ChatColor.GOLD + "Promo Wins: " + ChatColor.GREEN + promoWins);
				rl.add(ChatColor.GOLD + "Promo Losses: " + ChatColor.GREEN + promoLosses);
				rl.add("");
			}

			if (!t.hasFinishedPlacements()) {

				int placementWins = t.getPlacementWins();
				int placementLosses = t.getPlacementLosses();

				rl.add(ChatColor.GOLD + "Placement Wins: " + ChatColor.GREEN + placementWins);
				rl.add(ChatColor.GOLD + "Placement Losses: " + ChatColor.GREEN + placementLosses);

			}

			rankedMeta.setLore(rl);

		}

		else {
			rankedMeta.setDisplayName(ChatColor.RED + "You are not in a ranked team!");
		}

		glassMeta.setDisplayName(Main.prefix);
		skullMeta.setDisplayName(ChatColor.RED + p.getName());

		skullMeta.setLore(lore);

		info.setItemMeta(skullMeta);
		glass.setItemMeta(glassMeta);
		ranked.setItemMeta(rankedMeta);

		inv.setItem(0, glass);
		inv.setItem(1, info);
		inv.setItem(2, glass);
		inv.setItem(3, ranked);
		inv.setItem(4, glass);

		p.openInventory(inv);

	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {

			if (Utils.hasDisplayName(e.getItem(), ChatColor.RED + "" + ChatColor.MAGIC + "I" + ChatColor.AQUA
					+ " Stats " + ChatColor.RED + "" + ChatColor.MAGIC + "I")) {

				openStatInventory(e.getPlayer());
			}
		}
	}

}

package me.pro.dmg.PrevailPots.Listeners;

import org.bukkit.ChatColor;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;


import me.pro.dmg.PrevailPots.Core.Cache;
import me.pro.dmg.PrevailPots.Core.Main;
import me.pro.dmg.PrevailPots.Kits.KitDataFile;

import me.pro.dmg.PrevailPots.Matchmaking.GameTypeManager;
import me.pro.dmg.PrevailPots.Matchmaking.QueueManager;
import me.pro.dmg.PrevailPots.Player.Dueler;
import me.pro.dmg.PrevailPots.Utils.Utils;

public class DuelInventory implements Listener {

	static DuelInventory instance = new DuelInventory();

	public static DuelInventory getInstance() {
		return instance;
	}

	KitDataFile kd = KitDataFile.getInstance();
	Cache cache = Cache.getInstance();
	QueueManager queue = QueueManager.getInstance();
	GameTypeManager gtm = GameTypeManager.getInstance();
	Join j = Join.getInstance();

	@EventHandler
	public void onLog(PlayerQuitEvent e) {

		Dueler d = cache.getDueler(e.getPlayer());

		if (gtm.inInPrivate(d)) {
			for (Dueler d1 : gtm.getAscociatedSet(d)) {

				if (!d1.getName().equals(e.getPlayer().getName())) {
					d1.sendMessage(
							Main.prefix + ChatColor.RED + d.getName() + ChatColor.GOLD + " has canceled the duel!");
					j.giveSpawnItems(d1.getPlayer(), true);
				}

			}
			gtm.privateDuels.remove(gtm.getAscociatedSet(d));
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {

		if (Utils.hasDisplayName(e.getItem(), ChatColor.RED + "Leave")) {

			Dueler d = cache.getDueler(e.getPlayer());

			if (gtm.inInPrivate(d)) {
				for (Dueler d1 : gtm.getAscociatedSet(d)) {

					if (!d1.getName().equals(e.getPlayer().getName())) {
						d1.sendMessage(
								Main.prefix + ChatColor.RED + d.getName() + ChatColor.GOLD + " has canceled the duel!");
					}

				}
				gtm.privateDuels.remove(gtm.getAscociatedSet(d));
				j.giveSpawnItems(e.getPlayer(), false);
				e.getPlayer().sendMessage(Main.prefix + ChatColor.GOLD + "You have cancelled the duel!");
			}

		}

	}

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e) {
		
		

		if (gtm.inInPrivate(cache.getDueler(e.getPlayer()))) {
			
			if(e.getMessage().equalsIgnoreCase("/d accept") || e.getMessage().equalsIgnoreCase("/duel accept") || e.getMessage().equalsIgnoreCase("/d decline") || e.getMessage().equalsIgnoreCase("/duel decline")) {
				return;
			}
			
			else {
				
				e.getPlayer().sendMessage(Main.prefix + ChatColor.RED + "You have a pending duel request! Use /duel accept or /duel decline");
			e.setCancelled(true);
		
			}
		}

	}

}

package me.pro.dmg.PrevailPots.Listeners;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pro.dmg.PrevailPots.Arenas.ArenaDataFile;
import me.pro.dmg.PrevailPots.Core.Cache;
import me.pro.dmg.PrevailPots.Core.Main;
import me.pro.dmg.PrevailPots.Kits.KitDataFile;
import me.pro.dmg.PrevailPots.Matchmaking.GameTypeManager;
import me.pro.dmg.PrevailPots.Matchmaking.QueueManager;
import me.pro.dmg.PrevailPots.Utils.Utils;

public class KitLocationTeleport implements Listener {

	KitDataFile kd = KitDataFile.getInstance();
	ArenaDataFile ad = ArenaDataFile.getInstance();
	GameTypeManager gtm = GameTypeManager.getInstance();
	Cache cache = Cache.getInstance();
	QueueManager queue = QueueManager.getInstance();

	@EventHandler
	public void onClick(PlayerInteractEvent e) {

		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {

			if (gtm.inInPrivate(cache.getDueler(e.getPlayer()))) {
				e.getPlayer().sendMessage(
						Main.prefix + ChatColor.RED + "You can't do that while having a pending duel request!");
				return;
			}

			if (e.getItem() != null) {

				if (e.getItem().getType() == Material.ENCHANTED_BOOK) {

					if (Utils.hasDisplayName(e.getItem(), ChatColor.GOLD + "Kit Editor")) {

						Inventory inv = Bukkit.createInventory(null, 9, ChatColor.BOLD + "Kit Editor");
						int size = kd.getData().getConfigurationSection("Kits").getKeys(false).size();

						if (size <= 9) {
							inv = Bukkit.createInventory(null, 9, ChatColor.BOLD + "Kit Editor");

						} else if (size <= 18) {
							inv = Bukkit.createInventory(null, 18, ChatColor.BOLD + "Kit Editor");

						} else if (size <= 27) {
							inv = Bukkit.createInventory(null, 27, ChatColor.BOLD + "Kit Editor");

						} else if (size <= 36) {
							inv = Bukkit.createInventory(null, 36, ChatColor.BOLD + "Kit Editor");

						} else if (size <= 45) {
							inv = Bukkit.createInventory(null, 45, ChatColor.BOLD + "Kit Editor");

						}

						for (String key : kd.getData().getConfigurationSection("Kits").getKeys(false)) {

							String name = kd.getData().getString("Kits." + key + ".Name");

							if (Main.getPlugin().getConfig().getStringList("Blocked-Kit-Edits").contains(name)) {
								continue;
							}

							Material item = Material.matchMaterial(kd.getData().getString("Kits." + key + ".Material"));
							ArrayList<String> lore = new ArrayList<String>();

							lore.add("");
							lore.add(ChatColor.GOLD + "Click to edit!");

							ItemStack is = new ItemStack(item, 1);

							ItemMeta im = is.getItemMeta();

							im.setDisplayName(ChatColor.GREEN + name);
							im.setLore(lore);

							is.setItemMeta(im);

							if (name.equalsIgnoreCase("Ranked")) {
								inv.setItem(inv.getSize() - 2, is);
							}

							else if (name.equalsIgnoreCase("TeamRanked")) {
								inv.setItem(inv.getSize() - 1, is);
							}

							else {
								inv.addItem(is);
							}

							e.getPlayer().openInventory(inv);
						}

					}
				}
			}
		}
	}

	@EventHandler
	public void onItemClick(InventoryClickEvent e) {

		if (e.getInventory().getName().equals(ChatColor.BOLD + "Kit Editor")) {

			if (e.getCurrentItem() != null) {

				if (e.getCurrentItem().hasItemMeta()) {
					if (e.getCurrentItem().getItemMeta().hasDisplayName()) {
						e.setCancelled(true);
						Player p = (Player) e.getWhoClicked();

						String name = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());

						World w = Bukkit.getWorld(ad.getData().getString("Locations.KitCreation.Spawn.World"));
						double x = ad.getData().getDouble("Locations.KitCreation.Spawn.X");
						double y = ad.getData().getDouble("Locations.KitCreation.Spawn.Y");
						double z = ad.getData().getDouble("Locations.KitCreation.Spawn.Z");
						double pitch = ad.getData().getDouble("Locations.KitCreation.Spawn.Pitch");
						double yaw = ad.getData().getDouble("Locations.KitCreation.Spawn.Yaw");

						Location KitCreation = new Location(w, x, y, z, (float) yaw, (float) pitch);
						p.getInventory().clear();

						gtm.kitEditing.put(cache.getDueler(p), queue.getGameTypeFromQueueByName(name));
						p.sendMessage(
								Main.prefix + ChatColor.GOLD + "You are now editing kit: " + ChatColor.GREEN + name);
						p.teleport(KitCreation);
						p.updateInventory();

					}
				}

			}

		}

		else if (e.getInventory().getName().equals(ChatColor.BOLD + "Recap")) {
			e.setCancelled(true);
		}

	}

}

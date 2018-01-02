package me.pro.dmg.PrevailPots.Listeners;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pro.dmg.PrevailPots.Arenas.ArenaManager;
import me.pro.dmg.PrevailPots.Core.Cache;
import me.pro.dmg.PrevailPots.Core.Main;
import me.pro.dmg.PrevailPots.Kits.KitDataFile;
import me.pro.dmg.PrevailPots.Matchmaking.GameType;
import me.pro.dmg.PrevailPots.Matchmaking.GameTypeManager;
import me.pro.dmg.PrevailPots.Matchmaking.QueueManager;
import me.pro.dmg.PrevailPots.Utils.Utils;

public class UnrankedInventory implements Listener {

	KitDataFile kd = KitDataFile.getInstance();
	Cache cache = Cache.getInstance();
	QueueManager queue = QueueManager.getInstance();
	GameTypeManager gtm = GameTypeManager.getInstance();
	ArenaManager am = ArenaManager.getInstance();

	public void openUnrankedInventory(Player p) {

		Inventory inv = Bukkit.createInventory(null, 9, ChatColor.BOLD + "Unranked");
		int size = kd.getData().getConfigurationSection("Kits").getKeys(false).size();

		if (size <= 9) {
			inv = Bukkit.createInventory(null, 9, ChatColor.BOLD + "Unranked");

		} else if (size <= 18) {
			inv = Bukkit.createInventory(null, 18, ChatColor.BOLD + "Unranked");

		} else if (size <= 27) {
			inv = Bukkit.createInventory(null, 27, ChatColor.BOLD + "Unranked");

		} else if (size <= 36) {
			inv = Bukkit.createInventory(null, 36, ChatColor.BOLD + "Unranked");

		} else if (size <= 45) {
			inv = Bukkit.createInventory(null, 45, ChatColor.BOLD + "Unranked");

		}

		for (String key : kd.getData().getConfigurationSection("Kits").getKeys(false)) {

			String name = kd.getData().getString("Kits." + key + ".Name");

			if (!name.equalsIgnoreCase("Ranked") && !name.equalsIgnoreCase("TeamRanked")) {

				Material item = Material.matchMaterial(kd.getData().getString("Kits." + key + ".Material"));
				ArrayList<String> lore = new ArrayList<String>();

				int inGame = gtm.games.get(queue.getGameTypeFromQueueByName(name)).keySet().size();

				lore.add("");
				lore.add(ChatColor.GOLD + "In Game: " + ChatColor.RED + inGame);

				ItemStack is = new ItemStack(item, 1);

				ItemMeta im = is.getItemMeta();

				im.setDisplayName(ChatColor.GREEN + name);
				im.setLore(lore);

				is.setItemMeta(im);

				inv.addItem(is);

			}

			p.openInventory(inv);
		}
	}

	@EventHandler
	public void onUnrankedClick(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (Utils.hasDisplayName(e.getItem(), ChatColor.GREEN + "Unranked")) {

				if (gtm.inInPrivate(cache.getDueler(e.getPlayer()))) {
					e.getPlayer().sendMessage(
							Main.prefix + ChatColor.RED + "You can't do that while having a pending duel request!");
					return;
				}
			

				openUnrankedInventory(e.getPlayer());
			}
		}

	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {

		if (e.getInventory().getName().equals(ChatColor.BOLD + "Stats")) {
			e.setCancelled(true);
			return;
		}

		Player p = (Player) e.getWhoClicked();

		if (e.getInventory().getName().equals(ChatColor.BOLD + "Unranked")) {

			if (e.getCurrentItem() != null) {
				if (e.getCurrentItem().hasItemMeta()) {
					if (e.getCurrentItem().getItemMeta().hasDisplayName()) {

						e.setCancelled(true);
						p.closeInventory();

						String gameTypeName = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());

						GameType gt = queue.getGameTypeFromQueueByName(gameTypeName);

						queue.confirmDuel(p, gt);
						// check if queue is full

					}
				}
			}
		}

	}

}

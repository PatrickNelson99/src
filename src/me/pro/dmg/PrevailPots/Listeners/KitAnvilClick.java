package me.pro.dmg.PrevailPots.Listeners;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pro.dmg.PrevailPots.Arenas.ArenaDataFile;
import me.pro.dmg.PrevailPots.Core.Cache;
import me.pro.dmg.PrevailPots.Core.Main;
import me.pro.dmg.PrevailPots.Matchmaking.GameType;
import me.pro.dmg.PrevailPots.Matchmaking.GameTypeManager;
import me.pro.dmg.PrevailPots.Matchmaking.QueueManager;
import me.pro.dmg.PrevailPots.Player.Dueler;

public class KitAnvilClick implements Listener {

	ArenaDataFile ad = ArenaDataFile.getInstance();
	Cache cache = Cache.getInstance();
	GameTypeManager gtm = GameTypeManager.getInstance();
	QueueManager queue = QueueManager.getInstance();

	@EventHandler
	public void onClick(PlayerInteractEvent e) {

		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {

			if (e.getClickedBlock() != null) {

				if (e.getClickedBlock().getType() == Material.ANVIL) {

					if (gtm.isEditingKit(cache.getDueler(e.getPlayer()))) {
						e.setCancelled(true);

						Player p = e.getPlayer();
				

						Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER,
								ChatColor.BOLD + "Save Kits");
						ItemStack save = new ItemStack(Material.CHEST, 1);
						ItemMeta saveMeta = save.getItemMeta();

						ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
						ItemMeta glassMeta = glass.getItemMeta();
						glassMeta.setDisplayName(Main.prefix);
						glass.setItemMeta(glassMeta);
						inv.setItem(0, glass);
						inv.setItem(4, glass);

						for (int i = 1; i < 4; i++) {

							saveMeta.setDisplayName(ChatColor.GOLD + "Custom Kit: " + ChatColor.GREEN + i);
							save.setItemMeta(saveMeta);
							inv.setItem(i, save);

						}

						p.openInventory(inv);
					}

				}

			}

		}
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {



		if (e.getInventory().getName().equals(ChatColor.BOLD + "Save Kits")) {
			e.setCancelled(true);

			if (e.getCurrentItem() != null) {
				if (e.getCurrentItem().hasItemMeta()) {
					if (e.getCurrentItem().getItemMeta().hasDisplayName()) {

						Player p = (Player) e.getWhoClicked();

						Dueler d = cache.getDueler(p);
						GameType gt = gtm.getEditingGameType(d);

						if (e.getCurrentItem().getType() == Material.CHEST) {
							// kit saver

							ItemStack[] armourContents = p.getInventory().getArmorContents().clone();
							ItemStack[] inventoryContents = p.getInventory().getContents().clone();

							String clickedName = ChatColor
									.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
							clickedName = clickedName.replace("Custom Kit: ", "");

							d.addKit(gt.getName(), clickedName, inventoryContents, armourContents);
						
							p.sendMessage(Main.prefix + ChatColor.GREEN + "Kit Saved");
							return;

						}

					}
				}
			}
		}

	}

}

package me.pro.dmg.PrevailPots.Listeners;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.pro.dmg.PrevailPots.Core.Cache;
import me.pro.dmg.PrevailPots.Core.Main;
import me.pro.dmg.PrevailPots.Matchmaking.GameType;
import me.pro.dmg.PrevailPots.Matchmaking.GameTypeManager;
import me.pro.dmg.PrevailPots.Matchmaking.QueueManager;
import me.pro.dmg.PrevailPots.Player.Dueler;
import net.md_5.bungee.api.ChatColor;

public class BookClick implements Listener {

	GameTypeManager gtm = GameTypeManager.getInstance();
	Cache cache = Cache.getInstance();
	QueueManager qm = QueueManager.getInstance();

	@EventHandler
	public void onClick(PlayerInteractEvent e) {

		if (e.getItem() != null) {

			if (e.getItem().getType() == Material.BOOK) {
				if (e.getItem().hasItemMeta()) {
					if (e.getItem().getItemMeta().hasDisplayName()) {

						Player p = e.getPlayer();

						Dueler d = cache.getDueler(p);

						GameType gt;

						if (gtm.isInTeamGame(p)) {
							gt = qm.getGameTypeFromQueueByName("TeamRanked");
						}

						else {
							gt = gtm.getGameTypeOfPlayer(d);
						}

						if (gt == null) {
							return;
						}

						String name = ChatColor.stripColor(e.getItem().getItemMeta().getDisplayName());
						name = name.replace("Custom Kit: ", "");
						p.getInventory().clear();

						if (name.equals("Default")) {

							p.getInventory().setContents(gt.getInventoryContents());
							p.getInventory().setArmorContents(gt.getArmourContents());
							p.sendMessage(Main.prefix + org.bukkit.ChatColor.GOLD + "Equipped kit: "
									+ org.bukkit.ChatColor.GREEN + "Default");
							p.updateInventory();
							return;
						}
						for (HashMap<String, HashMap<String, HashMap<String, ItemStack[]>>> kit : d.getKits()) {

							if (kit.containsKey(gt.getName())) {
								p.getInventory().clear();
								p.getInventory().setContents(kit.get(gt.getName()).get(name).get("Inventory"));
								p.getInventory().setArmorContents(kit.get(gt.getName()).get(name).get("Armour"));
								p.sendMessage(Main.prefix + org.bukkit.ChatColor.GOLD + "Equipped kit: "
										+ org.bukkit.ChatColor.GREEN + name);
								p.updateInventory();
								return;

							}
						}

					}
				}
			}

		}

	}

}

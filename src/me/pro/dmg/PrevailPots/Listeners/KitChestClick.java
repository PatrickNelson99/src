package me.pro.dmg.PrevailPots.Listeners;

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

import me.pro.dmg.PrevailPots.Arenas.ArenaDataFile;
import me.pro.dmg.PrevailPots.Core.Cache;
import me.pro.dmg.PrevailPots.Core.Main;
import me.pro.dmg.PrevailPots.Matchmaking.GameTypeManager;
import me.pro.dmg.PrevailPots.Matchmaking.QueueManager;
import me.pro.dmg.PrevailPots.Player.Dueler;
import me.pro.dmg.PrevailPots.Utils.Utils;

public class KitChestClick implements Listener {

	ArenaDataFile ad = ArenaDataFile.getInstance();
	Cache cache = Cache.getInstance();
	GameTypeManager gtm = GameTypeManager.getInstance();
	QueueManager queue = QueueManager.getInstance();

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {

		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {

			if (e.getClickedBlock().getType() == Material.CHEST) {

				World w = Bukkit.getWorld(ad.getData().getString("Locations.KitCreation.Chest.World"));
				double x = ad.getData().getDouble("Locations.KitCreation.Chest.X");
				double y = ad.getData().getDouble("Locations.KitCreation.Chest.Y");
				double z = ad.getData().getDouble("Locations.KitCreation.Chest.Z");
				double pitch = ad.getData().getDouble("Locations.KitCreation.Chest.Pitch");
				double yaw = ad.getData().getDouble("Locations.KitCreation.Chest.Yaw");

				Location kitLocation = new Location(w, x, y, z, (float) yaw, (float) pitch);

				if (e.getClickedBlock().getLocation().equals(kitLocation)) {
					e.setCancelled(true);

					Player p = e.getPlayer();
					

					Dueler d = cache.getDueler(p);
					gtm.openKitEditor(d);

				}

			}

		}

	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (e.getInventory().getName().equals(ChatColor.BOLD + "Editor")) {
			if (Utils.hasDisplayName(e.getCurrentItem(), Main.prefix)) {
				e.setCancelled(true);
			}
		}
	}

}

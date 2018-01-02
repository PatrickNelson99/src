package me.pro.dmg.PrevailPots.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.pro.dmg.PrevailPots.Core.Cache;
import me.pro.dmg.PrevailPots.Matchmaking.GameTypeManager;

public class ReturnToSpawnSign implements Listener {

	Join join = Join.getInstance();
	GameTypeManager gtm = GameTypeManager.getInstance();
	Cache cache = Cache.getInstance();

	@EventHandler
	public void onSignClick(PlayerInteractEvent e) {

		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {

			if (e.getClickedBlock().getState() instanceof Sign) {

				Sign sign = (Sign) e.getClickedBlock().getState();

				if (sign.getLine(0).equals(ChatColor.GREEN + "Right Click")) {

					if (sign.getLine(1).equals("to return to")) {
						if (sign.getLine(2).equals("spawn")) {

							join.giveSpawnItems(e.getPlayer(), true);
							gtm.kitEditing.remove(cache.getDueler(e.getPlayer()));

						}
					}

				}

			}

		}

	}

}

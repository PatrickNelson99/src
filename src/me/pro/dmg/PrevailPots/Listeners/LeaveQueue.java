package me.pro.dmg.PrevailPots.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import me.pro.dmg.PrevailPots.Core.Cache;
import me.pro.dmg.PrevailPots.Core.Main;
import me.pro.dmg.PrevailPots.Matchmaking.QueueManager;
import me.pro.dmg.PrevailPots.Utils.Utils;

public class LeaveQueue implements Listener {

	Join join = Join.getInstance();
	QueueManager qm = QueueManager.getInstance();
	Cache cache = Cache.getInstance();

	@EventHandler
	public void onQueueLeave(PlayerInteractEvent e) {

		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {

			if (Utils.hasDisplayName(e.getItem(), ChatColor.RED + "Leave Queue")) {

				qm.removeFromQueue(cache.getDueler(e.getPlayer()));
				join.giveSpawnItems(e.getPlayer(), false);
				e.getPlayer().sendMessage(Main.prefix + ChatColor.RED + "You have left the queue!");
				return;

			}

		}
	}

}

package me.pro.dmg.PrevailPots.Matchmaking;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.pro.dmg.PrevailPots.Core.Cache;
import me.pro.dmg.PrevailPots.Core.Main;
import me.pro.dmg.PrevailPots.Listeners.Join;
import me.pro.dmg.PrevailPots.Teams.Team;
import me.pro.dmg.PrevailPots.Utils.Utils;

public class TeamQueueManager implements Listener {

	Cache cache = Cache.getInstance();
	GameTypeManager gtm = GameTypeManager.getInstance();
	QueueManager qm = QueueManager.getInstance();
	Join join = Join.getInstance();

	public List<Team> queue = new ArrayList<Team>();

	private TeamQueueManager() {
	}

	static TeamQueueManager instance = new TeamQueueManager();

	public static TeamQueueManager getInstance() {

		return instance;
	}

	public void confirmDuel(Team t) {

		queue.add(t);

		if (queue.size() >= 2) {

			for (Team newDueler : queue) {

				if (!newDueler.getName().equals(t.getName())) {

					queue.remove(newDueler);
					queue.remove(t);
					gtm.addTeamGame(qm.getGameTypeFromQueueByName("TeamRanked"), t, newDueler);

					return;

				}
			}

		}

	}

	@EventHandler
	public void onLog(PlayerQuitEvent e) {

		qm.removeFromQueue(cache.getDueler(e.getPlayer()));

		for (Team newDueler : queue) {

			if (newDueler.getRoster().contains(e.getPlayer().getName())) {

				for (String names : newDueler.getRoster()) {
					join.giveSpawnItems(Bukkit.getPlayer(names), true);
				}
				newDueler.getRoster().clear();
				newDueler.sendMessage(Main.prefix + ChatColor.RED + " has logged!" + ChatColor.GOLD
						+ " You have been removed from the queue.");
				queue.remove(newDueler);

				return;

			}

		}

	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {

		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {

			if (Utils.hasDisplayName(e.getItem(), ChatColor.RED + "Leave Team Queue")) {

				for (Team newDueler : queue) {
				
					if (newDueler.getRoster().contains(e.getPlayer().getName())) {

						for (String names : newDueler.getRoster()) {
							join.giveSpawnItems(Bukkit.getPlayer(names), false);
						}

						queue.remove(newDueler);
						newDueler.sendMessage(Main.prefix + ChatColor.RED + "You have left the queue!");
						;
						return;

					}

				}

			}
		}
	}
}

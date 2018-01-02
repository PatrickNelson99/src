package me.pro.dmg.PrevailPots.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pro.dmg.PrevailPots.Core.Cache;
import me.pro.dmg.PrevailPots.Core.Main;
import me.pro.dmg.PrevailPots.Kits.KitDataFile;
import me.pro.dmg.PrevailPots.Matchmaking.GameType;
import me.pro.dmg.PrevailPots.Matchmaking.GameTypeManager;
import me.pro.dmg.PrevailPots.Matchmaking.QueueManager;
import me.pro.dmg.PrevailPots.Matchmaking.TeamQueueManager;
import me.pro.dmg.PrevailPots.Teams.Team;
import me.pro.dmg.PrevailPots.Utils.Utils;

public class RankedInventory implements Listener {

	KitDataFile kd = KitDataFile.getInstance();
	Cache cache = Cache.getInstance();
	QueueManager queue = QueueManager.getInstance();
	GameTypeManager gtm = GameTypeManager.getInstance();
	TeamQueueManager tq = TeamQueueManager.getInstance();
	int teamSize = Main.getPlugin().getConfig().getInt("TeamSize");

	@EventHandler
	public void onRankedClick(PlayerInteractEvent e) {

		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (Utils.hasDisplayName(e.getItem(), ChatColor.RED + "Ranked")) {

				e.setCancelled(true);
				if (gtm.inInPrivate(cache.getDueler(e.getPlayer()))) {
					e.getPlayer().sendMessage(
							Main.prefix + ChatColor.RED + "You can't do that while having a pending duel request!");
					return;
				}
				GameType gt = queue.getGameTypeFromQueueByName("Ranked");

				queue.confirmDuel(e.getPlayer(), gt);
				e.getPlayer().sendMessage(Main.prefix + ChatColor.GREEN + "You have joined the ranked queue!");

			}
		}

	}

	@EventHandler
	public void onTeamRanked(PlayerInteractEvent e) {

		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {

			if (Utils.hasDisplayName(e.getItem(), ChatColor.RED + "2v2 Ranked")) {
				Player p = e.getPlayer();

				if (!cache.isInTeam(p)) {

					p.sendMessage(Main.prefix + ChatColor.RED + "You must be in a team to join the queue!");
					return;

				}

				Team t = cache.getTeam(p);
				// EDIT TO 5
				if (t.getRoster().size() < teamSize) {

					p.sendMessage(Main.prefix + ChatColor.RED + "Your team doesn't have a full roster!");
					return;

				}

				if (gtm.teamIsInGame(e.getPlayer())) {
					p.sendMessage(Main.prefix + ChatColor.RED + "Your team is already in a game!");
					return;
				}

				for (String name : t.getRoster()) {

					if (Bukkit.getPlayer(name) == null) {
						p.sendMessage(Main.prefix + ChatColor.GOLD + "You can't join the queue as " + ChatColor.RED
								+ Bukkit.getPlayer(name) + ChatColor.GOLD + " is currently offline!");
						return;
					}

					if (gtm.isInOneGame(cache.getDueler(Bukkit.getPlayer(name)))) {

						p.sendMessage(Main.prefix + ChatColor.GOLD + "You can't join the queue as " + ChatColor.RED
								+ Bukkit.getPlayer(name) + ChatColor.GOLD + " is currently in a duel!");
						return;
					}

					if (queue.isInQueue(cache.getDueler(Bukkit.getPlayer(name)))) {

						p.sendMessage(Main.prefix + ChatColor.GOLD + "You can't join the queue as " + ChatColor.RED
								+ Bukkit.getPlayer(name) + ChatColor.GOLD + " is currently in queue!");

						return;

					}

					if (gtm.isEditingKit(cache.getDueler(Bukkit.getPlayer(name)))) {
						p.sendMessage(Main.prefix + ChatColor.GOLD + "You can't join the queue as " + ChatColor.RED
								+ Bukkit.getPlayer(name) + ChatColor.GOLD + " is currently editing a kit!");
						return;
					}

				}

				p.getInventory().clear();

				ItemStack leave = new ItemStack(Material.REDSTONE, 1);
				ItemMeta meta = leave.getItemMeta();
				meta.setDisplayName(ChatColor.RED + "Leave Team Queue");
				leave.setItemMeta(meta);

				for (String name : t.getRoster()) {
					Bukkit.getPlayer(name).getInventory().setItem(0, leave);
					Bukkit.getPlayer(name).updateInventory();
				}

				tq.confirmDuel(t);
				t.sendMessage(Main.prefix + ChatColor.GREEN + "Your team has joined the queue!");

			}

		}

	}

}

package me.pro.dmg.PrevailPots.Listeners;

import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.pro.dmg.PrevailPots.Core.Cache;
import me.pro.dmg.PrevailPots.Core.Main;
import me.pro.dmg.PrevailPots.Matchmaking.GameTypeManager;
import me.pro.dmg.PrevailPots.Matchmaking.QueueManager;
import me.pro.dmg.PrevailPots.Teams.Team;

public class Misc implements Listener {

	GameTypeManager gt = GameTypeManager.getInstance();
	QueueManager qm = QueueManager.getInstance();
	Cache cache = Cache.getInstance();
	ArrayList<String> blockedCmds = new ArrayList<String>(
			Main.getPlugin().getConfig().getStringList("Blocked-Duel-Commands"));

	@EventHandler
	public void onEnderpearl(PlayerInteractEvent e) {

		if (e.getItem() != null) {
			if (e.getItem().getType() == Material.ENDER_PEARL) {

				if (gt.enderpearlCooldown.contains(e.getPlayer().getName())) {
					e.setCancelled(true);
					e.getPlayer().sendMessage(
							Main.prefix + ChatColor.RED + "The duel has just started. You can't use enderpearls yet!");
				}

			}
		}

	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {

		if (!e.getPlayer().isOp()) {
			e.setCancelled(true);
		}

	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {

		if (e.getPlayer().isOp()) {
			return;
		}

		if (gt.isEditingKit(cache.getDueler(e.getPlayer()))) {
			e.getItemDrop().remove();
			return;
		}

		final Entity dropped = e.getItemDrop();
		new BukkitRunnable() {
			public void run() {

				if (dropped == null) {
					return;
				}
				if (dropped.isDead()) {
					return;
				}

				dropped.remove();
			}
		}.runTaskLater(Main.getPlugin(), 60);

	}

	@EventHandler
	public void onChat(PlayerCommandPreprocessEvent e) {

		if (gt.isInOneGame(cache.getDueler(e.getPlayer()))) {

			for (String blockedCommand : blockedCmds) {
				String command = e.getMessage().replace("/", "");
				String commandArgs[] = command.split(" ", 2);

				if (commandArgs[0].toLowerCase().equals(blockedCommand.toLowerCase())) {
					e.getPlayer()
							.sendMessage(Main.prefix + ChatColor.RED + "Please wait until your duel has finished!");
					e.setCancelled(true);
					return;

				}

			}

		}
		if (cache.isInTeam(e.getPlayer())) {

			Team t = cache.getTeam(e.getPlayer());

			if (gt.teamIsInGame(e.getPlayer())) {

				if (t.getRoster().contains(e.getPlayer().getName())) {

					for (String blockedCommand : blockedCmds) {
						String command = e.getMessage();
						String commandArgs[] = command.split(" ", 2);

						if (commandArgs[0].toLowerCase().equals(blockedCommand.toLowerCase())) {
							e.getPlayer().sendMessage(
									Main.prefix + ChatColor.RED + "Please wait until your duel has finished!");
							e.setCancelled(true);
							return;

						}

					}

				}

			}
		}

	}

}

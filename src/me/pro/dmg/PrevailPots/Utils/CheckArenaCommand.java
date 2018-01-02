package me.pro.dmg.PrevailPots.Utils;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.pro.dmg.PrevailPots.Arenas.Arena;
import me.pro.dmg.PrevailPots.Arenas.ArenaManager;
import me.pro.dmg.PrevailPots.Core.AdminSubCommand;
import me.pro.dmg.PrevailPots.Core.Main;

public class CheckArenaCommand extends AdminSubCommand {

	ArenaManager am = ArenaManager.getInstance();

	@Override
	public void onCommand(CommandSender sender, String[] args) {

		if (sender instanceof Player) {

			if (sender.isOp()) {

				int timer = 0;

				final Player p = (Player) sender;

				for (Arena arena : am.arenas.keySet()) {

					final int id = arena.getID();
					final Location blue = arena.getBlueSpawn();
					final Location red = arena.getRedSpawn();

					new BukkitRunnable() {
						public void run() {

							p.teleport(blue);
							p.sendMessage(Main.prefix + ChatColor.GOLD + "Arena: " + ChatColor.GREEN + id
									+ ChatColor.GOLD + " Spawn: " + ChatColor.BLUE + "Blue");

							new BukkitRunnable() {
								public void run() {
									p.teleport(red);
									p.sendMessage(Main.prefix + ChatColor.GOLD + "Arena: " + ChatColor.GREEN + id
											+ ChatColor.GOLD + " Spawn: " + ChatColor.RED + "Red");
								}
							}.runTaskLater(Main.getPlugin(), 30);

						}
					}.runTaskLater(Main.getPlugin(), timer);
					timer += 60;
				}

			}

		}

	}

	@Override
	public String name() {

		return "checkarenas";
	}

	@Override
	public String info() {

		return "Teleports you to all arena locations";
	}

}

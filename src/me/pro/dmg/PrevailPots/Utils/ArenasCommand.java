package me.pro.dmg.PrevailPots.Utils;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import me.pro.dmg.PrevailPots.Arenas.Arena;
import me.pro.dmg.PrevailPots.Arenas.ArenaManager;

public class ArenasCommand implements CommandExecutor {

	ArenaManager am = ArenaManager.getInstance();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String l, String[] args) {

		if (cmd.getName().equalsIgnoreCase("arenas")) {

			for (Arena arena : am.arenas.keySet()) {

				final int id = arena.getID();
				final Location blue = arena.getBlueSpawn();
				String blueWorld = blue.getWorld().getName();
				int blueX = blue.getBlockX();
				int blueY = blue.getBlockY();
				int blueZ = blue.getBlockZ();

				final Location red = arena.getRedSpawn();
				String redWorld = red.getWorld().getName();
				int redX = red.getBlockX();
				int redY = red.getBlockY();
				int redZ = red.getBlockZ();
				sender.sendMessage(ChatColor.DARK_GRAY + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
				sender.sendMessage(ChatColor.GOLD + "Arena: " + ChatColor.GREEN + id + "\n");

				sender.sendMessage(ChatColor.GOLD + "Spawn: " + ChatColor.BLUE + "Blue\n");

				sender.sendMessage(ChatColor.GOLD + "World: " + ChatColor.RED + blueWorld);
				sender.sendMessage(ChatColor.GOLD + "X: " + ChatColor.RED + blueX);
				sender.sendMessage(ChatColor.GOLD + "Y: " + ChatColor.RED + blueY);
				sender.sendMessage(ChatColor.GOLD + "Z: " + ChatColor.RED + blueZ + "\n");

				sender.sendMessage(ChatColor.GOLD + "Spawn: " + ChatColor.RED + "Red\n");

				sender.sendMessage(ChatColor.GOLD + "World: " + ChatColor.RED + redWorld);
				sender.sendMessage(ChatColor.GOLD + "X: " + ChatColor.RED + redX);
				sender.sendMessage(ChatColor.GOLD + "Y: " + ChatColor.RED + redY);
				sender.sendMessage(ChatColor.GOLD + "Z: " + ChatColor.RED + redZ);

			}
			sender.sendMessage(ChatColor.DARK_GRAY + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
		}
		return false;
	}
}

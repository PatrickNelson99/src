package me.pro.dmg.PrevailPots.Arenas;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.pro.dmg.PrevailPots.Core.AdminSubCommand;
import me.pro.dmg.PrevailPots.Core.Main;

public class SetArenaCommand extends AdminSubCommand {

	ArenaDataFile ad = ArenaDataFile.getInstance();

	@Override
	public void onCommand(CommandSender sender, String[] args) {

		if (sender instanceof Player) {

			if (sender.hasPermission("prevailpvp.admin")) {

				Player p = (Player) sender;

				if (args.length != 2) {

					p.sendMessage(Main.prefix + ChatColor.RED + "Use /setarena <id> <team>");
					return;
				}

				try {

					Integer.parseInt(args[0]);
				}

				catch (NumberFormatException e) {

					p.sendMessage(Main.prefix + ChatColor.RED + args[0] + ChatColor.GOLD + " is not a number!");
					return;
				}

				if (!(args[1].equalsIgnoreCase("red") || args[1].equalsIgnoreCase("blue"))) {

					p.sendMessage(Main.prefix + ChatColor.GOLD + "Team must be either " + ChatColor.RED + "Red "
							+ ChatColor.GOLD + "or " + ChatColor.BLUE + "Blue");
					return;

				}

				int id = Integer.parseInt(args[0]);

				Location loc = p.getLocation();

				if (args[1].equalsIgnoreCase("red")) {

					ad.getData().set("Arenas." + id + ".Red.World", loc.getWorld().getName());
					ad.getData().set("Arenas." + id + ".Red.X", loc.getX());
					ad.getData().set("Arenas." + id + ".Red.Y", loc.getY());
					ad.getData().set("Arenas." + id + ".Red.Z", loc.getZ());
					ad.getData().set("Arenas." + id + ".Red.Pitch", loc.getPitch());
					ad.getData().set("Arenas." + id + ".Red.Yaw", loc.getYaw());
					ad.saveData();

					p.sendMessage(Main.prefix + ChatColor.GOLD + "You have set " + ChatColor.RED + "Red "
							+ ChatColor.GOLD + "spawn for arena: " + ChatColor.RED + id);

					return;
				}

				else if (args[1].equalsIgnoreCase("blue")) {

					ad.getData().set("Arenas." + id + ".Blue.World", loc.getWorld().getName());
					ad.getData().set("Arenas." + id + ".Blue.X", loc.getX());
					ad.getData().set("Arenas." + id + ".Blue.Y", loc.getY());
					ad.getData().set("Arenas." + id + ".Blue.Z", loc.getZ());
					ad.getData().set("Arenas." + id + ".Blue.Pitch", loc.getPitch());
					ad.getData().set("Arenas." + id + ".Blue.Yaw", loc.getYaw());
					ad.saveData();

					p.sendMessage(Main.prefix + ChatColor.GOLD + "You have set " + ChatColor.BLUE + "Blue "
							+ ChatColor.GOLD + "spawn for arena: " + ChatColor.RED + id);

					return;

				}
			}

		}

	}

	@Override
	public String name() {

		return "setarena";
	}

	@Override
	public String info() {

		return "Sets arena locations";
	}

}

package me.pro.dmg.PrevailPots.Kits;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.pro.dmg.PrevailPots.Arenas.ArenaDataFile;
import me.pro.dmg.PrevailPots.Core.AdminSubCommand;
import me.pro.dmg.PrevailPots.Core.Main;

public class SetKitChest extends AdminSubCommand {

	ArenaDataFile ad = ArenaDataFile.getInstance();

	@SuppressWarnings("deprecation")
	@Override
	public void onCommand(CommandSender sender, String[] args) {
		if (sender instanceof Player) {

			Player p = (Player) sender;

			Block b = p.getTargetBlock(null, 5);
			Location loc = b.getLocation();

			ad.getData().set("Locations.KitCreation.Chest.World", loc.getWorld().getName());
			ad.getData().set("Locations.KitCreation.Chest.X", loc.getX());
			ad.getData().set("Locations.KitCreation.Chest.Y", loc.getY());
			ad.getData().set("Locations.KitCreation.Chest.Z", loc.getZ());
			ad.getData().set("Locations.KitCreation.Chest.Yaw", loc.getYaw());
			ad.getData().set("Locations.KitCreation.Chest.Pitch", loc.getPitch());
			ad.saveData();

			p.sendMessage(Main.prefix + ChatColor.GREEN + "Chest location set!");

		}

	}

	@Override
	public String name() {

		return "kitchest";
	}

	@Override
	public String info() {

		return "Sets kit editor chest location";
	}

}

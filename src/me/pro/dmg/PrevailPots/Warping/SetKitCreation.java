package me.pro.dmg.PrevailPots.Warping;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.pro.dmg.PrevailPots.Arenas.ArenaDataFile;
import me.pro.dmg.PrevailPots.Core.AdminSubCommand;
import me.pro.dmg.PrevailPots.Core.Main;

public class SetKitCreation extends AdminSubCommand {

	ArenaDataFile ad = ArenaDataFile.getInstance();

	@Override
	public void onCommand(CommandSender sender, String[] args) {

		if (sender instanceof Player) {

			Player p = (Player) sender;

			Location loc = p.getLocation();

			ad.getData().set("Locations.KitCreation.Spawn.World", loc.getWorld().getName());
			ad.getData().set("Locations.KitCreation.Spawn.X", loc.getX());
			ad.getData().set("Locations.KitCreation.Spawn.Y", loc.getY());
			ad.getData().set("Locations.KitCreation.Spawn.Z", loc.getZ());
			ad.getData().set("Locations.KitCreation.Spawn.Yaw", loc.getYaw());
			ad.getData().set("Locations.KitCreation.Spawn.Pitch", loc.getPitch());
			ad.saveData();

			p.sendMessage(Main.prefix + ChatColor.GREEN + "Location set!");
		}

	}

	@Override
	public String name() {

		return "seteditor";
	}

	@Override
	public String info() {

		return "Sets kit editor spawn location";
	}

}

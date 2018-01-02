package me.pro.dmg.PrevailPots.Warping;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import me.pro.dmg.PrevailPots.Arenas.ArenaDataFile;

public class WarpingUtils {

	ArenaDataFile ad = ArenaDataFile.getInstance();

	public void teleportToSpawn(Player p) {

		World w = Bukkit.getWorld(ad.getData().getString("Location.Spawn.World"));
		double x = ad.getData().getDouble("Locations.Spawn.X");
		double y = ad.getData().getDouble("Locations.Spawn.Y");
		double z = ad.getData().getDouble("Locations.Spawn.Z");
		float yaw = (float) ad.getData().getDouble("Locations.Spawn.Yaw");
		float pitch = (float) ad.getData().getDouble("Locations.Spawn.Pitch");

		Location loc = new Location(w, x, y, z, yaw, pitch);

		p.teleport(loc);

	}

}

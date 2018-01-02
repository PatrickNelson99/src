package me.pro.dmg.PrevailPots.Arenas;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class Arena {

	ArenaDataFile ad = ArenaDataFile.getInstance();
	private int id;
	private Location redSpawn, blueSpawn;

	public Arena(int id) {

		this.id = id;

		World redWorld = Bukkit.getWorld(ad.getData().getString("Arenas." + id + ".Red.World"));
		double redX = ad.getData().getInt("Arenas." + id + ".Red.X");
		double redY = ad.getData().getInt("Arenas." + id + ".Red.Y");
		double redZ = ad.getData().getInt("Arenas." + id + ".Red.Z");
		double redPitch = ad.getData().getDouble("Arenas." + id + ".Red.Pitch");
		double redYaw = ad.getData().getDouble("Arenas." + id + ".Red.Yaw");

		World blueWorld = Bukkit.getWorld(ad.getData().getString("Arenas." + id + ".Blue.World"));
		double blueX = ad.getData().getInt("Arenas." + id + ".Blue.X");
		double blueY = ad.getData().getInt("Arenas." + id + ".Blue.Y");
		double blueZ = ad.getData().getInt("Arenas." + id + ".Blue.Z");
		double bluePitch = ad.getData().getDouble("Arenas." + id + ".Blue.Pitch");
		double blueYaw = ad.getData().getDouble("Arenas." + id + ".Blue.Yaw");

		this.redSpawn = new Location(redWorld, redX, redY, redZ, (float) redYaw, (float) redPitch);
		this.blueSpawn = new Location(blueWorld, blueX, blueY, blueZ, (float) blueYaw, (float) bluePitch);
	}

	public int getID() {
		return id;
	}

	public Location getBlueSpawn() {
		return blueSpawn;
	}

	public Location getRedSpawn() {
		return redSpawn;
	}
}

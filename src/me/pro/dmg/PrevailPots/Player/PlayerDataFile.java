package me.pro.dmg.PrevailPots.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import me.pro.dmg.PrevailPots.Core.Main;

public class PlayerDataFile {

	private PlayerDataFile() {
	}

	static PlayerDataFile instance = new PlayerDataFile();

	public static PlayerDataFile getInstance() {
		return instance;
	}

	public void setup(Plugin p) {
		File userFile = new File(p.getDataFolder().getAbsolutePath() + File.separator + "Users");

		if (!userFile.exists()) {

			Bukkit.broadcastMessage(Main.prefix + ChatColor.RED + "User File being generated");
			userFile.mkdirs();

		}

	}

	public void addFile(Player p) {

		File dfile;

		dfile = new File(Main.getPlugin().getDataFolder().getAbsolutePath() + File.separator + "Users",
				p.getUniqueId().toString() + ".yml");

		if (!dfile.exists()) {
			try {

				dfile.createNewFile();

				FileConfiguration data = YamlConfiguration.loadConfiguration(dfile);

				data.set("Info.General.Name", p.getName());
				data.set("Info.General.ID", p.getUniqueId().toString());
				data.set("Info.Ranking.Rank", "Placements");
				data.set("Info.Ranking.Tier", 1);
				data.set("Info.Ranking.Points", 0);
				data.set("Info.Ranking.Elo", 500);
				data.set("Info.Ranking.Wins", 0);
				data.set("Info.Ranking.Losses", 0);
				data.set("Info.Ranking.Promos.Enabled", false);
				data.set("Info.Ranking.Promos.Wins", 0);
				data.set("Info.Ranking.Promos.Losses", 0);
				data.set("Info.Placements.Wins", 0);
				data.set("Info.Placements.Losses", 0);
				data.createSection("CustomKits");
				data.save(dfile);

			} catch (IOException e) {
				Bukkit.getServer().getLogger()
						.severe(ChatColor.RED + "Could not create " + p.getName() + "'s Data File!");
			}

			return;
		}

	}

	public FileConfiguration getData(String id) {

		File f = new File(Main.getPlugin().getDataFolder().getAbsolutePath() + File.separator + "Users", id + ".yml");
		FileConfiguration data = YamlConfiguration.loadConfiguration(f);

		return data;

	}

	public void saveData(String id, FileConfiguration c) {

		File f = new File(Main.getPlugin().getDataFolder().getAbsolutePath() + File.separator + "Users", id + ".yml");

		try {
			c.save(f);
		} catch (IOException e1) {

			e1.printStackTrace();
		}

	}

	public int getElo(String id) {

		return getData(id).getInt("Info.Ranking.Elo");

	}

	public boolean hasFinishedPlacements(String id) {

		if (getTotalPlacements(id) == 10) {
			return true;
		}
		return false;

	}

	public int getPlacementWins(String id) {

		return getData(id).getInt("Info.Placements.Wins");

	}

	public int getPlacementLosses(String id) {

		return getData(id).getInt("Info.Placements.Losses");

	}

	public int getTotalPlacements(String id) {

		return getPlacementWins(id) + getPlacementLosses(id);

	}

	public String getRank(String id) {

		return getData(id).getString("Info.Ranking.Rank");

	}

	public int getPoints(String id) {

		return getData(id).getInt("Info.Ranking.Points");

	}

	public boolean isInPromo(String id) {

		return getData(id).getBoolean("Info.Ranking.Promos.Enabled");
	}

	public int getPromoWins(String id) {

		return getData(id).getInt("Info.Ranking.Promos.Wins");

	}

	public int getPromoLosses(String id) {

		return getData(id).getInt("Info.Ranking.Promos.Losses");

	}

	public int getPromoGamesPlayed(String id) {

		return getPromoWins(id) + getPromoLosses(id);

	}

	public int getTier(String id) {
		return getData(id).getInt("Info.Ranking.Tier");
	}

	public int getWins(String id) {
		return getData(id).getInt("Info.Ranking.Wins");

	}

	public int getLosses(String id) {
		return getData(id).getInt("Info.Ranking.Losses");

	}

	public ArrayList<HashMap<String, HashMap<String, HashMap<String, ItemStack[]>>>> getKits(String id) {

		ArrayList<HashMap<String, HashMap<String, HashMap<String, ItemStack[]>>>> kits = new ArrayList<HashMap<String, HashMap<String, HashMap<String, ItemStack[]>>>>();
		// <name of game type> , <custom name, inventory/armour : contents>

		for (String gameTypeName : getData(id).getConfigurationSection("CustomKits").getKeys(false)) {

			HashMap<String, HashMap<String, HashMap<String, ItemStack[]>>> k = new HashMap<String, HashMap<String, HashMap<String, ItemStack[]>>>();
			HashMap<String, HashMap<String, ItemStack[]>> customKit = new HashMap<String, HashMap<String, ItemStack[]>>();

			for (String customKitName : getData(id).getConfigurationSection("CustomKits." + gameTypeName)
					.getKeys(false)) {

				ItemStack[] inventoryContents = getData(id)
						.getList("CustomKits." + gameTypeName + "." + customKitName + ".Inventory")
						.toArray(new ItemStack[0]);

				ItemStack[] armourContents = getData(id)
						.getList("CustomKits." + gameTypeName + "." + customKitName + ".Armour")
						.toArray(new ItemStack[0]);

				HashMap<String, ItemStack[]> contents = new HashMap<String, ItemStack[]>();
				contents.put("Inventory", inventoryContents);
				contents.put("Armour", armourContents);
				customKit.put(customKitName, contents);

			}
			k.put(gameTypeName, customKit);
			kits.add(k);

		}
		return kits;

	}

	public void saveKits(Dueler d) {

	}

}
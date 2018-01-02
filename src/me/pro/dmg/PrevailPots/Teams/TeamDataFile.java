package me.pro.dmg.PrevailPots.Teams;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.pro.dmg.PrevailPots.Core.Cache;
import me.pro.dmg.PrevailPots.Core.Main;
import me.pro.dmg.PrevailPots.Core.MySQL;

public class TeamDataFile {

	MySQL sql = MySQL.getInstance();
	Cache cache = Cache.getInstance();

	private TeamDataFile() {
	}

	static TeamDataFile instance = new TeamDataFile();

	public static TeamDataFile getInstance() {
		return instance;
	}

	public Set<String> createCooldown = new HashSet<String>();

	public void setup(Plugin p) {
		File userFile = new File(p.getDataFolder().getAbsolutePath() + File.separator + "Teams");

		if (!userFile.exists()) {

			Bukkit.broadcastMessage(Main.prefix + ChatColor.RED + "Team File being generated");
			userFile.mkdirs();

		}

	}

	public Boolean addFile(final Player p, String name) {

		boolean status = false;
		File dfile;

		dfile = new File(Main.getPlugin().getDataFolder().getAbsolutePath() + File.separator + "Teams", name + ".yml");

		if (!dfile.exists()) {
			try {

				dfile.createNewFile();

				FileConfiguration data = YamlConfiguration.loadConfiguration(dfile);
				ArrayList<String> members = new ArrayList<String>();
				members.add(p.getUniqueId().toString());
				data.set("Info.General.Name", name);
				data.set("Info.General.Leader", p.getUniqueId().toString());
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
				data.set("Members", members);
				data.save(dfile);
				p.sendMessage(Main.prefix + ChatColor.GOLD + "You have successfully created the team " + ChatColor.GREEN
						+ name + ChatColor.GOLD + "!");
				createCooldown.add(p.getUniqueId().toString());

				new BukkitRunnable() {
					public void run() {
						createCooldown.remove(p.getUniqueId().toString());

					}
				}.runTaskLater(Main.getPlugin(), 20 * Main.getPlugin().getConfig().getInt("Team-Create-Cooldown"));

				status = true;

			} catch (IOException e) {
				Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not create " + name + "'s Data File!");
			}

		}

		else {
			status = false;
			p.sendMessage(Main.prefix + ChatColor.RED + "A team with the name " + ChatColor.GOLD + name + ChatColor.RED
					+ " already exists!");
		}

		return status;

	}

	public boolean disband(Player p, String name) {

		boolean status = false;
		File dfile = new File(Main.getPlugin().getDataFolder().getAbsolutePath() + File.separator + "Teams",
				name + ".yml");

		if (dfile.exists()) {
			p.sendMessage(Main.prefix + ChatColor.GREEN + name + ChatColor.GOLD + " has been deleted!");

			FileConfiguration data = YamlConfiguration.loadConfiguration(dfile);

			for (String id : data.getStringList("Members")) {

				try {
					sql.setTeam(id, "0");
				} catch (SQLException e) {

					e.printStackTrace();
				}

			}

			dfile.delete();
			status = true;
		}

		else {
			p.sendMessage(Main.prefix + ChatColor.GOLD + "The team " + ChatColor.GREEN + name + ChatColor.GOLD
					+ " doesn't exist!");

		}
		return status;

	}

	public boolean isOnCreateCooldown(Player p) {
		if (createCooldown.contains(p.getUniqueId().toString())) {
			return true;
		}
		return false;
	}

	public String getTeamName(String playerID) {

		for (File f : new File(Main.getPlugin().getDataFolder().getAbsoluteFile() + File.separator + "Teams")
				.listFiles()) {

			FileConfiguration data = YamlConfiguration.loadConfiguration(f);

			for (String member : data.getStringList("Members")) {

				if (member.equals(playerID)) {
					return data.getString("Info.General.Name");
				}

			}

		}
		return null;

	}

	public FileConfiguration getData(String id) {

		File f = new File(Main.getPlugin().getDataFolder().getAbsolutePath() + File.separator + "Teams",
				getTeamName(id) + ".yml");
		FileConfiguration data = YamlConfiguration.loadConfiguration(f);

		return data;

	}

	public void saveData(String name, FileConfiguration c) {

		File f = new File(Main.getPlugin().getDataFolder().getAbsolutePath() + File.separator + "Teams", name + ".yml");

		try {
			c.save(f);
		} catch (IOException e1) {

			e1.printStackTrace();
		}

	}

	public int getName(String id) {

		return getData(id).getInt("Info.General.Name");

	}

	public String getLeader(String id) {

		return getData(id).getString("Info.General.Leader");

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

	public List<String> getMembers(String id) {
		return getData(id).getStringList("Members");
	}

}
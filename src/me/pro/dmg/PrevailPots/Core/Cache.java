package me.pro.dmg.PrevailPots.Core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.pro.dmg.PrevailPots.Matchmaking.GameTypeManager;
import me.pro.dmg.PrevailPots.Player.Dueler;
import me.pro.dmg.PrevailPots.Player.PlayerDataFile;
import me.pro.dmg.PrevailPots.Teams.Team;
import me.pro.dmg.PrevailPots.Teams.TeamDataFile;

public class Cache implements Listener {
	private Cache() {
	}

	static Cache instance = new Cache();

	public static Cache getInstance() {
		return instance;
	}

	PlayerDataFile pd = PlayerDataFile.getInstance();
	TeamDataFile td = TeamDataFile.getInstance();
	GameTypeManager gtm = GameTypeManager.getInstance();
	public List<Team> teams = new ArrayList<Team>();
	public Map<String, Team> teamInvites = new HashMap<String, Team>();

	public ArrayList<Dueler> duelers = new ArrayList<Dueler>();

	public void addTeam(final Player p) {

		// load team when one of the players comes online, remove if all players
		// are offline

		// if cache does not contains team with player
		if (!isInTeam(p)) {

			// if they belong to a team
			if (td.getTeamName(p.getUniqueId().toString()) != null) {

				String teamName = td.getTeamName(p.getUniqueId().toString());
				String leader = td.getLeader(p.getUniqueId().toString());
				String rank = td.getRank(p.getUniqueId().toString());

				int tier = td.getTier(p.getUniqueId().toString());
				int points = td.getPoints(p.getUniqueId().toString());
				int elo = td.getElo(p.getUniqueId().toString());
				boolean promosEnabled = td.isInPromo(p.getUniqueId().toString());
				int promoWins = td.getPromoWins(p.getUniqueId().toString());
				int promoLosses = td.getPromoLosses(p.getUniqueId().toString());
				int placementWins = td.getPlacementWins(p.getUniqueId().toString());
				int placementLosses = td.getPlacementLosses(p.getUniqueId().toString());
				int wins = td.getWins(p.getUniqueId().toString());
				int losses = td.getLosses(p.getUniqueId().toString());
				List<String> members = td.getMembers(p.getUniqueId().toString());

				// public Team(String leader, Set<String> members, String rank,
				// int tier, int points, int elo, boolean promosEnabled,
				// int promoWins, int promoLosses, int placementWins, int wins,
				// int losses, int placementLosses) {

				teams.add(new Team(teamName, leader, members, rank, tier, points, elo, promosEnabled, promoWins,
						promoLosses, placementWins, wins, losses, placementLosses, new ArrayList<String>()));

			}

		}

	}

	public void saveData() {

		new BukkitRunnable() {
			public void run() {

				if (duelers.size() != 0) {

					Iterator<Dueler> iter = duelers.iterator();

					while (iter.hasNext()) {

						Dueler d = iter.next();

						FileConfiguration data = pd.getData(d.getUUID());

						data.set("Info.General.Name", d.getName());
						data.set("Info.General.ID", d.getUUID());
						data.set("Info.Ranking.Rank", d.getRank());
						data.set("Info.Ranking.Tier", d.getTier());
						data.set("Info.Ranking.Points", d.getPoints());
						data.set("Info.Ranking.Elo", d.getElo());
						data.set("Info.Ranking.Wins", d.getWins());
						data.set("Info.Ranking.Losses", d.getLosses());
						data.set("Info.Ranking.Promos.Enabled", d.promosEnabled());
						data.set("Info.Ranking.Promos.Wins", d.getPromoWins());
						data.set("Info.Ranking.Promos.Losses", d.getPromoLosses());
						data.set("Info.Placements.Wins", d.getPlacementWins());
						data.set("Info.Placements.Losses", d.getPlacementLosses());

						ConfigurationSection k = data.getConfigurationSection("CustomKits");

						ArrayList<HashMap<String, HashMap<String, HashMap<String, ItemStack[]>>>> kits = d.getKits();

						for (int i = 0; i < kits.size(); i++) {

							for (String gameTypeName : kits.get(i).keySet()) {

								for (String customKitName : kits.get(i).get(gameTypeName).keySet()) {

									ItemStack[] inventoryContents = kits.get(i).get(gameTypeName).get(customKitName)
											.get("Inventory");
									ItemStack[] armourContents = kits.get(i).get(gameTypeName).get(customKitName)
											.get("Armour");
									k.set(gameTypeName + "." + customKitName + ".Armour", armourContents);
									k.set(gameTypeName + "." + customKitName + ".Inventory", inventoryContents);

								}

							}

						}

						pd.saveData(d.getUUID(), data);

						if (Bukkit.getPlayer(d.getName()) == null) {
							iter.remove();
						}
					}

				}

				if (teams.size() != 0) {

					Iterator<Team> iter = teams.iterator();

					while (iter.hasNext()) {

						Team d = iter.next();

						FileConfiguration data = td.getData(d.getLeader());

						data.set("Info.General.Name", d.getName());
						data.set("Info.General.Leader", d.getLeader());
						data.set("Info.Ranking.Rank", d.getRank());
						data.set("Info.Ranking.Tier", d.getTier());
						data.set("Info.Ranking.Points", d.getPoints());
						data.set("Info.Ranking.Elo", d.getElo());
						data.set("Info.Ranking.Wins", d.getWins());
						data.set("Info.Ranking.Losses", d.getLosses());
						data.set("Info.Ranking.Promos.Enabled", d.isInPromos());
						data.set("Info.Ranking.Promos.Wins", d.getPromoWins());
						data.set("Info.Ranking.Promos.Losses", d.getPromoLosses());
						data.set("Info.Placements.Wins", d.getPlacementWins());
						data.set("Info.Placements.Losses", d.getPlacementLosses());
						data.set("Members", d.getMembers());

						td.saveData(d.getName(), data);

						for (String id : d.getMembers()) {

							if (Bukkit.getPlayer(UUID.fromString(id)) != null) {

								return;

							}

						}

						iter.remove();

					}

				}

			}
		}.runTaskTimer(Main.getPlugin(), 1, 20 * 10);

	}

	public Team getTeamByName(String name) {
		for (Team t : teams) {
			if (t.getName().equalsIgnoreCase(name)) {
				return t;
			}
		}
		return null;
	}

	public boolean containsTeam(String name) {

		for (Team t : teams) {

			if (t.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;

	}

	public void addDueler(Player p) {

		if (getDueler(p) == null) {

			String name = p.getName();
			String id = p.getUniqueId().toString();
			String rank = pd.getRank(p.getUniqueId().toString());
			int tier = pd.getTier(p.getUniqueId().toString());
			int points = pd.getPoints(p.getUniqueId().toString());
			int elo = pd.getElo(p.getUniqueId().toString());
			boolean promosEnabled = pd.isInPromo(p.getUniqueId().toString());
			int promoWins = pd.getPromoWins(p.getUniqueId().toString());
			int promoLosses = pd.getPromoLosses(p.getUniqueId().toString());
			int placementWins = pd.getPlacementWins(p.getUniqueId().toString());
			int placementLosses = pd.getPlacementLosses(p.getUniqueId().toString());
			int wins = pd.getWins(p.getUniqueId().toString());
			int losses = pd.getLosses(p.getUniqueId().toString());
			ArrayList<HashMap<String, HashMap<String, HashMap<String, ItemStack[]>>>> kits = pd
					.getKits(p.getUniqueId().toString());

			duelers.add(new Dueler(name, id, rank, tier, points, elo, promosEnabled, promoWins, promoLosses,
					placementWins, wins, losses, placementLosses, kits));

		}

	}

	public Team getTeam(Player p) {

		for (Team t : teams) {

			if (t.getMembers().contains(p.getUniqueId().toString())) {

				return t;
			}
		}
		return null;

	}

	public boolean isInTeam(Player p) {
		if (getTeam(p) != null) {
			return true;
		}
		return false;
	}

	public Dueler getDueler(Player p) {

		for (Dueler d : duelers) {

			if (d.getName().equals(p.getName())) {

				return d;
			}
		}
		return null;
	}

	public ArrayList<Dueler> getDuelers() {
		return duelers;
	}

}

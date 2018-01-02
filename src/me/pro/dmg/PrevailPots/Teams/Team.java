package me.pro.dmg.PrevailPots.Teams;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import me.pro.dmg.PrevailPots.Core.Main;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Team {

	private String name;
	private String leader;
	private List<String> members;
	private int elo;
	private String rank;
	private int tier;
	private int points;
	private boolean promosEnabled;
	private int promoWins;
	private int promoLosses;
	private int placementWins;
	private int placementLosses;
	private int losses;
	private int wins;
	private List<String> roster;

	public Team(String name, String leader, List<String> members2, String rank, int tier, int points, int elo,
			boolean promosEnabled, int promoWins, int promoLosses, int placementWins, int wins, int losses,
			int placementLosses, List<String> roster) {

		this.name = name;
		this.leader = leader;
		this.members = members2;
		this.elo = elo;
		this.rank = rank;
		this.tier = tier;
		this.points = points;
		this.promosEnabled = promosEnabled;
		this.promoWins = promoWins;
		this.promoLosses = promoLosses;
		this.placementWins = placementWins;
		this.placementLosses = placementLosses;
		this.losses = losses;
		this.wins = wins;
		this.roster = roster;

	}

	public String getName() {
		return name;
	}

	public String getLeader() {
		return this.leader;
	}

	public List<String> getMembers() {
		return this.members;
	}

	public int getElo() {
		return this.elo;
	}

	public String getRank() {
		return rank;
	}

	public int getTier() {
		return tier;
	}

	public int getPoints() {
		return points;
	}

	public boolean isInPromos() {
		return promosEnabled;
	}

	public int getWins() {
		return wins;
	}

	public int getLosses() {
		return losses;

	}

	public List<String> getRoster() {
		return roster;
	}

	public Double getKD() {

		if (losses == 0) {
			return (double) wins;
		}

		DecimalFormat df = new DecimalFormat("#.00");

		double kd = (double) wins / losses;
		String kdf = df.format(kd);
		return Double.valueOf(kdf);

	}

	public int getPromoWins() {
		return promoWins;
	}

	public int getPromoLosses() {
		return promoLosses;
	}

	public int getPlacementWins() {
		return placementWins;
	}

	public int getPlacementLosses() {
		return placementLosses;
	}

	public boolean hasFinishedPlacements() {

		if ((getPlacementWins() + getPlacementLosses()) == 10) {
			return true;
		}

		return false;

	}

	public void setLeader(String id) {
		this.leader = id;
	}

	public void addMember(String id) {

		members.add(id);
	}

	// PLACEMENTS -=-=-=-=-=-

	public void addPlacementWin() {

		placementWins++;
		wins++;
		elo -= 10;

		if (placementWins + placementLosses == 10) {
			setRankFromPlacements();
		}

	}

	public void kickMember(String id) {

		if (!id.equalsIgnoreCase(leader)) {

			members.remove(id);
		}

	}

	public void addPlacementLoss() {

		placementLosses++;
		losses++;
		elo -= 50;

		if (placementWins + placementLosses == 10) {
			setRankFromPlacements();
		}
	}

	public void setRankFromPlacements() {

		rank = "Bronze";

		if (elo >= 400) {

			tier = 1;
			sendMessage(Main.prefix + ChatColor.GREEN + "You have been placed in: " + getColouredRank() + " "
					+ ChatColor.RED + getNumeral());
			elo = 1200;
			return;

		}

		if (elo >= 300) {

			tier = 2;
			sendMessage(Main.prefix + ChatColor.GREEN + "You have been placed in: " + getColouredRank() + " "
					+ ChatColor.RED + getNumeral());
			elo = 1200;
			return;

		}

		if (elo >= 200) {

			tier = 3;
			sendMessage(Main.prefix + ChatColor.GREEN + "You have been placed in: " + getColouredRank() + " "
					+ ChatColor.RED + getNumeral());
			elo = 1200;
			return;
		}

		if (elo >= 100) {

			tier = 4;
			sendMessage(Main.prefix + ChatColor.GREEN + "You have been placed in: " + getColouredRank() + " "
					+ ChatColor.RED + getNumeral());
			elo = 1200;
			return;
		}

		if (elo >= 0) {

			tier = 5;
			sendMessage(Main.prefix + ChatColor.GREEN + "You have been placed in: " + getColouredRank() + " "
					+ ChatColor.RED + getNumeral());
			elo = 1200;
			return;
		}

	}

	public void setWin() {

		if (rank.equals("Placements")) {

			addPlacementWin();
			TextComponent message = new TextComponent(
					ChatColor.GOLD + "-== " + ChatColor.GREEN + "Match Recap" + ChatColor.GOLD + " ==-");
			message.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/p recap"));
			message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
					new ComponentBuilder(ChatColor.GOLD + "Click to view match recap").create()));

			sendMessage(ChatColor.DARK_GRAY + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
			sendMessage("");
			sendMessage(ChatColor.GREEN + "You have won the duel!");
			sendMessage("");

			sendMessage(ChatColor.GOLD + "Current Placement series status: " + ChatColor.GREEN + placementWins
					+ ChatColor.GOLD + "/" + ChatColor.RED + placementLosses + ChatColor.GOLD + "/" + ChatColor.GRAY
					+ "10");
			sendMessage("");
			sendComponentMessage(message);
			sendMessage("");

			return;

		}
		// WE CAN NOW EDIT ELO

		wins += 1;
		if (promosEnabled) {
			// if they are in promos

			if (promoWins == 2) {
				// if they have 3 wins

				promosEnabled = false;
				points = 0;
				promoWins = 0;
				promoLosses = 0;

				if (tier == 1) {
					// if they are in first tier

					if (rank.equals("Bronze")) {
						rank = "Silver";
					}

					else if (rank.equals("Silver")) {
						rank = "Gold";
					}

					else if (rank.equals("Gold")) {

						rank = "Platinum";

					}

					else if (rank.equals("Platinum")) {
						rank = "Diamond";
					}

					else if (rank.equals("Diamond")) {
						rank = "Challenger";
					}
					tier = 5;
					if (rank.equals("Challenger")) {
						tier = 1;

					}

				}

				else {
					// if they are not in first tier
					tier--;

				}

				TextComponent message = new TextComponent(
						ChatColor.GOLD + "-== " + ChatColor.GREEN + "Match Recap" + ChatColor.GOLD + " ==-");
				message.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/p recap"));
				message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
						new ComponentBuilder(ChatColor.GOLD + "Click to view match recap").create()));

				sendMessage(ChatColor.DARK_GRAY + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
				sendMessage("");
				sendMessage(ChatColor.GREEN + "You have won the duel!");
				sendMessage("");

				sendMessage(ChatColor.GREEN + "You have ranked up to: " + ChatColor.GOLD + rank + " " + ChatColor.GOLD
						+ tier);
				sendMessage("");
				sendComponentMessage(message);

				sendMessage("");

				// rankup - check if their tier is one, if so next rank, if not,
				// tier up

			}

			else {
				// if they don't have 3 wins
				promoWins++;

				TextComponent message = new TextComponent(
						ChatColor.GOLD + "-== " + ChatColor.GREEN + "Match Recap" + ChatColor.GOLD + " ==-");
				message.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/p recap"));
				message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
						new ComponentBuilder(ChatColor.GOLD + "Click to view match recap").create()));

				sendMessage(ChatColor.DARK_GRAY + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
				sendMessage("");
				sendMessage(ChatColor.GREEN + "You have won the duel!");
				sendMessage("");
				sendMessage(
						ChatColor.GOLD + "Current Promo series status: " + ChatColor.GREEN + promoWins + ChatColor.GOLD
								+ "/" + ChatColor.RED + promoLosses + ChatColor.GOLD + "/" + ChatColor.GRAY + 5);
				sendMessage("");
				sendComponentMessage(message);
				sendMessage("");

			}
		}

		else {
			// if they are not in promos just add 10 points

			TextComponent message = new TextComponent(
					ChatColor.GOLD + "-== " + ChatColor.GREEN + "Match Recap" + ChatColor.GOLD + " ==-");
			message.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/p recap"));
			message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
					new ComponentBuilder(ChatColor.GOLD + "Click to view match recap").create()));

			sendMessage(ChatColor.DARK_GRAY + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
			sendMessage("");
			sendMessage(ChatColor.GREEN + "You have won the duel!");
			sendMessage("");
			sendComponentMessage(message);

			sendMessage("");

			points += 10;

			if (rank.equals("Challenger")) {

				if (points >= 100) {
					points = 100;
				}
				return;
			}

			if (points >= 100) {
				points = 100;
				promosEnabled = true;

				sendMessage(Main.prefix + ChatColor.GREEN + "You have entered your Promo series!");
			}

		}

	}

	public void setLoss() {

		if (rank.equals("Placements")) {
			addPlacementLoss();

			TextComponent message = new TextComponent(
					ChatColor.GOLD + "-== " + ChatColor.GREEN + "Match Recap" + ChatColor.GOLD + " ==-");
			message.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/p recap"));
			message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
					new ComponentBuilder(ChatColor.GOLD + "Click to view match recap").create()));

			sendMessage(ChatColor.DARK_GRAY + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
			sendMessage("");
			sendMessage(Main.prefix + ChatColor.RED + "You have lost the duel!");
			sendMessage("");
			sendMessage(ChatColor.GOLD + "Current Placement series status: " + ChatColor.GREEN + placementWins
					+ ChatColor.GOLD + "/" + ChatColor.RED + placementLosses + ChatColor.GOLD + "/" + ChatColor.GRAY
					+ "10");
			sendMessage("");

			sendMessage("");

			return;

		}

		losses += 1;

		if (promosEnabled) {
			// if they are in promos

			promoLosses++;

			if (promoLosses >= 3) {
				// if they have played 5 promos take them out of promos and set
				// points to 60

				promosEnabled = false;

				promoWins = 0;
				promoLosses = 0;
				points = 50;

				Bukkit.getPlayer(name)
						.sendMessage(Main.prefix + ChatColor.RED + "You were unsuccessful in completeing your promos!");
				return;

			}

			TextComponent message = new TextComponent(
					ChatColor.GOLD + "-== " + ChatColor.GREEN + "Match Recap" + ChatColor.GOLD + " ==-");
			message.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/p recap"));
			message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
					new ComponentBuilder(ChatColor.GOLD + "Click to view match recap").create()));

			sendMessage(ChatColor.DARK_GRAY + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
			sendMessage("");
			sendMessage(ChatColor.RED + "You have lost the duel!");
			sendMessage("");
			sendMessage(ChatColor.GOLD + "Current Promo series status: " + ChatColor.GREEN + promoWins + ChatColor.GOLD
					+ "/" + ChatColor.RED + promoLosses + ChatColor.GOLD + "/" + ChatColor.GRAY + "5");
			sendMessage("");

			sendMessage("");

		}

		else {

			points -= 10;

			if (points < 0) {
				// if they lost while at 0 points

				if (tier == 5) {
					// if they are going to get demoted to last rank set tier to
					// 1

					tier = 1;

					if (rank.equals("Bronze")) {
						// if they are bronze 5
						rank = "Bronze";
						tier = 5;
						points = 0;

						TextComponent message = new TextComponent(
								ChatColor.GOLD + "-== " + ChatColor.GREEN + "Match Recap" + ChatColor.GOLD + " ==-");
						message.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/p recap"));
						message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
								new ComponentBuilder(ChatColor.GOLD + "Click to view match recap").create()));

						sendMessage(ChatColor.DARK_GRAY + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");

						sendMessage("");
						sendMessage(ChatColor.RED + "You have lost the duel!");
						sendMessage("");

						sendMessage("");

						return;
					}

					else if (rank.equals("Silver")) {
						rank = "Bronze";
					}

					else if (rank.equals("Gold")) {

						rank = "Silver";

					}

					else if (rank.equals("Platinum")) {
						rank = "Gold";
					}

					else if (rank.equals("Diamond")) {
						rank = "Platinum";
					}

				}

				else {
					// tier isnt 5 so - 1 from tier

					if (rank.equals("Challenger")) {

						points = 50;
						rank = "Diamond";
						tier = 1;

					}

					else {

						tier++;
						points = 50;
					}
				}

				TextComponent message = new TextComponent(
						ChatColor.GOLD + "-== " + ChatColor.GREEN + "Match Recap" + ChatColor.GOLD + " ==-");
				message.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/p recap"));
				message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
						new ComponentBuilder(ChatColor.GOLD + "Click to view match recap").create()));

				sendMessage(ChatColor.DARK_GRAY + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
				sendMessage("");
				sendMessage(ChatColor.RED + "You have lost the duel!");
				sendMessage("");
				sendMessage(ChatColor.GOLD + "You have been demoted to: " + ChatColor.RED + rank + ChatColor.GREEN
						+ getNumeral());

				return;

			}

			sendMessage(ChatColor.DARK_GRAY + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
			sendMessage("");
			sendMessage(ChatColor.RED + "You have lost the duel!");

			sendMessage("");

		}

	}

	public void setElo(int newElo) {
		elo = newElo;

	}

	public String getNumeral() {

		if (tier == 1) {
			return "I";
		}

		if (tier == 2) {
			return "II";
		}
		if (tier == 3) {
			return "III";
		}
		if (tier == 4) {
			return "IV";
		}
		if (tier == 5) {
			return "V";
		}
		return null;

	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public ChatColor getRankColour() {

		return ChatColor.getByChar(getColouredRank().charAt(4));

	}

	public String getColouredRank() {

		if (rank.equals("Placements")) {
			return ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + rank + ChatColor.DARK_GRAY + "]";
		}

		if (rank.equals("Bronze")) {
			return ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + rank + ChatColor.DARK_GRAY + "]";
		}

		if (rank.equals("Silver")) {
			return ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + rank + ChatColor.DARK_GRAY + "]";
		}

		if (rank.equals("Gold")) {
			return ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + rank + ChatColor.DARK_GRAY + "]";
		}

		if (rank.equals("Platinum")) {
			return ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + rank + ChatColor.DARK_GRAY + "]";
		}

		if (rank.equals("Diamond")) {
			return ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + rank + ChatColor.DARK_GRAY + "]";
		}

		if (rank.equals("Challenger")) {
			return ChatColor.DARK_GRAY + "[" + ChatColor.DARK_PURPLE + rank + ChatColor.DARK_GRAY + "]";
		}
		return null;

	}

	public void sendMessage(String message) {

		for (String id : this.members) {
			if (Bukkit.getPlayer(UUID.fromString(id)) != null) {
				Bukkit.getPlayer(UUID.fromString(id)).sendMessage(message);
			}
		}

	}

	public void sendComponentMessage(BaseComponent message) {
		for (String id : this.members) {
			if (Bukkit.getPlayer(id) != null) {
				Bukkit.getPlayer(id).spigot().sendMessage(message);
			}
		}
	}

	public String getLeaderName() {
		return Bukkit.getOfflinePlayer(UUID.fromString(leader)).getName();
	}

	public List<String> getMemberNames() {

		List<String> names = new ArrayList<String>();

		for (String id : members) {
			names.add(Bukkit.getOfflinePlayer(UUID.fromString(id)).getName().toLowerCase());
		}
		return names;
	}

	public void teleport(Location loc) {

		for (String id : members) {

			if (Bukkit.getPlayer(id) != null) {
				Bukkit.getPlayer(id).teleport(loc);
			}
		}
	}

	public void addMemberToRoster(String name) {

		roster.add(name);

	}

}

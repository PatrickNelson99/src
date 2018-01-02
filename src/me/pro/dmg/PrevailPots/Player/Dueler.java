package me.pro.dmg.PrevailPots.Player;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.pro.dmg.PrevailPots.Core.Cache;
import me.pro.dmg.PrevailPots.Core.Main;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ClickEvent.Action;

public class Dueler {

	Cache cache = Cache.getInstance();

	private String name;
	private String id;
	private String rank;
	private int tier;
	private int points;
	private int elo;
	private boolean promosEnabled;
	private int promoWins;
	private int promoLosses;
	private int placementWins;
	private int placementLosses;
	private int losses;
	private int wins;
	private ArrayList<HashMap<String, HashMap<String, HashMap<String, ItemStack[]>>>> kits;
	// gametypeName-> CustomKitName -> inventoryContents: itemstack[]

	public Dueler(String name, String id, String rank, int tier, int points, int elo, boolean promosEnabled,
			int promoWins, int promoLosses, int placementWins, int wins, int losses, int placementLosses,
			ArrayList<HashMap<String, HashMap<String, HashMap<String, ItemStack[]>>>> kits) {

		this.name = name;
		this.id = id;
		this.rank = rank;
		this.tier = tier;
		this.points = points;
		this.promosEnabled = promosEnabled;
		this.promoWins = promoWins;
		this.promoLosses = promoLosses;
		this.placementLosses = placementLosses;
		this.placementWins = placementWins;
		this.wins = wins;
		this.losses = losses;
		this.elo = elo;
		this.kits = kits;

	}

	public String getName() {
		return name;
	}

	public String getUUID() {

		return id;
	}

	public int getElo() {
		return elo;
	}

	public ArrayList<HashMap<String, HashMap<String, HashMap<String, ItemStack[]>>>> getKits() {
		return kits;
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

	public Boolean promosEnabled() {
		return promosEnabled;
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

	public int getWins() {
		return wins;
	}

	public int getLosses() {
		return losses;
	}

	public boolean isInTeam() {
		return cache.isInTeam(getPlayer());
	}

	public boolean hasFinishedPlacements() {

		if ((getPlacementWins() + getPlacementLosses()) == 10) {
			return true;
		}

		return false;

	}

	public void setUnrankedWin() {

		Player p = Bukkit.getPlayer(name);
		p.sendMessage(ChatColor.DARK_GRAY + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
		p.sendMessage("");
		Bukkit.getPlayer(name).sendMessage(Main.prefix + ChatColor.GREEN + "You have won the duel!");
		p.sendMessage("");
		TextComponent message = new TextComponent(
				ChatColor.GOLD + "-== " + ChatColor.GREEN + "Match Recap" + ChatColor.GOLD + " ==-");
		message.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/d recap"));
		message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new ComponentBuilder(ChatColor.GOLD + "Click to view match recap").create()));
		p.spigot().sendMessage(message);
		p.sendMessage("");

		p.sendMessage(ChatColor.DARK_GRAY + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");

	}

	public void setUnrankedLose() {

		Player p = Bukkit.getPlayer(name);
		p.sendMessage(ChatColor.DARK_GRAY + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
		p.sendMessage("");
		Bukkit.getPlayer(name).sendMessage(Main.prefix + ChatColor.RED + "You have lost the duel!");
		p.sendMessage("");
		TextComponent message = new TextComponent(
				ChatColor.GOLD + "-== " + ChatColor.GREEN + "Match Recap" + ChatColor.GOLD + " ==-");
		message.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/d recap"));
		message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new ComponentBuilder(ChatColor.GOLD + "Click to view match recap").create()));
		p.spigot().sendMessage(message);
		p.sendMessage("");

		p.sendMessage(ChatColor.DARK_GRAY + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");

	}

	public void setWin() {

		if (rank.equals("Placements")) {
			Player p = Bukkit.getPlayer(name);
			addPlacementWin();
			TextComponent message = new TextComponent(
					ChatColor.GOLD + "-== " + ChatColor.GREEN + "Match Recap" + ChatColor.GOLD + " ==-");
			message.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/d recap"));
			message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
					new ComponentBuilder(ChatColor.GOLD + "Click to view match recap").create()));

			p.sendMessage(ChatColor.DARK_GRAY + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
			p.sendMessage("");
			p.sendMessage(ChatColor.GREEN + "You have won the duel!");
			p.sendMessage("");

			p.sendMessage(ChatColor.GOLD + "Current Placement series status: " + ChatColor.GREEN + placementWins
					+ ChatColor.GOLD + "/" + ChatColor.RED + placementLosses + ChatColor.GOLD + "/" + ChatColor.GRAY
					+ "10");
			p.sendMessage("");
			p.spigot().sendMessage(message);
			p.sendMessage("");

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
					tier = 5;
					if (rank.equals("Diamond")) {
						rank = "Challenger";
						tier = 1;
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

				Player p = Bukkit.getPlayer(name);

				TextComponent message = new TextComponent(
						ChatColor.GOLD + "-== " + ChatColor.GREEN + "Match Recap" + ChatColor.GOLD + " ==-");
				message.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/d recap"));
				message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
						new ComponentBuilder(ChatColor.GOLD + "Click to view match recap").create()));

				p.sendMessage(ChatColor.DARK_GRAY + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
				p.sendMessage("");
				p.sendMessage(ChatColor.GREEN + "You have won the duel!");
				p.sendMessage("");

				p.sendMessage(ChatColor.GREEN + "You have ranked up to: " + ChatColor.GOLD + rank + " " + ChatColor.GOLD
						+ tier);
				p.sendMessage("");
				p.spigot().sendMessage(message);

				p.sendMessage("");

				// rankup - check if their tier is one, if so next rank, if not,
				// tier up

			}

			else {
				// if they don't have 3 wins
				promoWins++;

				Player p = Bukkit.getPlayer(name);

				TextComponent message = new TextComponent(
						ChatColor.GOLD + "-== " + ChatColor.GREEN + "Match Recap" + ChatColor.GOLD + " ==-");
				message.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/d recap"));
				message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
						new ComponentBuilder(ChatColor.GOLD + "Click to view match recap").create()));

				p.sendMessage(ChatColor.DARK_GRAY + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
				p.sendMessage("");
				p.sendMessage(ChatColor.GREEN + "You have won the duel!");
				p.sendMessage("");
				p.sendMessage(
						ChatColor.GOLD + "Current Promo series status: " + ChatColor.GREEN + promoWins + ChatColor.GOLD
								+ "/" + ChatColor.RED + promoLosses + ChatColor.GOLD + "/" + ChatColor.GRAY + 5);
				p.sendMessage("");
				p.spigot().sendMessage(message);
				p.sendMessage("");

			}
		}

		else {
			// if they are not in promos just add 10 points

			Player p = Bukkit.getPlayer(name);

			TextComponent message = new TextComponent(
					ChatColor.GOLD + "-== " + ChatColor.GREEN + "Match Recap" + ChatColor.GOLD + " ==-");
			message.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/d recap"));
			message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
					new ComponentBuilder(ChatColor.GOLD + "Click to view match recap").create()));

			p.sendMessage(ChatColor.DARK_GRAY + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
			p.sendMessage("");
			p.sendMessage(ChatColor.GREEN + "You have won the duel!");
			p.sendMessage("");
			p.spigot().sendMessage(message);

			p.sendMessage("");
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
				Bukkit.getPlayer(name)
						.sendMessage(Main.prefix + ChatColor.GREEN + "You have entered your Promo series!");
			}

		}

	}

	public void setLoss() {

		if (rank.equals("Placements")) {
			addPlacementLoss();

			Player p = Bukkit.getPlayer(name);

			TextComponent message = new TextComponent(
					ChatColor.GOLD + "-== " + ChatColor.GREEN + "Match Recap" + ChatColor.GOLD + " ==-");
			message.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/d recap"));
			message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
					new ComponentBuilder(ChatColor.GOLD + "Click to view match recap").create()));

			p.sendMessage(ChatColor.DARK_GRAY + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
			p.sendMessage("");
			p.sendMessage(Main.prefix + ChatColor.RED + "You have lost the duel!");
			p.sendMessage("");
			p.sendMessage(ChatColor.GOLD + "Current Placement series status: " + ChatColor.GREEN + placementWins
					+ ChatColor.GOLD + "/" + ChatColor.RED + placementLosses + ChatColor.GOLD + "/" + ChatColor.GRAY
					+ "10");
			p.sendMessage("");
			p.spigot().sendMessage(message);
			p.sendMessage("");

			return;

		}

		losses += 1;

		if (promosEnabled) {
			// if they are in promos

			promoLosses++;

			if (promoLosses >= 3) {
				// if they have lost 3 promos take them out of promos and set
				// points to 60

				promosEnabled = false;

				promoWins = 0;
				promoLosses = 0;
				points = 50;

				Bukkit.getPlayer(name)
						.sendMessage(Main.prefix + ChatColor.RED + "You were unsuccessful in completeing your promos!");
				return;

			}

			Player p = Bukkit.getPlayer(name);
			TextComponent message = new TextComponent(
					ChatColor.GOLD + "-== " + ChatColor.GREEN + "Match Recap" + ChatColor.GOLD + " ==-");
			message.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/d recap"));
			message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
					new ComponentBuilder(ChatColor.GOLD + "Click to view match recap").create()));

			p.sendMessage(ChatColor.DARK_GRAY + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
			p.sendMessage("");
			p.sendMessage(ChatColor.RED + "You have lost the duel!");
			p.sendMessage("");
			p.sendMessage(ChatColor.GOLD + "Current Promo series status: " + ChatColor.GREEN + promoWins
					+ ChatColor.GOLD + "/" + ChatColor.RED + promoLosses + ChatColor.GOLD + "/" + ChatColor.GRAY + "5");
			p.sendMessage("");
			p.spigot().sendMessage(message);
			p.sendMessage("");

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

						Player p = Bukkit.getPlayer(name);

						TextComponent message = new TextComponent(
								ChatColor.GOLD + "-== " + ChatColor.GREEN + "Match Recap" + ChatColor.GOLD + " ==-");
						message.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/d recap"));
						message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
								new ComponentBuilder(ChatColor.GOLD + "Click to view match recap").create()));

						p.sendMessage(ChatColor.DARK_GRAY + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");

						p.sendMessage("");
						p.sendMessage(ChatColor.RED + "You have lost the duel!");
						p.sendMessage("");
						p.spigot().sendMessage(message);

						p.sendMessage("");

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

				Player p = Bukkit.getPlayer(name);

				TextComponent message = new TextComponent(
						ChatColor.GOLD + "-== " + ChatColor.GREEN + "Match Recap" + ChatColor.GOLD + " ==-");
				message.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/d recap"));
				message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
						new ComponentBuilder(ChatColor.GOLD + "Click to view match recap").create()));

				p.sendMessage(ChatColor.DARK_GRAY + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
				p.sendMessage("");
				p.sendMessage(ChatColor.RED + "You have lost the duel!");
				p.sendMessage("");
				p.sendMessage(
						ChatColor.GOLD + "You have been demoted to: " + ChatColor.RED + rank + " " + getNumeral());
				p.sendMessage("");
				p.spigot().sendMessage(message);
				p.sendMessage("");

				return;

			}

			Player p = Bukkit.getPlayer(name);

			TextComponent message = new TextComponent(
					ChatColor.GOLD + "-== " + ChatColor.GREEN + "Match Recap" + ChatColor.GOLD + " ==-");
			message.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/d recap"));
			message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
					new ComponentBuilder(ChatColor.GOLD + "Click to view match recap").create()));

			p.sendMessage(ChatColor.DARK_GRAY + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
			p.sendMessage("");
			p.sendMessage(ChatColor.RED + "You have lost the duel!");
			p.sendMessage("");
			p.spigot().sendMessage(message);
			p.sendMessage("");

		}

	}

	public void setRankFromPlacements() {

		rank = "Bronze";

		if (elo >= 400) {

			tier = 1;
			Bukkit.getPlayer(name).sendMessage(Main.prefix + ChatColor.GREEN + "You have been placed in: "
					+ getColouredRank() + " " + ChatColor.RED + getNumeral());
			elo = 1200;
			return;

		}

		if (elo >= 300) {

			tier = 2;
			Bukkit.getPlayer(name).sendMessage(Main.prefix + ChatColor.GREEN + "You have been placed in: "
					+ getColouredRank() + " " + ChatColor.RED + getNumeral());
			elo = 1200;
			return;

		}

		if (elo >= 200) {

			tier = 3;
			Bukkit.getPlayer(name).sendMessage(Main.prefix + ChatColor.GREEN + "You have been placed in: "
					+ getColouredRank() + " " + ChatColor.RED + getNumeral());
			elo = 1200;
			return;
		}

		if (elo >= 100) {

			tier = 4;
			Bukkit.getPlayer(name).sendMessage(Main.prefix + ChatColor.GREEN + "You have been placed in: "
					+ getColouredRank() + " " + ChatColor.RED + getNumeral());
			elo = 1200;
			return;
		}

		if (elo >= 0) {

			tier = 5;
			Bukkit.getPlayer(name).sendMessage(Main.prefix + ChatColor.GREEN + "You have been placed in: "
					+ getColouredRank() + " " + ChatColor.RED + getNumeral());
			elo = 1200;
			return;
		}

	}

	public void addPlacementWin() {

		placementWins++;
		wins++;
		elo -= 10;

		if (placementWins + placementLosses == 10) {
			setRankFromPlacements();
		}

	}

	public Player getPlayer() {
		if (Bukkit.getPlayer(name) != null) {
			return Bukkit.getPlayer(name);
		}
		return null;
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

	public String getRankColourNoBrackets() {

		if (rank.equals("Placements")) {
			return ChatColor.GREEN + rank;
		}

		if (rank.equals("Bronze")) {
			return ChatColor.YELLOW + rank;
		}

		if (rank.equals("Silver")) {
			return ChatColor.GRAY + rank;
		}

		if (rank.equals("Gold")) {
			return ChatColor.GOLD + rank;
		}

		if (rank.equals("Platinum")) {
			return ChatColor.BLUE + rank;
		}

		if (rank.equals("Diamond")) {
			return ChatColor.DARK_AQUA + rank;
		}

		if (rank.equals("Challenger")) {
			return ChatColor.DARK_PURPLE + rank;
		}
		return null;

	}

	public void addPlacementLoss() {

		placementLosses++;
		losses++;
		elo -= 50;

		if (placementWins + placementLosses == 10) {
			setRankFromPlacements();
		}
	}

	public void teleport(Location loc) {
		if (Bukkit.getPlayer(name) != null) {
			Bukkit.getPlayer(name).teleport(loc);
		}
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

	public void setElo(int newElo) {
		elo = newElo;

	}

	public void sendMessage(String msg) {

		if (Bukkit.getPlayer(name) != null) {
			Bukkit.getPlayer(name).sendMessage(msg);
		}
	}

	public void addKit(String gameTypeName, String customKitName, ItemStack[] inventory, ItemStack[] armour) {

		// gametypeName-> CustomKitName -> inventoryContents: itemstack[]
		HashMap<String, ItemStack[]> kit = new HashMap<String, ItemStack[]>();
		kit.put("Inventory", inventory);
		kit.put("Armour", armour);

		if (kits.size() == 0) {
			kits.add(new HashMap<String, HashMap<String, HashMap<String, ItemStack[]>>>());

		}
		for (HashMap<String, HashMap<String, HashMap<String, ItemStack[]>>> k : new ArrayList<HashMap<String, HashMap<String, HashMap<String, ItemStack[]>>>>(
				kits)) {

			if (k.containsKey(gameTypeName)) {

				k.get(gameTypeName).put(customKitName, kit);

			}

			else {

				HashMap<String, HashMap<String, ItemStack[]>> k1 = new HashMap<String, HashMap<String, ItemStack[]>>();
				k1.put(customKitName, kit);

				kits.get(0).put(gameTypeName, k1);

			}

		}
	}

}

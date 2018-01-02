package me.pro.dmg.PrevailPots.Matchmaking;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import me.pro.dmg.PrevailPots.Arenas.Arena;
import me.pro.dmg.PrevailPots.Arenas.ArenaManager;
import me.pro.dmg.PrevailPots.Core.Cache;
import me.pro.dmg.PrevailPots.Core.Main;
import me.pro.dmg.PrevailPots.Core.MySQL;
import me.pro.dmg.PrevailPots.Kits.KitDataFile;
import me.pro.dmg.PrevailPots.Listeners.Join;
import me.pro.dmg.PrevailPots.Player.Dueler;
import me.pro.dmg.PrevailPots.Player.RecapData;
import me.pro.dmg.PrevailPots.Teams.Team;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class GameTypeManager implements Listener {

	KitDataFile kd = KitDataFile.getInstance();
	ArenaManager am = ArenaManager.getInstance();
	QueueManager queue = QueueManager.getInstance();
	Cache cache = Cache.getInstance();
	MySQL sql = MySQL.getInstance();

	Join join = Join.getInstance();

	private GameTypeManager() {
	}

	static GameTypeManager instance = new GameTypeManager();

	public static GameTypeManager getInstance() {
		return instance;
	}

	public HashMap<GameType, HashMap<Arena, HashSet<Dueler>>> games = new HashMap<GameType, HashMap<Arena, HashSet<Dueler>>>();
	public HashMap<String, RecapData> recap = new HashMap<String, RecapData>();
	public HashSet<String> spectators = new HashSet<String>();
	public HashMap<Dueler, GameType> kitEditing = new HashMap<Dueler, GameType>();
	public HashMap<HashSet<Dueler>, GameType> privateDuels = new HashMap<HashSet<Dueler>, GameType>();
	public HashMap<Arena, HashMap<Team, List<String>>> teamRanked = new HashMap<Arena, HashMap<Team, List<String>>>();
	public List<String> enderpearlCooldown = new ArrayList<>();

	public boolean isInTeamGame(Player p) {

		for (HashMap<Team, List<String>> game : teamRanked.values()) {

			for (List<String> name : game.values()) {

				if (name.contains(p.getName())) {

					return true;
				}
			}

		}
		return false;

	}

	public boolean teamIsInGame(Player p) {
		for (HashMap<Team, List<String>> game : teamRanked.values()) {

			for (Team t : game.keySet()) {

				if (t.getMembers().contains(p.getUniqueId().toString())) {
					return true;
				}
			}

		}
		return false;
	}

	public void killTeamPlayer(String name) {

		for (Arena a : teamRanked.keySet()) {

			for (Team loser : teamRanked.get(a).keySet()) {

				if (teamRanked.get(a).get(loser).contains(name)) {
					teamRanked.get(a).get(loser).remove(name);

					if (teamRanked.get(a).get(loser).size() == 0) {

						// end game,

						for (Team winner : teamRanked.get(a).keySet()) {

							for (String names : winner.getRoster()) {
								join.giveSpawnItems(Bukkit.getPlayer(names), true);
							}

							if (!winner.getName().equals(loser.getName())) {

								teamRanked.remove(a);
								am.arenas.put(a, false);
								winner.setWin();
								loser.setLoss();
								try {
									sql.setTeamData(winner);
									sql.setTeamData(loser);
								} catch (SQLException e) {

									e.printStackTrace();
								}

								int oneScore = winner.getElo();
								int twoScore = loser.getElo();

								double matchOutcome = 1;
								// if win, matchOutcome = 1
								// if lose, matchOutcome = 0
								int weightingFactor = 50;

								double expectedOneScore = 1 / (1 + Math.pow(10, ((twoScore - oneScore) / 400)));

								int newEloChange = (int) (oneScore
										+ Math.round(weightingFactor * (matchOutcome - expectedOneScore)));

								if (winner.hasFinishedPlacements() && loser.hasFinishedPlacements()) {

									winner.setElo(newEloChange);
									matchOutcome = 0;
									newEloChange = (int) (oneScore
											+ Math.round(weightingFactor * (matchOutcome - expectedOneScore)));

									loser.setElo(newEloChange);

									TextComponent message = new TextComponent(ChatColor.GOLD + "-== " + ChatColor.GREEN
											+ "Elo Changes" + ChatColor.GOLD + " ==-");
									message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
											new ComponentBuilder(ChatColor.GREEN + "New Elo:" + '\n' + ChatColor.GOLD
													+ winner.getName() + ChatColor.DARK_GRAY + ": " + ChatColor.RED
													+ String.valueOf(winner.getElo()) + '\n' + ChatColor.GOLD
													+ loser.getName() + ChatColor.DARK_GRAY + ": " + ChatColor.RED
													+ String.valueOf(loser.getElo()) + '\n' + "" + '\n' + ChatColor.GOLD
													+ winner.getName() + ChatColor.DARK_GRAY + " -> "
													+ ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "+"
													+ String.valueOf(Math.abs(oneScore - newEloChange))
													+ ChatColor.DARK_GRAY + "]" + '\n' + ChatColor.GOLD
													+ loser.getName() + ChatColor.DARK_GRAY + " -> "
													+ ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-"
													+ String.valueOf(Math.abs(twoScore - newEloChange))
													+ ChatColor.DARK_GRAY + "]").create()));

									winner.sendComponentMessage(message);
									winner.sendMessage("");
									winner.sendMessage(ChatColor.DARK_GRAY + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");

									loser.sendComponentMessage(message);
									loser.sendMessage("");
									loser.sendMessage(ChatColor.DARK_GRAY + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");

								}

								else {
									winner.sendMessage(Main.prefix + ChatColor.GOLD + "No elo change " + ChatColor.RED
											+ "(Dueler in placements)");
									loser.sendMessage(Main.prefix + ChatColor.GOLD + "No elo change " + ChatColor.RED
											+ "(Dueler in placements)");
								}

								return;

							}

						}

					}

				}

				// remove from lives list

			}
		}
	}

	public boolean isInOneGame(Dueler d) {

		for (HashMap<Arena, HashSet<Dueler>> game : games.values()) {

			for (HashSet<Dueler> d1 : game.values()) {

				if (d1.contains(d)) {
					return true;
				}

			}

		}
		return false;

	}

	public boolean isEditingKit(Dueler d) {

		if (kitEditing.containsKey(d)) {
			return true;
		}

		return false;

	}

	public HashSet<Dueler> getAscociatedSet(Dueler d) {

		for (HashSet<Dueler> duelers : privateDuels.keySet()) {

			if (duelers.contains(d)) {
				return duelers;
			}

		}
		return null;

	}

	public boolean inInPrivate(Dueler d) {

		if (getAscociatedSet(d) != null) {
			return true;
		}
		return false;
	}

	public void inviteToDuel(Dueler inviter, Dueler invited, GameType gt) {

		HashSet<Dueler> duelers = new HashSet<Dueler>();
		duelers.add(invited);
		duelers.add(inviter);

		privateDuels.put(duelers, gt);

	}

	public GameType getEditingGameType(Dueler d) {

		if (isEditingKit(d)) {
			return kitEditing.get(d);
		}
		return null;

	}

	public void setEditingKit(Dueler d, GameType gt) {

		kitEditing.put(d, gt);
	}

	public void openKitEditor(Dueler d) {

		GameType gt = getEditingGameType(d);

		Inventory inv = Bukkit.createInventory(null, 54, ChatColor.BOLD + "Editor");
		int ap = 3;
		for (ItemStack armour : gt.getArmourContents()) {

			inv.setItem(ap, armour);
			ap--;

		}

		int ii = 18;
		for (ItemStack is : gt.getInventoryContents()) {

			inv.setItem(ii, is);
			ii++;
		}

		ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
		ItemMeta meta = glass.getItemMeta();
		meta.setDisplayName(Main.prefix);
		glass.setItemMeta(meta);

		for (int i = 9; i < 18; i++) {

			inv.setItem(i, glass);

		}

		d.getPlayer().openInventory(inv);

	}

	public void stopEditingKit(Dueler d) {

		if (isEditingKit(d)) {

			// set contents

		}

	}

	public void setup() {

		ConfigurationSection c = kd.getData().getConfigurationSection("Kits");

		if (c != null) {

			for (String key : c.getKeys(false)) {

				String name = kd.getData().getString("Kits." + key + ".Name");
				ItemStack[] inventoryContents = kd.getData().getList("Kits." + key + ".InventoryContents")
						.toArray(new ItemStack[0]);

				ItemStack[] armourContents = kd.getData().getList("Kits." + key + ".ArmourContents")
						.toArray(new ItemStack[0]);

				GameType gm = new GameType(name, inventoryContents, armourContents);

				games.put(gm, new HashMap<Arena, HashSet<Dueler>>());

			}

		}

	}

	public void addNewGame(GameType gt, Dueler firstDueler, Dueler secondDueler) {

		Arena a = am.getEmptyArena();
		am.arenas.put(a, true);
		HashSet<Dueler> d = new HashSet<Dueler>();
		d.add(firstDueler);
		d.add(secondDueler);

		games.get(gt).put(a, d);

		firstDueler.teleport(a.getBlueSpawn());
		secondDueler.teleport(a.getRedSpawn());

		giveBooks(gt, firstDueler);
		giveBooks(gt, secondDueler);

		final String nameOne = firstDueler.getName();
		final String nameTwo = secondDueler.getName();

		enderpearlCooldown.add(nameOne);
		enderpearlCooldown.add(nameTwo);
		firstDueler.sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&eStarting duel against " + secondDueler.getColouredRank() + "&8[&7" + secondDueler.getNumeral()
						+ "&8] &r" + secondDueler.getPlayer().getPlayerListName()));

		secondDueler.sendMessage(
				ChatColor.translateAlternateColorCodes('&', "&eStarting duel against " + firstDueler.getColouredRank()
						+ "&8[&7" + firstDueler.getNumeral() + "&8] &r" + firstDueler.getPlayer().getPlayerListName()));

		new BukkitRunnable() {
			public void run() {

				enderpearlCooldown.remove(nameOne);
				enderpearlCooldown.remove(nameTwo);

			}
		}.runTaskLater(Main.getPlugin(), 20 * Main.getPlugin().getConfig().getInt("Enderpearl-Cooldown"));

	}

	public void addTeamGame(GameType gt, Team teamOne, Team teamTwo) {

		Arena a = am.getEmptyArena();
		am.arenas.put(a, true);
		HashMap<Team, List<String>> game = new HashMap<Team, List<String>>();
		game.put(teamOne, new ArrayList<String>(teamOne.getRoster()));
		game.put(teamTwo, new ArrayList<String>(teamTwo.getRoster()));

		final List<String> names = new ArrayList<>();

		for (String name : teamOne.getRoster()) {

			Player p = Bukkit.getPlayer(name);
			Dueler d = cache.getDueler(p);
			giveBooks(gt, d);
			p.teleport(a.getBlueSpawn());
			names.add(p.getName());

		}

		for (String name : teamTwo.getRoster()) {

			Player p = Bukkit.getPlayer(name);
			Dueler d = cache.getDueler(p);
			p.teleport(a.getRedSpawn());
			giveBooks(gt, d);

			names.add(p.getName());

		}

		enderpearlCooldown.addAll(names);

		new BukkitRunnable() {
			public void run() {

				for (String name : names) {

					enderpearlCooldown.remove(name);

				}

			}
		}.runTaskLater(Main.getPlugin(), 20 * Main.getPlugin().getConfig().getInt("Enderpearl-Cooldown"));

		teamRanked.put(a, game);

	}

	public void giveBooks(GameType gt, Dueler d) {

		Player p = d.getPlayer();
		p.getInventory().clear();
		p.updateInventory();
		ItemStack book = new ItemStack(Material.BOOK, 1);
		ItemMeta bookMeta = book.getItemMeta();
		bookMeta.setDisplayName(ChatColor.GOLD + "Default");
		book.setItemMeta(bookMeta);

		p.getInventory().addItem(book);

		for (HashMap<String, HashMap<String, HashMap<String, ItemStack[]>>> kits : d.getKits()) {

			for (String gameTypeName : kits.keySet()) {

				if (gameTypeName.equals(gt.getName())) {

					for (String customName : kits.get(gameTypeName).keySet()) {

						bookMeta.setDisplayName(ChatColor.GOLD + "Custom Kit: " + ChatColor.GREEN + customName);
						book.setItemMeta(bookMeta);

						p.getInventory().addItem(book);

					}

				}

			}

		}
		p.updateInventory();
	}

	public GameType getGameTypeOfPlayer(Dueler d) {

		for (GameType gt : games.keySet()) {

			for (HashSet<Dueler> duelers : games.get(gt).values()) {
				if (duelers.contains(d)) {
					return gt;
				}
			}

		}
		return null;

	}

	public void removeGame(Dueler winner, Dueler loser) {

		Player winnerPlayer = winner.getPlayer();
		Player loserPlayer = loser.getPlayer();

		recap.put(winnerPlayer.getName(), new RecapData(loser.getName(),
				loserPlayer.getInventory().getContents().clone(), loserPlayer.getInventory().getArmorContents().clone(),
				new ArrayList<PotionEffect>(loserPlayer.getActivePotionEffects()), 0.0, loserPlayer.getFoodLevel()));

		recap.put(loserPlayer.getName(),
				new RecapData(winner.getName(), winnerPlayer.getInventory().getContents().clone(),
						winnerPlayer.getInventory().getArmorContents().clone(),
						new ArrayList<PotionEffect>(winnerPlayer.getActivePotionEffects()),
						((Damageable) winnerPlayer).getHealth(), winnerPlayer.getFoodLevel()));

		Iterator<HashMap<Arena, HashSet<Dueler>>> gameValues = games.values().iterator();

		while (gameValues.hasNext()) {

			HashMap<Arena, HashSet<Dueler>> gameInGames = gameValues.next();

			for (Arena a : new ArrayList<Arena>(gameInGames.keySet())) {

				if (gameInGames.get(a).contains(winner)) {

					am.arenas.put(a, false);

					for (GameType gt : games.keySet()) {
						if (games.get(gt) == gameInGames) {

							games.get(gt).remove(a);

							if (gt.getName().equalsIgnoreCase("Ranked")) {

								winner.setWin();
								loser.setLoss();

								final int oneScore = winner.getElo();
								final int twoScore = loser.getElo();

								double matchOutcome = 1;
								// if win, matchOutcome = 1
								// if lose, matchOutcome = 0
								int weightingFactor = 50;

								double expectedOneScore = 1 / (1 + Math.pow(10, ((twoScore - oneScore) / 400)));
								double expectedTwoScore = 1 / (1 + Math.pow(10, ((oneScore - twoScore) / 400)));

								int newWinnerEloChange = (int) (oneScore
										+ Math.round(weightingFactor * (matchOutcome - expectedOneScore)));

								matchOutcome = 0;
								int newLoserEloChange = (int) (twoScore
										+ Math.round(weightingFactor * (matchOutcome - expectedTwoScore)));

								if (winner.hasFinishedPlacements() && loser.hasFinishedPlacements()) {

									winner.setElo(newWinnerEloChange);

									loser.setElo(newLoserEloChange);

									TextComponent message = new TextComponent(ChatColor.GOLD + "-== " + ChatColor.GREEN
											+ "Elo Changes" + ChatColor.GOLD + " ==-");
									message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
											new ComponentBuilder(ChatColor.GREEN + "New Elo:" + '\n' + ChatColor.GOLD
													+ winner.getName() + ChatColor.DARK_GRAY + ": " + ChatColor.RED
													+ String.valueOf(winner.getElo()) + '\n' + ChatColor.GOLD
													+ loser.getName() + ChatColor.DARK_GRAY + ": " + ChatColor.RED
													+ String.valueOf(loser.getElo()) + '\n' + "" + '\n' + ChatColor.GOLD
													+ winner.getName() + ChatColor.DARK_GRAY + " -> "
													+ ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "+"
													+ String.valueOf(Math.abs(oneScore - newWinnerEloChange))
													+ ChatColor.DARK_GRAY + "]" + '\n' + ChatColor.GOLD
													+ loser.getName() + ChatColor.DARK_GRAY + " -> "
													+ ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-"
													+ String.valueOf(Math.abs(twoScore - newLoserEloChange))
													+ ChatColor.DARK_GRAY + "]").create()));

									winnerPlayer.spigot().sendMessage(message);
									winnerPlayer.sendMessage("");
									winnerPlayer
											.sendMessage(ChatColor.DARK_GRAY + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");

									loserPlayer.spigot().sendMessage(message);
									loserPlayer.sendMessage("");
									loserPlayer
											.sendMessage(ChatColor.DARK_GRAY + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");

								}

								else {
									winnerPlayer.sendMessage(Main.prefix + ChatColor.GOLD + "No elo change "
											+ ChatColor.RED + "(Dueler in placements)");
									loserPlayer.sendMessage(Main.prefix + ChatColor.GOLD + "No elo change "
											+ ChatColor.RED + "(Dueler in placements)");
								}
							}

							else {
								winner.setUnrankedWin();
								loser.setUnrankedLose();
							}

						}
					}

					join.giveSpawnItems(winner.getPlayer(), true);
					join.giveSpawnItems(loser.getPlayer(), true);
					join.updateScoreboard(winner);
					join.updateScoreboard(loser);

				}

			}

		}

		// [Arena 1, duelers] , [Arena 2, duelers]

	}

	public void getRecap(Player p) {

		Inventory inv = Bukkit.createInventory(null, 54, ChatColor.BOLD + "Recap");

		for (String name : recap.keySet()) {

			if (name.equals(p.getName())) {

				RecapData rd = recap.get(p.getName());

				for (ItemStack contents : rd.getInventory()) {

					if (contents != null) {

						inv.addItem(contents);
					}

					else {
						inv.addItem(new ItemStack(Material.AIR));
					}
				}

				int armourSlot = 49;
				for (ItemStack a : rd.getArmour()) {
					inv.setItem(armourSlot, a);
					armourSlot--;
				}

				inv.setItem(51, rd.getHealth());
				inv.setItem(52, rd.getFood());
				inv.setItem(53, rd.getPotions());
				p.openInventory(inv);
				return;

			}

		}
		p.sendMessage(Main.prefix + ChatColor.RED + "You don't have any active recaps!");

	}

}

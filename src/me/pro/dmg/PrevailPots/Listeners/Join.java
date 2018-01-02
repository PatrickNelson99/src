package me.pro.dmg.PrevailPots.Listeners;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import me.pro.dmg.PrevailPots.Arenas.ArenaDataFile;
import me.pro.dmg.PrevailPots.Core.Cache;
import me.pro.dmg.PrevailPots.Core.MySQL;
import me.pro.dmg.PrevailPots.Player.Dueler;
import me.pro.dmg.PrevailPots.Player.PlayerDataFile;
import me.pro.dmg.PrevailPots.Teams.Team;

public class Join implements Listener {

	PlayerDataFile pd = PlayerDataFile.getInstance();
	ArenaDataFile ad = ArenaDataFile.getInstance();
	Cache cache = Cache.getInstance();
	MySQL sql = MySQL.getInstance();

	private Join() {
	}

	static Join instance = new Join();

	public static Join getInstance() {
		return instance;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {

		Player p = e.getPlayer();

		pd.addFile(p);
		FileConfiguration c = pd.getData(e.getPlayer().getUniqueId().toString());
		pd.saveData(p.getUniqueId().toString(), c);

		cache.addDueler(p);
		cache.addTeam(p);
		String teamName;

		if (cache.getTeam(p) != null) {

			Team t = cache.getTeam(p);
			teamName = t.getName();

		}

		else {
			teamName = "0";
		}

		giveSpawnItems(p, true);

		for (Player o : Bukkit.getOnlinePlayers()) {

			if (o.getName() != p.getName()) {
				updateScoreboard(cache.getDueler(o));
			}
		}

		giveScoreBoard(cache.getDueler(p));
		try {
			sql.setData(cache.getDueler(e.getPlayer()), teamName);
		} catch (SQLException e1) {

			e1.printStackTrace();
		}

	}

	@SuppressWarnings("deprecation")
	public void giveSpawnItems(Player p, boolean tp) {

		p.setGameMode(GameMode.SURVIVAL);
		p.setAllowFlight(false);
		for (Player o : Bukkit.getOnlinePlayers()) {
			o.showPlayer(p);
		}

		p.getInventory().clear();
		p.getInventory().setHelmet(new ItemStack(Material.AIR));
		p.getInventory().setChestplate(new ItemStack(Material.AIR));
		p.getInventory().setLeggings(new ItemStack(Material.AIR));
		p.getInventory().setBoots(new ItemStack(Material.AIR));

		p.setHealth(20);
		p.setFoodLevel(20);
		
		p.setFireTicks(0);
		for (PotionEffect effect : p.getActivePotionEffects()) {
			p.removePotionEffect(effect.getType());

		}

		if (tp) {

			World w = Bukkit.getWorld(ad.getData().getString("Locations.Spawn.World"));
			double x = ad.getData().getDouble("Locations.Spawn.X");
			double y = ad.getData().getDouble("Locations.Spawn.Y");
			double z = ad.getData().getDouble("Locations.Spawn.Z");
			double pitch = ad.getData().getDouble("Locations.Spawn.Pitch");
			double yaw = ad.getData().getDouble("Locations.Spawn.Yaw");

			Location spawn = new Location(w, x, y, z, (float) yaw, (float) pitch);

			p.teleport(spawn);

		}

		ItemStack book = new ItemStack(Material.ENCHANTED_BOOK, 1);
		ItemStack goldSword = new ItemStack(Material.GOLD_SWORD, 1);
		ItemStack diamondSword = new ItemStack(Material.DIAMOND_SWORD, 1);
		ItemStack info = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);

		ItemMeta bookMeta = book.getItemMeta();

		ItemMeta goldMeta = goldSword.getItemMeta();
		ItemMeta diamondMeta = diamondSword.getItemMeta();
		SkullMeta skullMeta = (SkullMeta) info.getItemMeta();
		ItemStack teamItem = new ItemStack(Material.NETHER_STAR, 1);
		ItemMeta teamMeta = teamItem.getItemMeta();
		teamMeta.setDisplayName(ChatColor.RED + "2v2 Ranked");
		teamItem.setItemMeta(teamMeta);

		skullMeta.setOwner(p.getName());
		skullMeta.setDisplayName(ChatColor.RED + "" + ChatColor.MAGIC + "I" + ChatColor.AQUA + " Stats " + ChatColor.RED
				+ "" + ChatColor.MAGIC + "I");

		bookMeta.setDisplayName(ChatColor.GOLD + "Kit Editor");
		goldMeta.setDisplayName(ChatColor.GREEN + "Unranked");
		diamondMeta.setDisplayName(ChatColor.RED + "Ranked");
		goldMeta.spigot().setUnbreakable(true);
		diamondMeta.spigot().setUnbreakable(true);

		book.setItemMeta(bookMeta);
		goldSword.setItemMeta(goldMeta);
		diamondSword.setItemMeta(diamondMeta);

		info.setItemMeta(skullMeta);

		p.getInventory().setItem(0, book);
		p.getInventory().setItem(8, teamItem);
		p.getInventory().setItem(6, goldSword);
		p.getInventory().setItem(7, diamondSword);
		p.getInventory().setItem(4, info);
		p.updateInventory();

	}

	@SuppressWarnings("deprecation")
	public void giveScoreBoard(Dueler d) {

		Scoreboard sb = Bukkit.getServer().getScoreboardManager().getNewScoreboard();

		Objective o = sb.registerNewObjective("Prevail", "dummy");
		o.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "" + "Prevail Pots");
		o.setDisplaySlot(DisplaySlot.SIDEBAR);

		Score spacerScore = o.getScore(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "------------");
		Score spacerScore0 = o.getScore(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "----------");
		Score spacerScore2 = o.getScore(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "---------");
		Score spacerScore3 = o.getScore(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "--------");
		Score spacerScore4 = o.getScore(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "-------");
		Score spacerScore5 = o.getScore(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "------");

		Score rankScore = o.getScore(ChatColor.GREEN + d.getRankColourNoBrackets());
		Score pointScore = o.getScore(ChatColor.RED + "Points: " + ChatColor.GRAY + d.getPoints());
		Score tierScore = o.getScore(ChatColor.RED + "Tier: " + ChatColor.GRAY + d.getNumeral());

		Score eloScore;

		if (d.hasFinishedPlacements()) {
			eloScore = o.getScore(ChatColor.RED + "Elo: " + ChatColor.GRAY + d.getElo());
		}

		else {
			eloScore = o.getScore(ChatColor.RED + "Elo: " + ChatColor.GRAY + 0);
		}

		Score winScore = o.getScore(ChatColor.RED + "Wins: " + ChatColor.GRAY + d.getWins());
		Score loseScore = o.getScore(ChatColor.RED + "Losses: " + ChatColor.GRAY + d.getLosses());
		Score kdScore = o.getScore(ChatColor.RED + "W/L: " + ChatColor.GRAY + d.getKD());

		Score onlineScore = o
				.getScore(ChatColor.RED + "Online: " + ChatColor.GRAY + Bukkit.getServer().getOnlinePlayers().length);

		spacerScore.setScore(14);

		rankScore.setScore(13);
		spacerScore0.setScore(12);
		tierScore.setScore(11);
		pointScore.setScore(10);
		spacerScore2.setScore(9);
		winScore.setScore(8);
		loseScore.setScore(7);
		kdScore.setScore(6);
		spacerScore3.setScore(5);
		onlineScore.setScore(4);

		spacerScore4.setScore(2);
		eloScore.setScore(1);
		spacerScore5.setScore(0);

		d.getPlayer().setScoreboard(sb);

	}

	@SuppressWarnings("deprecation")
	public void updateScoreboard(Dueler d) {
		Player p = d.getPlayer();

		Scoreboard sb = p.getScoreboard();

		Objective og = sb.getObjective("Prevail");
		og.unregister();

		Objective o = sb.registerNewObjective("Prevail", "dummy");

		o.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "" + "Prevail Pots");
		o.setDisplaySlot(DisplaySlot.SIDEBAR);
		Score spacerScore = o.getScore(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "------------");
		Score spacerScore0 = o.getScore(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "-----------");
		Score spacerScore2 = o.getScore(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "----------");
		Score spacerScore3 = o.getScore(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "---------");
		Score spacerScore4 = o.getScore(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "--------");
		Score spacerScore5 = o.getScore(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "-------");
		Score rankScore = o.getScore(ChatColor.GREEN + d.getRankColourNoBrackets());
		Score pointScore = o.getScore(ChatColor.RED + "Points: " + ChatColor.GRAY + d.getPoints());
		Score tierScore = o.getScore(ChatColor.RED + "Tier: " + ChatColor.GRAY + d.getNumeral());

		Score eloScore;

		if (d.hasFinishedPlacements()) {
			eloScore = o.getScore(ChatColor.RED + "Elo: " + ChatColor.GRAY + d.getElo());
		}

		else {
			eloScore = o.getScore(ChatColor.RED + "Elo: " + ChatColor.GRAY + 0);
		}

		Score winScore = o.getScore(ChatColor.RED + "Wins: " + ChatColor.GRAY + d.getWins());
		Score loseScore = o.getScore(ChatColor.RED + "Losses: " + ChatColor.GRAY + d.getLosses());
		Score kdScore = o.getScore(ChatColor.RED + "W/L: " + ChatColor.GRAY + d.getKD());

		Score onlineScore = o
				.getScore(ChatColor.RED + "Online: " + ChatColor.GRAY + Bukkit.getServer().getOnlinePlayers().length);

		spacerScore.setScore(14);

		rankScore.setScore(13);
		spacerScore0.setScore(12);
		tierScore.setScore(11);
		pointScore.setScore(10);
		spacerScore2.setScore(9);
		winScore.setScore(8);
		loseScore.setScore(7);
		kdScore.setScore(6);
		spacerScore3.setScore(5);
		onlineScore.setScore(4);
		spacerScore4.setScore(2);
		eloScore.setScore(1);
		spacerScore5.setScore(0);

		p.setScoreboard(sb);

	}

}

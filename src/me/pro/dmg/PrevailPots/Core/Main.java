package me.pro.dmg.PrevailPots.Core;

import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.pro.dmg.PrevailPots.Arenas.ArenaDataFile;
import me.pro.dmg.PrevailPots.Arenas.ArenaManager;
import me.pro.dmg.PrevailPots.Kits.KitDataFile;
import me.pro.dmg.PrevailPots.Listeners.BookClick;
import me.pro.dmg.PrevailPots.Listeners.Death;
import me.pro.dmg.PrevailPots.Listeners.DuelInventory;
import me.pro.dmg.PrevailPots.Listeners.Join;
import me.pro.dmg.PrevailPots.Listeners.KitAnvilClick;
import me.pro.dmg.PrevailPots.Listeners.KitChestClick;
import me.pro.dmg.PrevailPots.Listeners.KitLocationTeleport;
import me.pro.dmg.PrevailPots.Listeners.LeaveQueue;
import me.pro.dmg.PrevailPots.Listeners.Misc;
import me.pro.dmg.PrevailPots.Listeners.OnTeamKill;
import me.pro.dmg.PrevailPots.Listeners.OneKill;
import me.pro.dmg.PrevailPots.Listeners.RankedInventory;
import me.pro.dmg.PrevailPots.Listeners.ReturnToSpawnSign;
import me.pro.dmg.PrevailPots.Listeners.SpawnSign;
import me.pro.dmg.PrevailPots.Listeners.Stats;
import me.pro.dmg.PrevailPots.Listeners.UnrankedInventory;
import me.pro.dmg.PrevailPots.Matchmaking.GameTypeManager;
import me.pro.dmg.PrevailPots.Matchmaking.PrivateDuel;
import me.pro.dmg.PrevailPots.Matchmaking.QueueManager;
import me.pro.dmg.PrevailPots.Matchmaking.TeamQueueManager;
import me.pro.dmg.PrevailPots.Player.PlayerDataFile;
import me.pro.dmg.PrevailPots.Teams.TeamDataFile;
import me.pro.dmg.PrevailPots.Utils.ArenasCommand;
import me.pro.dmg.PrevailPots.Utils.GameTypesCommand;

public class Main extends JavaPlugin {

	private static Plugin plugin;
	ArenaDataFile ad = ArenaDataFile.getInstance();
	KitDataFile kd = KitDataFile.getInstance();
	PlayerDataFile pd = PlayerDataFile.getInstance();
	QueueManager queue = QueueManager.getInstance();
	GameTypeManager gt = GameTypeManager.getInstance();
	Cache cache = Cache.getInstance();
	TeamDataFile td = TeamDataFile.getInstance();
	MySQL sql = MySQL.getInstance();
	TeamQueueManager tq = TeamQueueManager.getInstance();
	PrivateDuel prd = PrivateDuel.getInstance();
	public static String prefix;

	@Override
	public void onEnable() {

		plugin = this;
		saveDefaultConfig();
		prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Prefix"));

		pd.setup(this);
		ad.setup(this);
		kd.setup(this);
		td.setup(this);

		gt.setup();
		queue.setup();

		AdminCommandManager acm = new AdminCommandManager();
		acm.setup();
		getCommand("aprevail").setExecutor(acm);
		PlayerCommandManager pcm = new PlayerCommandManager();
		pcm.setup();
		getCommand("duel").setExecutor(pcm);
		TeamCommandManager tcm = new TeamCommandManager();
		tcm.setup();
		getCommand("team").setExecutor(tcm);
		ArenaManager.getInstance().setup();
		getCommand("arenas").setExecutor(new ArenasCommand());
		getCommand("gametypes").setExecutor(new GameTypesCommand());
		PluginManager pm = Bukkit.getPluginManager();

		ArrayList<Listener> l = new ArrayList<Listener>();
		l.add(Join.getInstance());
		l.add(new RankedInventory());
		l.add(new UnrankedInventory());
		l.add(new Stats());
		l.add(cache);
		l.add(new Misc());
		l.add(new OneKill());
		l.add(new LeaveQueue());
		l.add(new BookClick());
		l.add(new KitLocationTeleport());
		l.add(new KitChestClick());
		l.add(new KitAnvilClick());
		l.add(new ReturnToSpawnSign());
		l.add(new SpawnSign());
		l.add(new DuelInventory());
		l.add(new Death());
		l.add(gt);
		l.add(new OnTeamKill());
		l.add(tq);
		l.add(prd);

		for (Listener li : l) {
			pm.registerEvents(li, this);
		}
		try {
			sql.openConnection();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cache.saveData();

	}

	@Override
	public void onDisable() {
		sql.closeConnection();
	}

	public static Plugin getPlugin() {
		return plugin;

	}

}

package me.pro.dmg.PrevailPots.Core;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.pro.dmg.PrevailPots.Matchmaking.DuelAcceptCommand;
import me.pro.dmg.PrevailPots.Matchmaking.GameTypeManager;
import me.pro.dmg.PrevailPots.Matchmaking.MatchRecapCommand;
import me.pro.dmg.PrevailPots.Matchmaking.PrivateDuel;
import me.pro.dmg.PrevailPots.Matchmaking.QueueManager;
import me.pro.dmg.PrevailPots.Player.Dueler;

public class PlayerCommandManager implements CommandExecutor {

	String prefix = Main.prefix;
	private ArrayList<PlayerSubCommand> playerCommands = new ArrayList<PlayerSubCommand>();
	PrivateDuel pd = PrivateDuel.getInstance();
	GameTypeManager gt = GameTypeManager.getInstance();
	Cache cache = Cache.getInstance();
	QueueManager queue = QueueManager.getInstance();

	public void setup() {

		playerCommands.add(new DuelAcceptCommand());
		playerCommands.add(new MatchRecapCommand());

	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player)) {
			return true;
		}
		Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("duel")) {

			Dueler d = cache.getDueler(p);

			if (args.length == 0) {

				p.sendMessage(prefix + ChatColor.GOLD + "/duel <name>" + ChatColor.DARK_GRAY + " - " + ChatColor.RED
						+ "Request a duel");

				for (PlayerSubCommand c : playerCommands) {

					p.sendMessage(prefix + ChatColor.GOLD + "/duel " + c.name() + ChatColor.DARK_GRAY + " - "
							+ ChatColor.RED + c.info());

				}
				return true;
			}

			if (gt.isEditingKit(d)) {
				p.sendMessage(Main.prefix + ChatColor.RED + "You can't duel while editing a kit!");
				return true;
			}

			if (queue.isInQueue(d)) {
				p.sendMessage(Main.prefix + ChatColor.RED + "You can't private duel while in queue!");
				return true;
			}

			if (gt.isInOneGame(d)) {
				p.sendMessage(Main.prefix + ChatColor.RED + "You can't private duel while in a duel!");
				return true;
			}

			if (gt.isInTeamGame(p)) {
				p.sendMessage(Main.prefix + ChatColor.RED + "You can't prviate duel while in a duel");
				return true;
			}
			
			

			for (Player pl : Bukkit.getOnlinePlayers()) {

				if (args[0].equalsIgnoreCase(pl.getName())) {

					if (args[0].equals(p.getName())) {
						p.sendMessage(Main.prefix + ChatColor.GOLD + "You can't duel yourself!");
						return true;
					}

					pd.openInventory(p, args[0]);
					return true;

				}

			}

			PlayerSubCommand target = get(args[0]);

			if (target == null) {

				p.sendMessage(prefix + ChatColor.RED + "/duel " + args[0] + " is not a valid command!");
				return true;
			}

			ArrayList<String> a = new ArrayList<String>();
			a.addAll(Arrays.asList(args));
			a.remove(0);
			args = a.toArray(new String[a.size()]);

			try {
				target.onCommand(p, args);
			} catch (Exception e) {

				p.sendMessage(prefix + "An error has occured: " + e.getCause());
				e.printStackTrace();
			}

		}
		return true;

	}

	private PlayerSubCommand get(String name) {

		for (PlayerSubCommand cmd : playerCommands) {

			if (cmd.name().equalsIgnoreCase(name))
				return cmd;

		}
		return null;

	}

}

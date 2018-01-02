package me.pro.dmg.PrevailPots.Teams;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.pro.dmg.PrevailPots.Core.Cache;
import me.pro.dmg.PrevailPots.Core.Main;
import me.pro.dmg.PrevailPots.Core.TeamSubCommand;

public class Invite extends TeamSubCommand {

	Cache cache = Cache.getInstance();

	@Override
	public void onCommand(final Player p, String[] args) {

		if (args.length == 0) {
			p.sendMessage(Main.prefix + ChatColor.RED + "Please specify a player to invite!");
			return;

		}

		if (Bukkit.getPlayer(args[0]) == null) {
			p.sendMessage(Main.prefix + ChatColor.GOLD + "Can't find player: " + ChatColor.RED + args[0]);
			return;
		}

		if (!cache.isInTeam(p)) {
			p.sendMessage(Main.prefix + ChatColor.RED + "You must be in a team to use this command!");
			return;

		}

		if (cache.teamInvites.containsKey(args[0])) {
			p.sendMessage(
					Main.prefix + ChatColor.RED + args[0] + ChatColor.GOLD + " has already been invited to a team!");
			return;
		}

	

		final Team t = cache.getTeam(p);

		if (t.getMemberNames().size() == 7) {
			p.sendMessage(Main.prefix + ChatColor.GOLD + "There can only be a maximum of " + ChatColor.RED + "7 "
					+ ChatColor.GOLD + "players in a team!");
			return;
		}

		final Player inv = Bukkit.getPlayer(args[0]);

		if (cache.isInTeam(inv)) {
			p.sendMessage(Main.prefix + ChatColor.RED + args[0] + ChatColor.GOLD + " is already in a team!");
			return;
		}

		if (!cache.getDueler(inv).hasFinishedPlacements()) {
			p.sendMessage(
					Main.prefix + ChatColor.RED + args[0] + ChatColor.GOLD + " has not finished their placements yet!");
			return;
		}

		cache.teamInvites.put(inv.getName(), t);
		t.sendMessage(Main.prefix + ChatColor.GREEN + p.getName() + ChatColor.GOLD + " has invited " + ChatColor.GREEN
				+ args[0] + ChatColor.GOLD + " to your team!");

		inv.sendMessage(Main.prefix + ChatColor.GOLD + "You have been invited to " + ChatColor.GREEN + t.getName()
				+ ChatColor.GOLD + ". Invitation expires in " + ChatColor.RED + "30" + ChatColor.GOLD
				+ " seconds! Type " + ChatColor.GREEN + "/team join " + ChatColor.GOLD + "to join!");

		new BukkitRunnable() {
			public void run() {

				if (cache.teamInvites.containsKey(inv.getName())) {

					cache.teamInvites.remove(inv.getName());
					inv.sendMessage(Main.prefix + ChatColor.GOLD + "Your invitation to " + ChatColor.GREEN + t.getName()
							+ ChatColor.GOLD + " has " + ChatColor.RED + "expired");
					t.sendMessage(Main.prefix + ChatColor.GREEN + inv.getName() + ChatColor.GOLD
							+ "'s invitation to the party has " + ChatColor.RED + "expired");
					return;

				}

			}
		}.runTaskLater(Main.getPlugin(), 20 * 30);

	}

	@Override
	public String name() {

		return "invite";
	}

	@Override
	public String info() {

		return "Invites a player to your team";
	}

}

/*
 * if (args.length != 1) { p.sendMessage(Main.prefix + ChatColor.RED +
 * "Use /equipe invite <name>"); return; }
 * 
 * if (PartyUtils.isInParty(p)) {
 * 
 * if (Bukkit.getServer().getPlayer(args[0]) == null) {
 * p.sendMessage(Main.prefix + ChatColor.GOLD + "Could not find player: " +
 * ChatColor.RED + args[0]); return; }
 * 
 * if (PartyUtils.isInParty(Bukkit.getPlayer(args[0]))) {
 * p.sendMessage(Main.prefix + ChatColor.RED + args[0] + ChatColor.GOLD +
 * " is already in a party!"); return; }
 * 
 * Main.inviteList.put(Bukkit.getPlayer(args[0]).getName(),
 * PartyUtils.getParty(p));
 * 
 * p.sendMessage(Main.prefix + ChatColor.GOLD + "You have invited " +
 * ChatColor.RED + args[0] + ChatColor.GOLD + " to your party!");
 * 
 * Bukkit.getPlayer(args[0]).sendMessage(Main.prefix + ChatColor.RED +
 * p.getName() + ChatColor.GOLD + " Has invited you to their party. You have " +
 * ChatColor.RED + "10 seconds " + ChatColor.GOLD + "to join. Type " +
 * ChatColor.RED + "/equipe accept " + ChatColor.GOLD + " to join!");
 * 
 * new BukkitRunnable() {
 * 
 * public void run() {
 * 
 * if (Bukkit.getPlayer(args[0]) == null) {
 * 
 * return; }
 * 
 * if (Main.inviteList.containsKey(args[0])) {
 * 
 * if (Main.inviteList.get(args[0]) == PartyUtils.getParty(p)) {
 * 
 * Main.inviteList.remove(args[0]);
 * Bukkit.getPlayer(args[0]).sendMessage(Main.prefix + ChatColor.GOLD +
 * "Invitation to " + ChatColor.RED + p.getName() + ChatColor.GOLD +
 * "'s Party has expired");
 * 
 * PartyUtils.getParty(p).sendMessage(Main.prefix + ChatColor.RED + args[0] +
 * ChatColor.GOLD + "'s invitation to the party has expired!");
 * 
 * }
 * 
 * }
 * 
 * } }.runTaskLater(Main.getPlugin(), 200);
 * 
 * return;
 * 
 * } p.sendMessage(Main.prefix + ChatColor.RED + "You aren't in a party!");
 */
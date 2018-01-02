package me.pro.dmg.PrevailPots.Core;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.pro.dmg.PrevailPots.Teams.Create;
import me.pro.dmg.PrevailPots.Teams.Disband;
import me.pro.dmg.PrevailPots.Teams.Invite;
import me.pro.dmg.PrevailPots.Teams.Join;
import me.pro.dmg.PrevailPots.Teams.Kick;
import me.pro.dmg.PrevailPots.Teams.Leave;
import me.pro.dmg.PrevailPots.Teams.SetRoster;
import me.pro.dmg.PrevailPots.Teams.TeamChat;

public class TeamCommandManager implements CommandExecutor {

	String prefix = Main.prefix;
	private ArrayList<TeamSubCommand> teamCommands = new ArrayList<TeamSubCommand>();

	public void setup() {

		teamCommands.add(new Create());
		teamCommands.add(new Disband());
		teamCommands.add(new SetRoster());
		teamCommands.add(new Invite());
		teamCommands.add(new Join());
		teamCommands.add(new Leave());
		teamCommands.add(new Kick());
		teamCommands.add(new TeamChat());

	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player)) {
			return true;
		}
		Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("team")) {

			if (args.length == 0) {

				for (TeamSubCommand c : teamCommands) {

					p.sendMessage(prefix + ChatColor.GOLD + "/team " + c.name() + ChatColor.DARK_GRAY + " - "
							+ ChatColor.RED + c.info());

				}
				return true;
			}

			TeamSubCommand target = get(args[0]);

			if (target == null) {

				p.sendMessage(prefix + ChatColor.RED + "/team " + args[0] + " is not a valid command!");
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

	private TeamSubCommand get(String name) {

		for (TeamSubCommand cmd : teamCommands) {

			if (cmd.name().equalsIgnoreCase(name))
				return cmd;

		}
		return null;

	}

}

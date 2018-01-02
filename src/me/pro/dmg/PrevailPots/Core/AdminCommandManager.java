package me.pro.dmg.PrevailPots.Core;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import me.pro.dmg.PrevailPots.Arenas.SetArenaCommand;
import me.pro.dmg.PrevailPots.Kits.CreateKitCommand;
import me.pro.dmg.PrevailPots.Kits.DeleteKitCommand;
import me.pro.dmg.PrevailPots.Kits.SetKitChest;
import me.pro.dmg.PrevailPots.Utils.CheckArenaCommand;
import me.pro.dmg.PrevailPots.Warping.SetKitCreation;
import me.pro.dmg.PrevailPots.Warping.SetSpawn;

public class AdminCommandManager implements CommandExecutor {

	String prefix = Main.prefix;
	private ArrayList<AdminSubCommand> senderCommands = new ArrayList<AdminSubCommand>();

	public void setup() {

		senderCommands.add(new SetArenaCommand());
		senderCommands.add(new SetSpawn());
		senderCommands.add(new SetKitCreation());
		senderCommands.add(new CreateKitCommand());
		senderCommands.add(new DeleteKitCommand());
		senderCommands.add(new SetKitChest());
		senderCommands.add(new CheckArenaCommand());

	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("aprevail")) {

			if (sender instanceof ConsoleCommandSender || sender.isOp()) {

				if (args.length == 0) {

					for (AdminSubCommand c : senderCommands) {

						sender.sendMessage(prefix + ChatColor.GOLD + "/aprevail " + c.name() + ChatColor.DARK_GRAY
								+ " - " + ChatColor.RED + c.info());

					}
					return true;
				}

				AdminSubCommand target = get(args[0]);

				if (target == null) {

					sender.sendMessage(prefix + ChatColor.GOLD + "/aprevail " + args[0] + " is not a valid command!");
					return true;
				}

				ArrayList<String> a = new ArrayList<String>();
				a.addAll(Arrays.asList(args));
				a.remove(0);
				args = a.toArray(new String[a.size()]);

				try {
					target.onCommand(sender, args);
				} catch (Exception e) {

					sender.sendMessage(prefix + ChatColor.DARK_RED + "An error has occured: " + e.getCause());
					e.printStackTrace();
				}

			}
		}
		return true;

	}

	private AdminSubCommand get(String name) {

		for (AdminSubCommand cmd : senderCommands) {

			if (cmd.name().equalsIgnoreCase(name))
				return cmd;

		}
		return null;

	}

}

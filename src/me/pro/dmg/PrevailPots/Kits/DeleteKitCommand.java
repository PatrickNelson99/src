package me.pro.dmg.PrevailPots.Kits;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import me.pro.dmg.PrevailPots.Core.AdminSubCommand;
import me.pro.dmg.PrevailPots.Core.Main;

public class DeleteKitCommand extends AdminSubCommand {

	KitDataFile kd = KitDataFile.getInstance();

	@Override
	public void onCommand(CommandSender sender, String[] args) {

		if (args.length < 1) {

			sender.sendMessage(Main.prefix + ChatColor.RED + "Please specify a name");
			return;

		}

		if (kd.getData().getConfigurationSection("Kits") == null) {
			sender.sendMessage(Main.prefix + ChatColor.RED + "No available kits to delete!");
			return;
		}

		else {

			for (String key : kd.getData().getConfigurationSection("Kits").getKeys(false)) {

				if (kd.getData().getString("Kits." + key + ".Name").equalsIgnoreCase(args[0])) {
					kd.getData().set("Kits." + key, null);
					kd.saveData();
					sender.sendMessage(Main.prefix + ChatColor.GOLD + "Deleted kit: " + ChatColor.RED + args[0]);
					return;
				}

			}

		}

		sender.sendMessage(Main.prefix + ChatColor.GOLD + "Could not find kit: " + ChatColor.RED + args[0]);

	}

	@Override
	public String name() {

		return "delkit";
	}

	@Override
	public String info() {

		return "Deletes specified kit";
	}

}

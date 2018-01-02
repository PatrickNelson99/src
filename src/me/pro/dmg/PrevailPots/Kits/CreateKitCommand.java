package me.pro.dmg.PrevailPots.Kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import me.pro.dmg.PrevailPots.Core.AdminSubCommand;
import me.pro.dmg.PrevailPots.Core.Main;

public class CreateKitCommand extends AdminSubCommand {

	KitDataFile kd = KitDataFile.getInstance();

	@Override
	public void onCommand(CommandSender sender, String[] args) {

		if (sender instanceof Player) {

			Player p = (Player) sender;

			if (args.length < 2) {

				p.sendMessage(Main.prefix + ChatColor.RED + "Please specify a name and material!");
				return;

			}

			if (Material.matchMaterial(args[1]) == null) {
				p.sendMessage(Main.prefix + ChatColor.RED + args[1] + ChatColor.GOLD + " is not a material");
				return;
			}

			String name = args[0];

			kd.getData().set("Kits." + name + ".Name", name);
			kd.getData().set("Kits." + name + ".Material", args[1]);
			kd.getData().set("Kits." + name + ".InventoryContents", p.getInventory().getContents());
			kd.getData().set("Kits." + name + ".ArmourContents", p.getInventory().getArmorContents());
			kd.saveData();

			p.sendMessage(Main.prefix + ChatColor.GREEN + "Kit saved!");

		}

	}

	@Override
	public String name() {

		return "kit";
	}

	@Override
	public String info() {

		return "Creates a kit";
	}

}

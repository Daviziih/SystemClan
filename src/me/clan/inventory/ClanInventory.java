package me.clan.inventory;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import me.clan.Clan;

public class ClanInventory implements Listener {

	public ClanInventory(Clan main) {
		Bukkit.getPluginManager().registerEvents(this, main);
	}

	private static String nameInventory = "§7Informações do clan";

	public static void openInventory(Player p) {

		Inventory inventory = Bukkit.createInventory(null, 9 * 5, nameInventory);

		List<ItemStack> items = new ArrayList<>();

		int a = 0;

		if (a <= 18) {

			ItemStack Stats = new ItemStack(Material.SKULL_ITEM);
			Stats.setDurability((short) 3);
			Stats.setAmount(1);
			SkullMeta Stats2 = (SkullMeta) Stats.getItemMeta();
			// Stats2.setDisplayName(displayName);
			// Stats2.setLore(lore);
			Stats.setItemMeta(Stats2);
			items.add(Stats);

		}
		a++;

		p.openInventory(inventory);
	}
}

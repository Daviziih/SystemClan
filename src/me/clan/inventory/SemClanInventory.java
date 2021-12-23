package me.clan.inventory;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import me.clan.Clan;
import me.davi.api.Api;

public class SemClanInventory implements Listener {

	public SemClanInventory(Clan main) {
		Bukkit.getPluginManager().registerEvents(this, main);
	}

	private static String nomeInventory = "§7Menu de Kits";
	private static Inventory inv = Bukkit.createInventory(null, 9 * 3, nomeInventory);

	public static void openInventoryKits(Player p) {

		Api.setItemInventory(inv, 11, Material.NAME_TAG, "§bCrie sua Clan",
				Arrays.asList("", "§7Clique para criar sua clan", "", "§aClique para selecionar!"));

		Api.setItemInventory(inv, 15, Material.EMERALD, "§bConvites para Clan",
				Arrays.asList("", "§7Entre em uma Clan já existente", "", "§aClique para selecionar!"));

		p.openInventory(inv);
	}

	@EventHandler
	private void onInteractMenu(InventoryClickEvent e) {
		if (e.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) {
			return;
		}
		Player p = (Player) e.getWhoClicked();
		if (e.getInventory().getTitle().equals(nomeInventory)) {
			e.setCancelled(true);
			if (e.getCurrentItem() != null) {
				if (e.getSlot() == 11) {
				}
			}
		}
	}
}

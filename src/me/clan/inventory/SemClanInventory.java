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
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class SemClanInventory implements Listener {

	public SemClanInventory(Clan main) {
		Bukkit.getPluginManager().registerEvents(this, main);
	}

	private static String nomeInventory = "§7Menu de Clans";
	private static Inventory inv = Bukkit.createInventory(null, 9 * 3, nomeInventory);

	public static void openInventoryKits(Player p) {

		Api.setItemInventory(inv, 11, Material.NAME_TAG, "§bCrie sua Clan",
				Arrays.asList("", "§7Clique para criar sua clan", "", "§eClique para selecionar!"));

		Api.setItemInventory(inv, 15, Material.BEACON, "§bConvites para Clan",
				Arrays.asList("", "§7Entre em uma Clan já existente", "", "§eClique para selecionar!"));

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
					TextComponent texto = new TextComponent("§9§lCLAN§f Clique aqui para criar sua §9§lCLÃ§f!");
					texto.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/clan criar (clanName)"));
					p.spigot().sendMessage(texto);
					p.closeInventory();
				}
			}
		}
	}
}

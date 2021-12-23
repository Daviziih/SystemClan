package me.clan.app;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import me.clan.Clan;
import me.clan.database.Connections;
import me.davi.holograma.Hologram;
import me.davi.holograma.HologramLibrary;

public class CreateHologram {

	public static void update() {
		setTopGlobal();
	}

	private static void setTopGlobal() {
		Location locationTopClans = Bukkit.getWorld("world").getSpawnLocation();
		Hologram hologramTopClans = HologramLibrary.createHologram(locationTopClans, "", "", "", "", "", "", "", "", "",
				"", "", "§fcom o clan com mais LEVEL", "§fO rank é ordenado de acordo", "§4§lTOP 10 CLAN");
		hologramTopClans.spawn();

		new BukkitRunnable() {
			@Override
			public void run() {

				try {
					PreparedStatement stm = Connections.con
							.prepareStatement("SELECT * FROM PlayerClans ORDER BY Level DESC");
					ResultSet rs = stm.executeQuery();
					int i = 11;
					while (rs.next()) {
						if (i >= 0) {
							i--;
							String nome = rs.getString("CLAN");
							String level = rs.getString("LEVEL");

							hologramTopClans.updateLine(i, "§fClan §e" + nome + " §f| §4 LEVEL " + level);

						}
					}
				} catch (SQLException e) {
					Bukkit.getConsoleSender().sendMessage("§4HOLOGRAMA§f Não foi possivel pegar carregar o ");
				}
			}
		}.runTaskTimer(Clan.plugin, 0, 20);

	}

}
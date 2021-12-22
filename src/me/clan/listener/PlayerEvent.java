package me.clan.listener;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.clan.Clan;
import me.clan.construction.PlayerCargo;
import me.clan.construction.PlayerClan;
import me.clan.database.Connections;
import me.clan.enun.Cargos;

public class PlayerEvent implements Listener {

	public PlayerEvent(Clan main) {
		Bukkit.getPluginManager().registerEvents(this, main);
	}

	@EventHandler
	private void onPlayerLogin(PlayerLoginEvent e) {
		Player p = e.getPlayer();
		try {
			ResultSet rs = Connections.con.createStatement()
					.executeQuery("SELECT * FROM PlayerCargos WHERE Player = '" + p.getName() + "'");
			if (rs.next()) {

				String uuid = rs.getString("UUID");
				String nome = rs.getString("PLAYER");
				String cargo = rs.getString("CARGO");
				String clanName = rs.getString("CLAN");

				if (Cargos.valueOf(cargo) == Cargos.NENHUM) {
					PlayerCargo.cachePlayer.put(p.getName(),
							new PlayerCargo(UUID.fromString(uuid), nome, Cargos.valueOf(cargo), null));
				} else {
					PlayerCargo.cachePlayer.put(p.getName(),
							new PlayerCargo(UUID.fromString(uuid), nome, Cargos.valueOf(cargo), clanName));
				}

			} else {
				try {
					PreparedStatement stm = Connections.con
							.prepareStatement("INSERT INTO PlayerCargos (UUID, PLAYER, CARGO, CLAN) VALUES (?,?,?,?)");
					stm.setString(1, p.getUniqueId().toString());
					stm.setString(2, p.getName());
					stm.setString(3, Cargos.NENHUM.getName());
					stm.setString(4, null);
					stm.executeUpdate();
					PlayerCargo.cachePlayer.put(p.getName(),
							new PlayerCargo(p.getUniqueId(), p.getName(), Cargos.NENHUM, null));
				} catch (SQLException erro) {
				}
			}
		} catch (SQLException erro) {
		}

		try {
			ResultSet rs = Connections.con.createStatement()
					.executeQuery("SELECT * FROM PlayerClans WHERE Members = '" + p.getName() + "'");
			if (rs.next()) {
				String clanName = rs.getString("CLAN");
				String clanOwner = rs.getString("OWNER");
				String clanLevel = rs.getString("LEVEL");

				PlayerClan.cacheClan.put(p.getName(), new PlayerClan(clanName, clanOwner, null, clanLevel, null));
				System.out.println("Teste PlayerClan com clan");
			} else {
				PlayerClan.cacheClan.put(p.getName(), new PlayerClan(null, null, null, null, null));
				System.out.println("Teste PlayerClans");
			}
		} catch (SQLException erro) {
		}

	}

	@EventHandler
	private void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();

		PlayerCargo pCargo = PlayerCargo.cachePlayer.get(p.getName());

		try {
			Connections.con.createStatement().executeUpdate("UPDATE PlayerCargos set CARGO='" + pCargo.getCargo()
					+ "',CLAN='" + pCargo.getClanName() + "' WHERE PLAYER = '" + p.getName() + "'");
		} catch (SQLException e2) {
			e2.printStackTrace();
		}

		PlayerClan.cacheClan.remove(p.getName());
		PlayerCargo.cachePlayer.remove(p.getName());
	}
}

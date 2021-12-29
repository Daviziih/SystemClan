package me.clan;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import me.clan.app.CreateHologram;
import me.clan.command.cClan;
import me.clan.construction.PlayerCargo;
import me.clan.construction.PlayerClan;
import me.clan.database.Connections;
import me.clan.enun.Cargos;
import me.clan.inventory.SemClanInventory;
import me.clan.listener.PlayerEvent;

public class Clan extends JavaPlugin {

	public static Clan plugin;
	public Command commandManger;

	private String sql = "INSERT INTO PlayerClans (CLAN, OWNER, MEMBERS, LEVEL, LOCATION) VALUES ";
	private String membroslista = "";

	private String sqlPlayers = "INSERT INTO PlayerCargos (UUID, PLAYER, CARGO, CLAN) VALUES ";

	@Override
	public void onEnable() {
		plugin = this;

		Connections.openConnectionMySQL();

		new PlayerEvent(plugin);
		new SemClanInventory(plugin);

		CreateHologram.update();
		CommandsRegister(plugin);

		try {

			ResultSet rs = Connections.con.prepareStatement("SELECT * FROM PlayerCargos;").executeQuery();
			ResultSet rs1 = Connections.con.prepareStatement("SELECT * FROM PlayerClans;").executeQuery();

			while (rs.next()) {
				String playerUuid = rs.getString("UUID");
				String playerName = rs.getString("PLAYER");
				String playerCargo = rs.getString("CARGO");
				String playerClan = rs.getString("CLAN");

				PlayerCargo.cachePlayer.put(playerName, new PlayerCargo(UUID.fromString(playerUuid), playerName,
						Cargos.valueOf(playerCargo), playerClan));

				while (rs1.next()) {

					String clanName = rs1.getString("CLAN");
					String clanOwner = rs1.getString("OWNER");
					String clanMembers = rs1.getString("MEMBERS");
					List<PlayerCargo> lista = new ArrayList<>();
					String[] mebroName = clanMembers.split(",");
					lista.add(PlayerCargo.cachePlayer.get(mebroName.toString()));
					String clanLevel = rs1.getString("LEVEL");
					String clanLocaltion = rs1.getString("LOCATION");

					PlayerClan.cacheClan.put(playerName, new PlayerClan(clanName, clanOwner, lista, clanLevel,
							Connections.stringToLocation(clanLocaltion)));

				}
			}
		} catch (SQLException erro) {
		}
	}

	int id = 0;

	@Override
	public void onDisable() {
		plugin = null;

		id = PlayerCargo.cachePlayer.size();
		if (id == 0) {
			return;
		}
		PlayerCargo.cachePlayer.forEach((key, value) -> {

			PlayerCargo player = value;

			try {
				Connections.con.prepareStatement("DELETE FROM PlayerCargos WHERE uuid != -1;").executeUpdate();

				sqlPlayers += "('" + player.getUuid() + "', '" + player.getJogador() + "', '" + player.getCargo()
						+ "', '" + player.getClanName() + "')";

			} catch (SQLException e) {
				e.printStackTrace();
			}

			if (1 < id) {
				sqlPlayers += ",";
			}
			id--;
		});

		id = PlayerClan.cacheClan.size();

		if (id == 0) {
			return;
		}

		PlayerClan.cacheClan.forEach((key, value) -> {

			PlayerClan clan = value;


			List<PlayerCargo> membersList = clan.getMembros();
			membersList.forEach(sla -> {
				membroslista += sla.getJogador() + ",";
			});

			sql += "('" + clan.getClan() + "','" + clan.getOwner() + "','" + membroslista + "','" + clan.getLevel()
					+ "','" + Connections.serializeLoc(clan.getLocation()) + "')";
			membroslista = "";

			if (1 < id) {
				sql += ",";
			}
			id--;
		});

		System.err.println("[" + sql + "]");

		try {
			Connections.con.prepareStatement(sqlPlayers + ";").executeUpdate();
			Connections.con.prepareStatement(sql + ";").executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		Connections.closeConnections();

	}

	public void CommandsRegister(Clan main) {
		try {
			final Field commandField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
			commandField.setAccessible(true);

			CommandMap newCommand = (CommandMap) commandField.get(Bukkit.getServer());

			newCommand.register("clan", new cClan("clan"));

		} catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
		}
	}
}

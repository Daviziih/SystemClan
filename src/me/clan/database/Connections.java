package me.clan.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import me.clan.Clan;

import java.sql.Statement;

public class Connections {

	public static Connection con;

	private static String ip = "localhost";
	private static String usuario = "root";
	private static String senha = "";
	private static String database = "newbase";

	public static void openConnectionMySQL() {
		try {
			con = DriverManager.getConnection("jdbc:mysql://" + ip + ":3306/" + database, usuario, senha);
			Bukkit.getConsoleSender().sendMessage("§b§lMYSQL§f A coxexão com §b§lMYSQL§f foi aberta!");
			createTableSQLite();
		} catch (Exception e) {
			e.printStackTrace();
			Bukkit.getConsoleSender()
					.sendMessage("§b§lMYSQL§f A coxexão com §b§lMYSQL§f §c§lFALHOU§f, o plugin será desabilitado!");
			Clan.plugin.getPluginLoader().disablePlugin(Clan.plugin);
		}
	}

	public static void closeConnections() {
		if (con != null) {
			try {
				con = null;
				Bukkit.getConsoleSender()
						.sendMessage("§e§lCONNECITONS§f As conexões do plugin §e§lCLANS§f foi §c§lDESABILITADAS§f!");
			} catch (Exception e) {
				e.printStackTrace();
				Bukkit.getConsoleSender()
						.sendMessage("§e§lCONNECITONS§f Erro ao fechar as §b§lCONEXÕES§f do plugin §e§lNEXUS§f!");
			}
		}
	}

	private static void createTableSQLite() {
		try {
			Statement c = con.createStatement();

			c.executeUpdate(
					"CREATE TABLE IF NOT EXISTS PlayerCargos (UUID VARCHAR(100), PLAYER VARCHAR(100), CARGO VARCHAR(100), CLAN VARCHAR(100))");

			c.executeUpdate(
					"CREATE TABLE IF NOT EXISTS PlayerClans (CLAN VARCHAR(100), OWNER VARCHAR(100), MEMBERS VARCHAR(100), LEVEL INT, LOCATION VARCHAR(100))");

			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static String serializeLoc(Location location) {
		return location.getWorld().getName() + "," + location.getBlockX() + "," + location.getBlockY() + ","
				+ location.getBlockZ() + "," + location.getPitch() + "," + location.getYaw();
	}

	public static Location stringToLocation(String loc) {
		String[] str = loc.split(",");
		World world = Bukkit.getServer().getWorld(str[0]);
		double x = Double.parseDouble(str[1]);
		double y = Double.parseDouble(str[2]);
		double z = Double.parseDouble(str[3]);
		float pitch = Float.parseFloat(str[4]);
		float yaw = Float.parseFloat(str[5]);
		return new Location(world, x, y, z, pitch, yaw);
	}
}

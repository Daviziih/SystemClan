package me.clan;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import me.clan.command.cClan;
import me.clan.database.Connections;
import me.clan.listener.PlayerEvent;

public class Clan extends JavaPlugin {

	public static Clan plugin;
	public Command commandManger;

	@Override
	public void onEnable() {
		plugin = this;

		Connections.openConnectionMySQL();

		new PlayerEvent(plugin);

		CommandsRegister(plugin);

	}

	@Override
	public void onDisable() {
		plugin = null;
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

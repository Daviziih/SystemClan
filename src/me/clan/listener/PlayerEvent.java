package me.clan.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import me.clan.Clan;
import me.clan.command.cClan;
import me.clan.construction.PlayerCargo;
import me.clan.construction.PlayerClan;
import me.clan.enun.Cargos;

public class PlayerEvent implements Listener {

	public PlayerEvent(Clan main) {
		Bukkit.getPluginManager().registerEvents(this, main);
	}

	@EventHandler
	private void onPlayerLogin(PlayerLoginEvent e) {
		Player p = e.getPlayer();

		if (!PlayerCargo.cachePlayer.containsKey(p.getName())) {
			PlayerCargo.cachePlayer.put(p.getName(),
					new PlayerCargo(p.getUniqueId(), p.getName(), Cargos.NENHUM, null));
		}

		if (!PlayerClan.cacheClan.containsKey(p.getName())) {
			PlayerClan.cacheClan.put(p.getName(), new PlayerClan(null, null, null, null, null));
		}
	}

	@EventHandler
	private void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (cClan.clanHomeCooldown.contains(p)) {
			cClan.clanHomeCooldown.remove(p);
			cClan.clanHomeIr.remove(p);
			p.sendMessage("§9§lCLAN§f Você se mexeu, o teleporte foi cancelando!");
			return;
		}
	}
}

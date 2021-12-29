package me.clan.construction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.clan.enun.Cargos;

public class PlayerCargo {

	public static HashMap<String, PlayerCargo> cachePlayer = new HashMap<>();

	private UUID uuid;
	private String jogador;
	private Cargos cargo;
	private String clanName;

	public PlayerCargo(UUID uuid, String jogador, Cargos cargo, String clanName) {
		this.uuid = uuid;
		this.jogador = jogador;
		this.cargo = cargo;
		this.clanName = clanName;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getJogador() {
		return jogador;
	}

	public void setJogador(String jogador) {
		this.jogador = jogador;
	}

	public Cargos getCargo() {
		return cargo;
	}

	public void setCargo(Cargos cargo) {
		this.cargo = cargo;
	}

	public String getClanName() {
		return clanName;
	}

	public void setClanName(String nomeClan) {
		this.clanName = nomeClan;
	}

	public static List<PlayerCargo> getAll() {
		return PlayerCargo.cachePlayer.values().stream().collect(Collectors.toList());
	}

	public List<Player> getClanOnlinePlayers() {
		ArrayList<Player> jogadoresOn = new ArrayList<>();
		for (Player on : Bukkit.getOnlinePlayers()) {
			PlayerCargo cpOn = cachePlayer.get(on.getName());
			if (cpOn.getClanName() != null) {
				if (cpOn.getClanName().equals(getClanName())) {
					jogadoresOn.add(on);
				}
			}
		}
		return jogadoresOn;
	}

}

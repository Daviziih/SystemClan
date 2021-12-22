package me.clan.construction;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;

public class PlayerClan {

	public static HashMap<String, PlayerClan> cacheClan = new HashMap<>();

	private String clanName;
	private String ownerClan;
	private List<PlayerCargo> membros;
	private String level;
	private Location location;

	public PlayerClan(String clanName, String ownerClan, List<PlayerCargo> membros, String level, Location location) {
		this.clanName = clanName;
		this.ownerClan = ownerClan;
		this.membros = membros;
		this.level = level;
		this.location = location;
	}

	public String getClan() {
		return clanName;
	}

	public void setClan(String clanName) {
		this.clanName = clanName;
	}

	public String getOwner() {
		return ownerClan;
	}

	public void setOwner(String ownerClan) {
		this.ownerClan = ownerClan;
	}

	public List<PlayerCargo> getMembros() {
		return membros;
	}

	public void setMembros(List<PlayerCargo> membros) {
		this.membros = membros;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

}
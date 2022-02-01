package me.clan.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.clan.Clan;
import me.clan.construction.PlayerCargo;
import me.clan.construction.PlayerClan;
import me.clan.enun.Cargos;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class cClan extends Command {

	public cClan(String name) {
		super(name);
	}

	public HashMap<String, String> clanConvidar = new HashMap<>();
	public HashMap<String, Long> clanConvidarCooldown = new HashMap<>();

	public static ArrayList<Player> clanHomeCooldown = new ArrayList<>();
	public static ArrayList<Player> clanHomeIr = new ArrayList<>();

	public static ArrayList<Player> clanNexus = new ArrayList<>();

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return true;
		}
		Player p = (Player) sender;
		if (args.length == 0) {
			p.sendMessage("Teste");
			return true;
		}
		PlayerCargo cachePlayer = PlayerCargo.cachePlayer.get(p.getName());
		PlayerClan cacheClan = PlayerClan.cacheClan.get(p.getName());
		if (args.length >= 1) {
			if (args[0].equalsIgnoreCase("criar")) {
				if (args.length == 1) {
					p.sendMessage("�9�lCLAN�f Use correto: �9/clan criar (name)");
					return true;
				}
				String nomeClan = args[1].toString();
				if (cachePlayer.getClanName() != null) {
					p.sendMessage("�9�lCLAN�f Voc� j� faz parte de um cl�!");
					return true;
				}
				if (nomeClan.length() < 4 || nomeClan.length() > 8) {
					p.sendMessage("�9�lCLAN�f O nome do clan deve ter entre 4 a 8 caracteres");
					return true;
				}
				if (!nomeClan.matches("[a-zA-Z0-9]*")) {
					p.sendMessage("�9�lCLAN�f O nome do seu cl� s� pode ter letras e n�meros");
					return true;
				}
				if (nomeClan == cacheClan.getClan()) {
					p.sendMessage("Teste");
					return true;
				}
				p.sendMessage("�9�lCLAN�f O Cl� �b" + nomeClan + "�f foi criado com sucesso");
				Bukkit.broadcastMessage(
						"�9�lCLAN�f O Cl� �b" + nomeClan + "�f acabou de ser criado por �e" + p.getName() + "�f!");

				ArrayList<PlayerCargo> membros = new ArrayList<>();
				membros.add(cachePlayer);

				cachePlayer.setCargo(Cargos.LIDER);
				cachePlayer.setClanName(nomeClan);

				cacheClan.setClan(nomeClan);
				cacheClan.setOwner(p.getName());
				cacheClan.setMembros(membros);
				cacheClan.setLevel("1");
				cacheClan.setLocation(null);

			} else if (args[0].equalsIgnoreCase("excluir") || args[0].equalsIgnoreCase("delete")) {
				if (cachePlayer.getClanName() == null) {
					p.sendMessage("�9�lCLAN�f Voc� n�o faz parte de um cl�!");
					return true;
				}
				if (cachePlayer.getCargo() != Cargos.LIDER) {
					p.sendMessage("�9�lCLAN�f Somente o l�der do cl� pode exclu�-lo!");
					return true;
				}
				p.sendMessage("�9�lCLAN�f Cl� excluido com �a�lSUCESSO�f!");
				Bukkit.broadcastMessage(
						"�9�lCLAN�f  O cl� �b" + cachePlayer.getClanName() + "�f acabou de ser excluido!");
				for (PlayerCargo cps : PlayerCargo.getAll()) {
					if (cps.getClanName() == cps.getClanName()) {
						cps.setCargo(Cargos.NENHUM);
						cps.setClanName(null);
					}
				}
				for (PlayerClan cps : PlayerClan.getAll()) {
					if (cps.getClan() == cps.getClan()) {
						cps.setLevel(null);
						cps.setClan(null);
						cps.setLocation(null);
						cps.setMembros(null);
						cps.setOwner(null);
					}
				}
			} else if (args[0].equalsIgnoreCase("convidar")) {
				if (args.length == 1) {
					p.sendMessage("�9�lCLAN�f Use correto: �9/clan convidar (jogador)");
					return true;
				}
				Player alvo = Bukkit.getPlayer(args[1]);
				if (cachePlayer.getClanName() == null) {
					p.sendMessage("�9�lCLAN�f Voc� n�o � membro de um cl�!");
					return true;
				}
				if (cachePlayer.getCargo() == Cargos.MEMBRO || cachePlayer.getCargo() == Cargos.NENHUM) {
					p.sendMessage("�9�lCLAN�f Voc� n�o tem permiss�o para convidar membros para seu cl�!");
					return true;
				}
				if (alvo == null) {
					p.sendMessage("�4�lERRO�f O jogador est� �c�lOFFLINE�f!");
					return true;
				}
				PlayerCargo alvoCargos = PlayerCargo.cachePlayer.get(alvo.getName());
				if (alvoCargos.getClanName() != null) {
					p.sendMessage("�9�lCLAN�f O jogador j� � membro de um cl�!");
					return true;
				}
				if (clanConvidar.containsKey(p.getName())) {
					p.sendMessage("�9�lCLAN�f Aguarde para convidar um outro jogador em seu cl�!");
					return true;
				}
				clanConvidar.put(alvo.getName(), p.getName());
				new BukkitRunnable() {
					@Override
					public void run() {
						clanConvidar.remove(alvo.getName(), p.getName());
					}
				}.runTaskLater(Clan.plugin, 500L);
				p.sendMessage("�9�lCLAN�f Voc� convidou o jogador �b" + alvo.getName() + "�f para entrar em seu cl�");
				alvo.sendMessage(
						"�9�lCLAN�f Voc� recebeu um convite para entrar no cl� �b" + cachePlayer.getClanName());
			} else if (args[0].equalsIgnoreCase("remover") || args[0].equalsIgnoreCase("expulsar")) {
				if (args.length == 1) {
					p.sendMessage("�9�lCLAN�f Use correto: �9/clan remover (jogador)");
					return true;
				}
				Player alvo = Bukkit.getPlayer(args[1]);
				if (cachePlayer.getClanName() == null) {
					p.sendMessage("�9�lCLAN�f Voc� n�o � membro de um cl�!");
					return true;
				}
				if (cachePlayer.getCargo() == Cargos.RECRUTADOR || cachePlayer.getCargo() == Cargos.MEMBRO
						|| cachePlayer.getCargo() == Cargos.NENHUM) {
					p.sendMessage("�9�lCLAN�f Voc� n�o tem permiss�o para explusar membros do seu cl�!");
					return true;
				}
				if (alvo == null) {
					p.sendMessage("�4�lERRO�f O jogador est� �c�lOFFLINE�f!");
					return true;
				}
				PlayerCargo alvoCahePlayer = PlayerCargo.cachePlayer.get(alvo.getName());
				PlayerClan alvoCacheClan = PlayerClan.cacheClan.get(alvo.getName());

				if (alvoCahePlayer.getJogador().equals(p.getName())) {
					p.sendMessage("�9�lCLAN�f Voc� n�o pode expulsar s� mesmo do cl�");
					return true;
				}
				if (alvoCahePlayer.getClanName() == null) {
					p.sendMessage("�9�lCLAN�f Esse jogador n�o faz parte de cl�!");
					return true;
				}
				if (cachePlayer.getClanName() != alvoCahePlayer.getClanName().toString()) {
					p.sendMessage("�9�lCLAN�f Esse jogador n�o faz parte do seu cl�!");
					return true;
				}
				if (cachePlayer.getCargo() == Cargos.OFICIAL) {
					if (alvoCahePlayer.getCargo() == Cargos.LIDER) {
						p.sendMessage("�9�lCLAN�f Voc� n�o pode expulsar o �b�lL�DER�f do seu cl�!");
						return true;
					}
					if (alvoCahePlayer.getCargo() == Cargos.OFICIAL) {
						p.sendMessage("�9�lCLAN�f Voc� n�o pode expulsar o �b�lOFICIAL�f do seu cl�!");
						return true;
					}
					p.sendMessage("�9�lCLAN�f O Jogador �b" + alvoCahePlayer.getJogador()
							+ "�f expulso do cl� com �a�lSUCESSO�f!");

					List<PlayerCargo> list = cacheClan.getMembros();
					list.remove(alvoCahePlayer);

					alvoCahePlayer.setCargo(Cargos.NENHUM);
					alvoCahePlayer.setClanName(null);

					alvoCacheClan.setMembros(list);

				} else {
					p.sendMessage("�9�lCLAN�f O Jogador �b" + alvoCahePlayer.getJogador()
							+ "�f expulso do cl� com �a�lSUCESSO�f!");

					List<PlayerCargo> list = cacheClan.getMembros();
					list.remove(alvoCahePlayer);

					alvoCahePlayer.setCargo(Cargos.NENHUM);
					alvoCahePlayer.setClanName(null);
					alvoCacheClan.setMembros(list);

				}
			} else if (args[0].equalsIgnoreCase("sair") || args[0].equalsIgnoreCase("leave")) {
				if (cachePlayer.getClanName() == null) {
					p.sendMessage("�9�lCLAN�f Voc� n�o � membro de uma cl�!");
					return true;
				}
				if (cachePlayer.getCargo() == Cargos.LIDER) {
					p.sendMessage("�9�lCLAN�f Voc� s� consegue sair do cl� excluindo!");
					return true;
				}

				List<PlayerCargo> list = cacheClan.getMembros();
				list.remove(cachePlayer);

				cachePlayer.setCargo(Cargos.NENHUM);
				cachePlayer.setClanName(null);

				cacheClan.setMembros(list);

				p.sendMessage("�9�lCLAN�f Voc� saiu do cl� com �a�lSUCESSO�f!");
			} else if (args[0].equalsIgnoreCase("entrar")) {
				if (args.length == 1) {
					p.sendMessage("�9�lCLAN�f Use correto: �9/clan entrar (clan)");
					return true;
				}
				String nomeClan = args[1].toString();
				if (cachePlayer.getClanName() != null) {
					p.sendMessage("�9�lCLAN�f Voc� j� faz parte de um cl�!");
					return true;
				}
				if (clanConvidar.containsKey(p.getName())) {
					String jogador = clanConvidar.get(p.getName());
					PlayerCargo cachePlayerConvite = PlayerCargo.cachePlayer.get(jogador);
					PlayerClan cacheClanConvite = PlayerClan.cacheClan.get(jogador);
					if (nomeClan.equalsIgnoreCase(cachePlayerConvite.getClanName())) {
						if (cacheClanConvite.getMembros().size() < 15) {
							if (cachePlayerConvite.getClanName() != null) {

								List<PlayerCargo> list = cacheClanConvite.getMembros();
								list.add(cachePlayer);

								cachePlayer.setCargo(Cargos.MEMBRO);
								cachePlayer.setClanName(cacheClanConvite.getClan());
								cacheClan.setMembros(list);

								PlayerClan.cacheClan.put(p.getName(),
										new PlayerClan(cacheClanConvite.getClan(), cacheClanConvite.getOwner(),
												cacheClanConvite.getMembros(), cacheClanConvite.getLevel(),
												cacheClanConvite.getLocation()));

								p.sendMessage("�9�lCLAN�f Voc� entrou no cl� �b�l" + cachePlayer.getClanName()
										+ "�f com sucesso!");
							} else {
								p.sendMessage("�9�lCLAN�f Esse cl� n�o existe!");
							}
						} else {
							p.sendMessage("�9�lCLAN�f O clan esta com a quantidade maxima de membros");
						}
					}
				} else {
					p.sendMessage("�9�lCLAN�f Voc� n�o tem nenhum convite para entrar em um cl�!");
				}
			} else if (args[0].equalsIgnoreCase("setnexus")) {
				if (cachePlayer.getClanName() == null) {
					p.sendMessage("�9�lCLAN�f Voc� n�o � membro de nenhum cl�!");
					return true;
				}
				if (cachePlayer.getCargo() != Cargos.LIDER) {
					p.sendMessage("�9�lCLAN�f Somente o lider consegue setar o �b�lNEXUS�f do clan");
					return true;
				}
				if (cacheClan.getLocation() != null) {
					p.sendMessage("�9�lCLAN�f O �b�lNEXUS�f do clan j� foi setado!");
					return true;
				}
				new BukkitRunnable() {
					@Override
					public void run() {
						clanNexus.remove(p);
					}
				}.runTaskLater(Clan.plugin, 500);
				clanNexus.add(p);
				TextComponent texto = new TextComponent(
						"�9�lCLAN�f �fClique �a�lAQUI�f para setar o �b�lNEXUS�f do seu clan");
				texto.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan nexussetar12"));
				p.spigot().sendMessage(texto);
			} else if (args[0].equalsIgnoreCase("nexussetar12")) {
				if (clanNexus.contains(p)) {
					if (cachePlayer.getClanName() == null) {
						p.sendMessage("�9�lCLAN�f Voc� n�o � membro de nenhum cl�!");
						return true;
					}
					if (cachePlayer.getCargo() != Cargos.LIDER) {
						p.sendMessage("�9�lCLAN�f Somente o lider consegue setar o �b�lNEXUS�f do clan");
						return true;
					}
					if (cacheClan.getLocation() != null) {
						p.sendMessage("�9�lCLAN�f O �b�lNEXUS�f do clan j� foi setado!");
						return true;
					}
					if (!p.getWorld().getName().equals("world")) {
						p.sendMessage("�4�lERRO�f Voc� n�o consegue spawnar o �b�lNEXUS�f aqui!");
						return true;
					}
					if (p.getLocation().getBlockY() > 150) {
						p.sendMessage("�4�lERRO�f Voc� pode spawnar o �b�lNEXUS�f at� a �a�lALTURA�f 150!");
						return true;
					}
					cacheClan.setLocation(p.getLocation());
					clanNexus.remove(p);
					p.sendMessage("�9�lCLAN�f O �b�lNEXUS�f do clan foi setado com �a�lSUCESSO�f!");
					Bukkit.getWorld("world").spawn(p.getLocation(), EnderCrystal.class);
				}
			} else if (args[0].equalsIgnoreCase("nexus") || args[0].equalsIgnoreCase("home")) {
				if (cachePlayer.getClanName() == null) {
					p.sendMessage("�9�lCLAN�f Voc� n�o � membro de nenhum cl�!");
					return true;
				}
				if (cacheClan.getLocation() == null) {
					p.sendMessage("�9�lCLAN�f O �b�lNEXUS�f do seu clan ainda n�o foi setado");
					return true;
				}
				if (p.hasPermission("bypass.teleporte")) {
					Location location = cacheClan.getLocation();
					location.add(0, 0.2, 0);
					p.teleport(location);
					p.sendMessage("�9�lCLAN�f Voc� foi teleportado para o �b�lNEXUS�f do clan");
				} else {
					Bukkit.getScheduler().scheduleSyncDelayedTask(Clan.plugin, new Runnable() {
						public void run() {
							if (clanHomeIr.contains(p)) {
								clanHomeIr.remove(p);
								clanHomeCooldown.remove(p);
								Location location = cacheClan.getLocation();
								location.add(0, 0.2, 0);
								p.teleport(location);
								p.sendMessage("�9�lCLAN�f Voc� foi teleportado para o �b�lNEXUS�f do clan");
							}
						}
					}, 100L);
					clanHomeIr.add(p);
					clanHomeCooldown.add(p);
					p.sendMessage("�9�lHOME�f Aguarde �c5 Segundos�f para teletransportar");
				}
			} else if (args[0].equalsIgnoreCase("teste")) {
				System.out.println("Clan: " + cachePlayer.getClanName() + "Cargo: " + cachePlayer.getCargo()
						+ "Members: " + cacheClan.getMembros().toString() + " Location�e" + cacheClan.getLocation());
			}
		}
		return false;
	}
}

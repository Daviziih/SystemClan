package me.clan.command;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.clan.Clan;
import me.clan.construction.PlayerCargo;
import me.clan.construction.PlayerClan;
import me.clan.enun.Cargos;

public class cClan extends Command {

	public cClan(String name) {
		super(name);
	}

	public HashMap<String, String> clanConvidar = new HashMap<>();
	public HashMap<String, Long> clanConvidarCooldown = new HashMap<>();

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
		PlayerCargo pCargos = PlayerCargo.cachePlayer.get(p.getName());
		PlayerClan pClan = PlayerClan.cacheClan.get(p.getName());
		if (args.length >= 1) {
			if (args[0].equalsIgnoreCase("criar")) {
				if (args.length == 1) {
					p.sendMessage("�9�lCLAN�f Use cooreto: �9/clan criar (name)");
					return true;
				}
				String nomeClan = args[1].toString();
				if (pCargos.getClanName() != null) {
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
				p.sendMessage("�9�lCLAN�f O Cl� �b" + nomeClan + "�f foi criado com sucesso");
				Bukkit.broadcastMessage(
						"�9�lCLAN�f O Cl� �b" + nomeClan + "�f acabou de ser criado por �e" + p.getName() + "�f!");
				pCargos.setCargo(Cargos.LIDER);
				pCargos.setClanName(nomeClan);

				pClan.setClan(nomeClan);
				pClan.setOwner(p.getName());
				ArrayList<PlayerCargo> membros = new ArrayList<>();
				membros.add(pCargos);
				pClan.setMembros(membros);
				pClan.setLocation(null);
			} else if (args[0].equalsIgnoreCase("excluir") || args[0].equalsIgnoreCase("delete")) {
				if (pCargos.getClanName() == null) {
					p.sendMessage("�9�lCLAN�f Voc� n�o faz parte de um cl�!");
					return true;
				}
				if (pCargos.getCargo() != Cargos.LIDER) {
					p.sendMessage("�9�lCLAN�f Somente o l�der do cl� pode exclu�-lo!");
					return true;
				}
				p.sendMessage("�9�lCLAN�f Cl� excluido com �a�lSUCESSO�f!");
				Bukkit.broadcastMessage("�9�lCLAN�f  O cl� �b" + pCargos.getClanName() + "�f acabou de ser excluido!");
				for (PlayerCargo cps : PlayerCargo.getAll()) {
					if (cps.getClanName() == cps.getClanName()) {
						cps.setCargo(Cargos.NENHUM);
						cps.setClanName(null);
					}
				}
			} else if (args[0].equalsIgnoreCase("convidar")) {
				if (args.length == 1) {
					p.sendMessage("�9�lCLAN�f Use correto: �9/clan convidar (jogador)");
					return true;
				}
				Player alvo = Bukkit.getPlayer(args[1]);
				if (pCargos.getClanName() == null) {
					p.sendMessage("�9�lCLAN�f Voc� n�o � membro de um cl�!");
					return true;
				}
				if (pCargos.getCargo() == Cargos.MEMBRO || pCargos.getCargo() == Cargos.NENHUM) {
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
				alvo.sendMessage("�9�lCLAN�f Voc� recebeu um convite para entrar no cl� �b" + pCargos.getClanName());
			} else if (args[0].equalsIgnoreCase("remover") || args[0].equalsIgnoreCase("expulsar")) {
				if (args.length == 1) {
					p.sendMessage("�9�lCLAN�f Use correto: �9/clan remover (jogador)");
					return true;
				}
				Player alvo = Bukkit.getPlayer(args[1]);
				if (pCargos.getClanName() == null) {
					p.sendMessage("�9�lCLAN�f Voc� n�o � membro de um cl�!");
					return true;
				}
				if (pCargos.getCargo() == Cargos.RECRUTADOR || pCargos.getCargo() == Cargos.MEMBRO
						|| pCargos.getCargo() == Cargos.NENHUM) {
					p.sendMessage("�9�lCLAN�f Voc� n�o tem permiss�o para explusar membros do seu cl�!");
					return true;
				}
				if (alvo == null) {
					p.sendMessage("�4�lERRO�f O jogador est� �c�lOFFLINE�f!");
					return true;
				}
				PlayerCargo alvoCargos = PlayerCargo.cachePlayer.get(alvo.getName());
				if (alvoCargos.getJogador().equals(p.getName())) {
					p.sendMessage("�9�lCLAN�f Voc� n�o pode expulsar s� mesmo do cl�");
					return true;
				}
				if (alvoCargos.getClanName() == null) {
					p.sendMessage("�9�lCLAN�f Esse jogador n�o faz parte de cl�!");
					return true;
				}
				if (pCargos.getClanName() != alvoCargos.getClanName().toString()) {
					p.sendMessage("�9�lCLAN�f Esse jogador n�o faz parte do seu cl�!");
					return true;
				}
				if (pCargos.getCargo() == Cargos.OFICIAL) {
					if (alvoCargos.getCargo() == Cargos.LIDER) {
						p.sendMessage("�9�lCLAN�f Voc� n�o pode expulsar o �b�lL�DER�f do seu cl�!");
						return true;
					}
					if (alvoCargos.getCargo() == Cargos.OFICIAL) {
						p.sendMessage("�9�lCLAN�f Voc� n�o pode expulsar o �b�lOFICIAL�f do seu cl�!");
						return true;
					}
					p.sendMessage("�9�lCLAN�f O Jogador �b" + alvoCargos.getJogador()
							+ "�f expulso do cl� com �a�lSUCESSO�f!");
					alvoCargos.setCargo(Cargos.NENHUM);
					alvoCargos.setClanName(null);
					ArrayList<PlayerCargo> membros = new ArrayList<>();
					membros.remove(alvoCargos);
					pClan.setMembros(membros);
				} else {
					p.sendMessage("�9�lCLAN�f O Jogador �b" + alvoCargos.getJogador()
							+ "�f expulso do cl� com �a�lSUCESSO�f!");
					alvoCargos.setCargo(Cargos.NENHUM);
					alvoCargos.setClanName(null);
					ArrayList<PlayerCargo> membros = new ArrayList<>();
					membros.remove(alvoCargos);
					pClan.setMembros(membros);
				}
			} else if (args[0].equalsIgnoreCase("sair") || args[0].equalsIgnoreCase("leave")) {
				if (pCargos.getClanName() == null) {
					p.sendMessage("�9�lCLAN�f Voc� n�o � membro de uma cl�!");
					return true;
				}
				if (pCargos.getCargo() == Cargos.LIDER) {
					p.sendMessage("�9�lCLAN�f Voc� s� consegue sair do cl� excluindo!");
					return true;
				}
				ArrayList<PlayerCargo> membros = new ArrayList<>();
				membros.remove(pCargos);
				pClan.setMembros(membros);
				pCargos.setCargo(Cargos.NENHUM);
				pCargos.setClanName(null);
				p.sendMessage("�9�lCLAN�f Voc� saiu do cl� com �a�lSUCESSO�f!");
			} else if (args[0].equalsIgnoreCase("entrar")) {
				if (args.length == 1) {
					p.sendMessage("�9�lCLAN�f Use correto: �9/clan entrar (clan)");
					return true;
				}
				String nomeClan = args[1].toString();
				if (pCargos.getClanName() != null) {
					p.sendMessage("�9�lCLAN�f Voc� j� faz parte de uma cl�!");
					return true;
				}
				if (clanConvidar.containsKey(p.getName())) {
					String jogador = clanConvidar.get(p.getName());
					PlayerCargo playerConvite = PlayerCargo.cachePlayer.get(jogador);
					PlayerClan playerTeste = PlayerClan.cacheClan.get(jogador);
					if (nomeClan.equalsIgnoreCase(playerConvite.getClanName())) {
						if (playerConvite.getClanName() != null) {
							pCargos.setCargo(Cargos.MEMBRO);
							pCargos.setClanName(playerTeste.getClan());
							ArrayList<PlayerCargo> membros = new ArrayList<>();
							membros.add(pCargos);
							pClan.setMembros(membros);
							p.sendMessage(
									"�9�lCLAN�f Voc� entrou no cl� �b�l" + pCargos.getClanName() + "�f com sucesso!");
						} else {
							p.sendMessage("�9�lCLAN�f Esse cl� n�o existe!");
						}
					}
				} else {
					p.sendMessage("�9�lCLAN�f Voc� n�o tem nenhum convite para entrar em um cl�!");
				}
			} else if (args[0].equalsIgnoreCase("teste")) {
				System.out.println("Clan: " + pCargos.getClanName() + "Cargo: " + pCargos.getCargo() + "Members: "
						+ pClan.getMembros().toString());
			}
		}
		return false;
	}
}

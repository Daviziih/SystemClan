package me.clan.enun;

public enum Cargos {

	NENHUM("NENHUM"), 
	MEMBRO("MEMBRO"), 
	RECRUTADOR("RECRUTADOR"), 
	OFICIAL("OFICIAL"), 
	LIDER("LIDER");

	String name;

	Cargos(String string) {
		this.name = string;
	}

	public String getName() {
		return name;
	}
}

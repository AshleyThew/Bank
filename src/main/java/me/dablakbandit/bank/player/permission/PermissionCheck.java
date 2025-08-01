package me.dablakbandit.bank.player.permission;

public class PermissionCheck {

	private final String name;
	private final String permission;
	private final boolean has;

	public PermissionCheck(String name, String permission, boolean has) {
		this.name = name;
		this.permission = permission;
		this.has = has;
	}

	public String getName() {
		return name;
	}

	public String getPermission() {
		return permission;
	}

	public boolean has() {
		return has;
	}
}

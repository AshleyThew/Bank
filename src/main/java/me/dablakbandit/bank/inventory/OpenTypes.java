package me.dablakbandit.bank.inventory;

public enum OpenTypes {
	ALL, MENU, ITEMS, MONEY, EXP, LOANS;

	public static OpenTypes getOpenType(String name) {
		for (OpenTypes type : values()) {
			if (type.name().equalsIgnoreCase(name)) {
				return type;
			}
		}
		return null;
	}

}

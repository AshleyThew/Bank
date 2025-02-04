package me.dablakbandit.bank.implementations.citizens;

import me.dablakbandit.bank.inventory.OpenTypes;
import net.citizensnpcs.api.exception.NPCLoadException;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;
import net.citizensnpcs.api.util.DataKey;

import java.util.Arrays;
import java.util.stream.Collectors;

@TraitName("bank-trait")
public class BankTrait extends Trait{


	private OpenTypes[] types = new OpenTypes[0];

	public BankTrait(){
		super("bank-trait");
	}

	@Override
	public void load(DataKey key) throws NPCLoadException {
		super.load(key);
		if (types == null) {
			types = new OpenTypes[0];
		}
		if (key.keyExists("types")) {
			String[] savedTypes = key.getString("types").split(",");
			types = new OpenTypes[savedTypes.length];
			for (int i = 0; i < savedTypes.length; i++) {
				types[i] = OpenTypes.valueOf(savedTypes[i]);
			}
		}
	}

	public OpenTypes[] getTypes() {
		return types;
	}

	public void setOpenTypes(OpenTypes[] types) {
		this.types = types;
	}

	@Override
	public void save(DataKey key) {
		super.save(key);
		if (types == null || types.length == 0) {
			if (key.keyExists("types")) {
				key.removeKey("types");
			}
		} else {
			String saveType = Arrays.stream(types).map(Enum::name).collect(Collectors.joining(","));
			key.setString("types", saveType);
		}
	}
}

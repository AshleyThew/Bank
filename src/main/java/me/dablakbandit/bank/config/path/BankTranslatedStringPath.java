package me.dablakbandit.bank.config.path;

import me.dablakbandit.core.config.RawConfiguration;
import me.dablakbandit.core.config.path.TranslatedStringPath;

public class BankTranslatedStringPath extends TranslatedStringPath{
	public BankTranslatedStringPath(String def){
		super(def);
	}
	
	@Override
	protected String get(RawConfiguration config, String path){
		String value = super.get(config, path);
		value = value.replaceAll("<nl>", "\n");
		return value;
	}
	
	@Override
	protected Object setAs(String s){
		return super.setAs(s.replaceAll("\n", "<nl>"));
	}
}

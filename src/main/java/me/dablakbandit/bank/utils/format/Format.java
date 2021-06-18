package me.dablakbandit.bank.utils.format;

import me.dablakbandit.bank.config.BankPluginConfiguration;

public class Format{
	
	public static final String	NBSP_STRING		= "\u00A0";
	public static final String	NNBSP_STRING	= "\u202F";
	public static final char	NBSP			= NBSP_STRING.charAt(0);
	public static final char	NNBSP			= NNBSP_STRING.charAt(0);
	public static final char	REPLACE			= ',';
	
	public static String formatMoney(double amount){
		String ret;
		if(!BankPluginConfiguration.BANK_MONEY_FORMAT_ENABLED.get()){
			ret = String.format(BankPluginConfiguration.BANK_MONEY_FORMAT_NORMAL.get(), amount);
		}else{
			ret = FormatEnum.formatNormal(BankPluginConfiguration.BANK_MONEY_FORMAT_NORMAL.get(), BankPluginConfiguration.BANK_MONEY_FORMAT_ROUND.get(), amount, BankPluginConfiguration.BANK_MONEY_FULL_DOLLARS.get(), BankPluginConfiguration.BANK_MONEY_FORMAT_THOUSANDS.get());
		}
		if(ret.endsWith(".00")){
			ret = ret.substring(0, ret.length() - 3);
		}
		return ret.replace(NBSP, REPLACE).replace(NNBSP, REPLACE);
	}
	
	public static String formatExp(double amount){
		String ret;
		if(!BankPluginConfiguration.BANK_EXP_FORMAT_ENABLED.get()){
			ret = String.format(BankPluginConfiguration.BANK_EXP_FORMAT_NORMAL.get(), amount);
		}else{
			ret = FormatEnum.formatNormal(BankPluginConfiguration.BANK_EXP_FORMAT_NORMAL.get(), BankPluginConfiguration.BANK_EXP_FORMAT_ROUND.get(), amount, false, BankPluginConfiguration.BANK_EXP_FORMAT_THOUSANDS.get());
		}
		if(ret.endsWith(".00")){
			ret = ret.substring(0, ret.length() - 3);
		}
		return ret.replace(NBSP, REPLACE).replace(NNBSP, REPLACE);
	}
	
	public static String round(double amount, int points){
		return String.format("%." + points + "f", amount);
	}
}

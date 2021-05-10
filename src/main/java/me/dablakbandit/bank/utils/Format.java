package me.dablakbandit.bank.utils;

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
			ret = String.format("%,.2f", amount);
		}else if(BankPluginConfiguration.BANK_MONEY_FULL_DOLLARS.get()){
			ret = FormatEnum.formatNormal(amount, true);
		}else
			ret = FormatEnum.formatNormal(amount, false);
		if(ret.endsWith(".00")){
			ret = ret.substring(0, ret.length() - 3);
		}
		return ret.replace(NBSP, REPLACE).replace(NNBSP, REPLACE);
	}
	
	public static String formatExp(double amount){
		String ret;
		if(!BankPluginConfiguration.BANK_EXP_FORMAT_ENABLED.get()){
			ret = String.format("%,.2f", amount);
		}else{
			ret = FormatEnum.formatNormal(amount, false);
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

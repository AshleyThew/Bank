package me.dablakbandit.bank.utils;

import me.dablakbandit.bank.config.BankPluginConfiguration;

public class Format{
	
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
		return ret;
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
		return ret;
	}
	
	public static String round(double amount, int points){
		return String.format("%." + points + "f", amount);
	}
}

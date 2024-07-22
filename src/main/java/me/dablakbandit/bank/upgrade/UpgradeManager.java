package me.dablakbandit.bank.upgrade;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.bank.config.BankUpgradeConfiguration;
import me.dablakbandit.bank.log.BankLog;
import me.dablakbandit.bank.player.converter.old.OldBankConverter;
import me.dablakbandit.bank.save.type.SaveType;
import me.dablakbandit.core.config.comment.CommentConfiguration;
import me.dablakbandit.core.configuration.CoreConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class UpgradeManager{

	private static UpgradeManager instance = new UpgradeManager();
	private final int currentVersion;
	private final int previousVersion;
	private final File backupFolder;

	private UpgradeManager() {
		currentVersion = Integer.parseInt(BankPlugin.getInstance().getDescription().getVersion().replaceAll("[^0-9]", ""));

		BankUpgradeConfiguration.load(BankPlugin.getInstance());
		previousVersion = BankUpgradeConfiguration.UPGRADE_VERSION.get();
		backupFolder = new File(BankPlugin.getInstance().getDataFolder(), "backup" + File.separator + new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()));

		String bukkitVersion = Bukkit.getBukkitVersion().split("-")[0];
		int fullstopCount = bukkitVersion.length() - bukkitVersion.replace(".", "").length();
		int version = Integer.parseInt(bukkitVersion.replace(".", ""));
		if (fullstopCount == 1) {
			version *= 10;
		}

		if (previousVersion != 0 && previousVersion <= 470 && version >= 1205) {
			BankLog.errorAlways("Bank needs to update itemstacks on a server version prior to 1.20.4");
			BankLog.errorAlways("Please convert your database with an older server version.");
			BankLog.errorAlways("If you have already done this please set UPGRADE.VERSION to 480 in the upgrade.yml");
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.exit(0);
			Bukkit.shutdown();
			//Disable plugin
			Bukkit.getPluginManager().disablePlugin(BankPlugin.getInstance());
		}
	}

	public static UpgradeManager getInstance() {
		return instance;
	}

	public int getCurrentVersion() {
		return currentVersion;
	}

	public int getPreviousVersion() {
		return previousVersion;
	}
	
	public boolean hasUpgrade(){
		if(!new File(BankPlugin.getInstance().getDataFolder(), "config.yml").exists()){
			BankUpgradeConfiguration.UPGRADE_VERSION.set(currentVersion);
			return false;
		}
		return BankUpgradeConfiguration.UPGRADE_VERSION.get() == 0;
	}
	
	public boolean confirmUpgrade(){
		return BankUpgradeConfiguration.UPGRADE_CONFIRM.get();
	}
	
	public void printUpgradeAndShutdown(){
		BankLog.errorAlways("--=---=--=---=--=---=--=---=--");
		BankLog.errorAlways(" Bank plugin needs an upgrade");
		BankLog.errorAlways(" Please edit the upgrade.yml");
		BankLog.errorAlways("--=---=--=---=--=---=--=---=--");
		try{
			Thread.sleep(10000);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		Bukkit.shutdown();
	}
	
	public void upgrade(){
		int old = BankUpgradeConfiguration.UPGRADE_VERSION.get();
		boolean completed = false;
		if(old == 0){
			oldUpgrade();
		}else{
			copyFiles(".yml", "upgrade.yml");
			copyFiles(".db");
			completed = true;
		}
		if(!completed){
			System.exit(0);
			return;
		}
		updateConfig();
	}
	
	private void updateConfig(){
		BankUpgradeConfiguration.getInstance().saveConfig();
		BankUpgradeConfiguration.UPGRADE_VERSION.set(currentVersion);
		BankUpgradeConfiguration.UPGRADE_CONFIRM.set(false);
		BankUpgradeConfiguration.UPGRADE_CONVERSION_OLD.set(false);
		BankUpgradeConfiguration.UPGRADE_CONVERSION_MYSQL.set(false);
		BankUpgradeConfiguration.getInstance().saveConfig();
	}
	
	private void oldUpgrade(){
		SaveType to = SaveType.SQLITE;
		boolean completed = true;
		if(BankUpgradeConfiguration.UPGRADE_CONVERSION_OLD.get()){
			FileConfiguration configuration = new CoreConfiguration(BankPlugin.getInstance(), "config.yml").getConfig();
			completed = OldBankConverter.getInstance().convert(configuration.getString("SaveType", "SQLLITE"), to);
		}
		moveFiles(".yml", "upgrade.yml", "mysql.yml");
		moveFiles(".db", "database.db");
		BankPluginConfiguration.getInstance().saveConfig();
		CommentConfiguration configuration = BankPluginConfiguration.getInstance().getConfig();
		configuration.getKeys(false).forEach(s -> configuration.set(s, null));
		BankPluginConfiguration.getInstance().load();
		BankPluginConfiguration.BANK_SAVE_TYPE.set(to);
		updateConfig();
	}
	
	private void moveFiles(String extension, String... ignored){
		Arrays.stream(BankPlugin.getInstance().getDataFolder().listFiles()).filter(file -> filter(file, extension, ignored)).forEach(this::tryMoveFile);
	}
	
	private void copyFiles(String extension, String... ignored){
		Arrays.stream(BankPlugin.getInstance().getDataFolder().listFiles()).filter(file -> filter(file, extension, ignored)).forEach(this::tryCopyFile);
	}
	
	private boolean filter(File file, String extension, String... ignored){
		for(String ignore : ignored){
			if(file.getName().equalsIgnoreCase(ignore)){ return false; }
		}
		return file.getName().endsWith(extension);
	}
	
	private void tryMoveFile(File file){
		if(!file.isFile()){ return; }
		moveToBackupFolder(file, false);
	}
	
	private void tryCopyFile(File file){
		if(!file.isFile()){ return; }
		moveToBackupFolder(file, true);
	}
	
	private void moveToBackupFolder(File file, boolean copy){
		try{
			if(!backupFolder.exists()){
				backupFolder.mkdirs();
			}
			if(copy){
				Files.copy(file.toPath(), new File(backupFolder, file.getName()).toPath());
			}else{
				Files.move(file.toPath(), new File(backupFolder, file.getName()).toPath());
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
}

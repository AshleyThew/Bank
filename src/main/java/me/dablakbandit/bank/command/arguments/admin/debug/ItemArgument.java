package me.dablakbandit.bank.command.arguments.admin.debug;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.bank.save.loader.LoaderManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonObject;

import me.dablakbandit.bank.command.base.BankEndArgument;
import me.dablakbandit.bank.config.BankLanguageConfiguration;
import me.dablakbandit.bank.log.BankLog;
import me.dablakbandit.core.command.config.CommandConfiguration;
import me.dablakbandit.core.utils.ItemUtils;
import me.dablakbandit.core.utils.json.JSONParser;
import me.dablakbandit.core.utils.jsonformatter.JSONFormatter;
import me.dablakbandit.core.utils.jsonformatter.click.OpenUrlEvent;
import me.dablakbandit.core.utils.jsonformatter.hover.ShowTextEvent;

public class ItemArgument extends BankEndArgument{
	
	public ItemArgument(CommandConfiguration.Command command){
		super(command);
	}
	
	@Override
	protected void onArgument(CommandSender s, Command cmd, String label, String[] args, String[] original){

		Player player = (Player)s;
		ItemStack hand = player.getInventory().getItemInHand();
		if(hand == null || hand.getType() == Material.AIR){
			BankLanguageConfiguration.sendFormattedMessage(s, BankLanguageConfiguration.BANK_ADMIN_DEBUG_ITEM_NONE.get());
			return;
		}
		LoaderManager.getInstance().runAsync(() ->{
			HttpURLConnection con = null;
			try{

				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				String value = gson.toJson(JSONParser.parse(ItemUtils.getInstance().convertItemStackToJSON(hand).toString()));

				String url = "https://api.mclo.gs/1/log";
				String urlParameters = "content=" + URLEncoder.encode(value, "UTF-8");

				byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

				URL myurl = new URL(url);
				con = (HttpURLConnection)myurl.openConnection();

				con.setDoOutput(true);
				con.setRequestMethod("POST");
				con.setRequestProperty("User-Agent", "Java client");
				con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

				try(DataOutputStream wr = new DataOutputStream(con.getOutputStream())){

					wr.write(postData);
				}

				StringBuilder content;

				try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()))){
					String line;
					content = new StringBuilder();

					while((line = br.readLine()) != null){
						content.append(line);
						content.append(System.lineSeparator());
					}
				}

				JsonObject result = JSONParser.parse(content.toString()).getAsJsonObject();

				if(result.get("success").getAsBoolean()){
					String logUrl = result.get("url").getAsString();
					BankLog.debug("Uploaded debug item: " + logUrl);
					Bukkit.getScheduler().runTask(BankPlugin.getInstance(), () ->{
						JSONFormatter formatter = new JSONFormatter().appendHoverClick(	BankLanguageConfiguration.BANK_ADMIN_DEBUG_ITEM_URL.get().replaceAll("<url>", logUrl),
								new ShowTextEvent(ChatColor.GREEN + "Click to open"), new OpenUrlEvent(logUrl));
						formatter.send(player);
					});
				}else{
					BankLog.debug("Failed debug item: " + result.get("error").getAsString());
					BankLanguageConfiguration.sendFormattedMessage(s, BankLanguageConfiguration.BANK_ADMIN_DEBUG_ITEM_ERROR.get());
				}
			}catch(Exception e){
				e.printStackTrace();
				BankLanguageConfiguration.sendFormattedMessage(s, BankLanguageConfiguration.BANK_ADMIN_DEBUG_ITEM_ERROR.get());
			}finally{
				try{
					con.disconnect();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});

	}
	
	@Override
	public boolean hasPermission(CommandSender s){
		return isPlayer(s) && super.hasPermission(s);
	}
	
}

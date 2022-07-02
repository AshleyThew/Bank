package me.dablakbandit.bank.command.arguments.admin.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.dablakbandit.bank.command.base.BankEndArgument;
import me.dablakbandit.bank.config.BankLanguageConfiguration;
import me.dablakbandit.bank.implementations.other.BlockType;
import me.dablakbandit.core.command.config.CommandConfiguration;

public class AddArgument extends BankEndArgument{
	
	public AddArgument(CommandConfiguration.Command command){
		super(command);
	}
	
	@Override
	protected void onArgument(CommandSender s, Command cmd, String label, String[] args, String[] original){
		if(!checkPlayer(s)){ return; }
		Player player = (Player)s;
		Block looking = player.getTargetBlock(null, 5);
		if(looking.getType() != Material.AIR){
			BlockType.getInstance().addBlock(looking.getLocation());
			base.sendFormattedMessage(s, BankLanguageConfiguration.BANK_ADMIN_BLOCK_SET.get().replaceAll("<block>", looking.getType().name()));
		}
	}
	
}

package me.dablakbandit.bank.player.event;

import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.event.PlayersEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class BankPlayersLoadedEvent extends PlayersEvent {
	public BankPlayersLoadedEvent(CorePlayers pl, Player player) {
		super(pl, player, true);

	}

	private static final HandlerList handlers = new HandlerList();

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}

package com.pany.game.tasks;

import java.util.concurrent.Callable;

/**
 * A callable task to send results of the game round to a player.
 * Method {@code} call blocks while transferring results to a player.
 */
public class SayResultsTask implements Callable<Void> {

	private final PlayerAnswer player;
	private final PlayerAnswer winner;
	private final PlayerAnswer opponent;

	public SayResultsTask(PlayerAnswer player, PlayerAnswer opponent, PlayerAnswer winner) {
		this.player = player;
		this.winner = winner;
		this.opponent = opponent;
	}

	@Override
	public Void call() {
		player.getPlayer().sendMessage("You said %s, your opponent said %s%n"
				.formatted(player.getAnswer(), opponent.getAnswer()));
		if (winner == null) {
			player.getPlayer().sendMessage("No one wins. Let's play again!\r\n");
		} else if (winner.equals(player)) {
			player.getPlayer().sendMessage("Congratz! You win! Bye-bye!\r\n");
		} else {
			player.getPlayer().sendMessage("Sorry, but you're looser! Bye-bye!\r\n");
		}
		return null;
	}
}

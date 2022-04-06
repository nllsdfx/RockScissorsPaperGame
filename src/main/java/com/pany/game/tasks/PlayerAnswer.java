package com.pany.game.tasks;

import com.pany.game.session.Session;

/**
 * Simple DTO to keep players and its answer from the beginning of the round.
 */
public class PlayerAnswer {

	private final Session player;
	private final String answer;

	public PlayerAnswer(Session player, String answer) {
		this.player = player;
		this.answer = answer;
	}

	public Session getPlayer() {
		return player;
	}

	public String getAnswer() {
		return answer;
	}
}

package com.pany.game.tasks;

import com.pany.game.session.Session;

import java.util.concurrent.Callable;

/**
 * A callable task to notify player of the beginning of the new round
 * and to collect his/her answer. Method {@code call} blocks while transferring messages
 * through IO.
 */
public class NewRoundTask implements Callable<PlayerAnswer> {

	private final Session session;

	public NewRoundTask(Session session) {
		this.session = session;
	}

	@Override
	public PlayerAnswer call() {
		session.sendMessage("A new round started! Type in rock, paper or scissors:\r\n");
		String answer = session.readMessage();
		session.sendMessage("Thanks! Now waiting for your opponent answer...\r\n");
		return new PlayerAnswer(session, answer);
	}
}

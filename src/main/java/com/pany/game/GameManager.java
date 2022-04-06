package com.pany.game;

import com.pany.game.session.Session;

/**
 * Adds a new session to a queue and starts new games.
 */
public interface GameManager {

	/**
	 * Adds session to a queue of sessions await for a beginning of a new game.
	 *
	 * @param session joined server to play a game.
	 */
	void addSessionToPlay(Session session);

	/**
	 * Turns off the game manager.
	 */
	void shutdown();

}

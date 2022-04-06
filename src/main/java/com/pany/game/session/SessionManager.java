package com.pany.game.session;

import com.pany.game.session.Session;

/**
 * Handles incoming sessions.
 */
public interface SessionManager {

	/**
	 * Handles session.
	 *
	 * @param session telnet session.
	 */
	void handleSession(Session session);

	/**
	 * Stops session manager.
	 */
	void shutdown();
}

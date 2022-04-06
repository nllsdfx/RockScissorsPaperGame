package com.pany.game.session;

/**
 * Player's session.
 */
public interface Session {

	/**
	 * Sends message to a player.
	 *
	 * @param message message
	 */
	void sendMessage(String message);

	/**
	 * Reads message from a player.
	 *
	 * @return message.
	 */
	String readMessage();

	/**
	 * Sets player's name.
	 *
	 * @param name name
	 */
	void setName(String name);

	/**
	 * Gets player's name.
	 *
	 * @return name
	 */
	String getName();

	/**
	 * Disconnects player.
	 */
	void disconnect();
}

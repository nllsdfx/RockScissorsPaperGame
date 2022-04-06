package com.pany.server;

/**
 * A simple Telnet Server.
 */
public interface TelnetServer {

	/**
	 * Starts the server.
	 */
	void start();

	/**
	 * Stops the server.
	 */
	void shutdown();

	/**
	 * Gets the server port
	 *
	 * @return server port.
	 */
	int getPort();
}

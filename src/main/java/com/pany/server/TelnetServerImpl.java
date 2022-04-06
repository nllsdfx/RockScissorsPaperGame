package com.pany.server;

import com.pany.game.session.SessionImpl;
import com.pany.game.session.SessionManager;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Component
public class TelnetServerImpl implements TelnetServer, Runnable {

	private final ServerSocket serverSocket;
	private final SessionManager sessionManager;
	private final Thread serverThread = new Thread(this);


	public TelnetServerImpl(ServerSocket serverSocket, SessionManager sessionManager) {
		this.serverSocket = serverSocket;
		this.sessionManager = sessionManager;
	}

	@Override
	public void start() {
		serverThread.start();
	}

	@Override
	public void shutdown() {
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			serverThread.interrupt();
			sessionManager.shutdown();
		}
	}

	@Override
	public int getPort() {
		return serverSocket.getLocalPort();
	}

	@Override
	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			try {
				Socket socket = serverSocket.accept();
				sessionManager.handleSession(new SessionImpl(socket));
			} catch (IOException ignored) {
			}
		}
	}
}

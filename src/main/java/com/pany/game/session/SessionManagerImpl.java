package com.pany.game.session;

import com.pany.game.GameManager;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class SessionManagerImpl implements SessionManager {

	private final ExecutorService executor = Executors.newCachedThreadPool();
	private final GameManager gameManager;

	public SessionManagerImpl(GameManager gameManager) {
		this.gameManager = gameManager;
	}

	@Override
	public void handleSession(Session session) {
		processLogin(session);
	}

	@Override
	public void shutdown() {
		gameManager.shutdown();
		executor.shutdownNow();
	}

	private void processLogin(Session session) {

		var future = executor.submit(() -> {
			session.sendMessage("Enter your name:\r\n");
			String name = session.readMessage();
			session.setName(name);
			session.sendMessage("Welcome, %s! The game will soon begin.%n".formatted(name));
			gameManager.addSessionToPlay(session);
		});

		executor.submit(() -> {
			try {
				future.get(45, TimeUnit.SECONDS);
			} catch (Exception ex) {
				session.disconnect();
				Thread.currentThread().interrupt();
			}
		});
	}
}

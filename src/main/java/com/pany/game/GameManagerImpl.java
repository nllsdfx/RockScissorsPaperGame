package com.pany.game;

import com.pany.game.session.Session;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class GameManagerImpl implements GameManager, Runnable {

	private final BlockingQueue<Session> players = new LinkedBlockingQueue<>();
	private final ExecutorService executorService = Executors.newCachedThreadPool();
	private final Thread gameManagerThread = new Thread(this);

	{
		gameManagerThread.start();
	}

	@Override
	public void addSessionToPlay(Session session) {
		try {
			players.put(session);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	@Override
	public void shutdown() {
		gameManagerThread.interrupt();
		executorService.shutdownNow();
	}

	@Override
	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			try {
				var first = players.take();
				var second = players.take();
				var players = Set.of(first, second);
				executorService.submit(new GameRoom(players));
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}
}

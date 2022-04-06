package com.pany.game;

import com.pany.game.session.Session;
import com.pany.game.tasks.NewRoundTask;
import com.pany.game.tasks.PlayerAnswer;
import com.pany.game.tasks.SayResultsTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * The game itself. Dived into 4 steps:
 * 1. Ask players for scissors, rock or paper
 * 2. Find winner
 * 3. Tell players results
 * 4. If no winner found, then repeat steps 1-4, if found - close sessions and the game.
 */
public class GameRoom implements Runnable {

	private final Set<Session> players = new HashSet<>();
	private final ExecutorService executorService = Executors.newFixedThreadPool(2);
	private final static Map<String, String> COUNTER_STRIKES = new HashMap<>();
	private boolean isPlaying = true;

	static {
		COUNTER_STRIKES.put("scissors", "paper");
		COUNTER_STRIKES.put("paper", "rock");
		COUNTER_STRIKES.put("rock", "scissors");
	}

	public GameRoom(Set<Session> players) {
		this.players.addAll(players);
	}

	/**
	 * Game loop.
	 */
	@Override
	public void run() {
		while (isPlaying && !Thread.currentThread().isInterrupted()) {
			try {
				var futures = roundBegin();
				var answers = List.of(futures.get(0).get(), futures.get(1).get());
				var winner = getWinner(answers);
				tellResults(answers, winner);
				isPlaying = winner == null;
			} catch (Exception ex) {
				Thread.currentThread().interrupt();
				isPlaying = false;
				ex.printStackTrace();
			} finally {
				if (!isPlaying) {
					disconnect();
				}
			}
		}
	}

	private void disconnect() {
		for (Session player : players) {
			player.disconnect();
		}
		executorService.shutdownNow();
	}

	/**
	 * Start a new game round.
	 * Thread blocks for 45 seconds max while waiting for players answers.
	 *
	 * @return list of futures with players' answers.
	 * @throws InterruptedException if interrupted while waiting for answers.
	 */
	private List<Future<PlayerAnswer>> roundBegin() throws InterruptedException {
		var tasks = new ArrayList<NewRoundTask>();
		for (var player : players) {
			tasks.add(new NewRoundTask(player));
		}
		return executorService.invokeAll(tasks, 45, TimeUnit.SECONDS);
	}

	/**
	 * Compares players answers and gets winner, if any.
	 *
	 * @param answers players answers
	 * @return winner's answer.
	 */
	private PlayerAnswer getWinner(List<PlayerAnswer> answers) {
		var first = answers.get(0);
		var second = answers.get(1);
		var firstAnswer = first.getAnswer().toLowerCase(Locale.ROOT);
		var secondAnswer = second.getAnswer().toLowerCase(Locale.ROOT);

		if (firstAnswer.equals(secondAnswer)) {
			return null;
		} else if (secondAnswer.equals(COUNTER_STRIKES.get(firstAnswer))) {
			return first;
		} else if (firstAnswer.equals(COUNTER_STRIKES.get(secondAnswer))) {
			return second;
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Tells results to players at the end of the round.
	 * Thread blocks for 10 secs max while sending results.
	 *
	 * @param playerAnswers players answers
	 * @param winner        winner
	 * @throws InterruptedException if interrupted while waiting for sending results to players.
	 */
	private void tellResults(List<PlayerAnswer> playerAnswers, PlayerAnswer winner) throws InterruptedException {
		var p1 = playerAnswers.get(0);
		var p2 = playerAnswers.get(1);
		var tasks = new ArrayList<SayResultsTask>();
		tasks.add(createTask(p1, p2, winner));
		tasks.add(createTask(p2, p1, winner));
		executorService.invokeAll(tasks, 10, TimeUnit.SECONDS);
	}

	private SayResultsTask createTask(PlayerAnswer player, PlayerAnswer opponent, PlayerAnswer winner) {
		return new SayResultsTask(player, opponent, winner);
	}


}

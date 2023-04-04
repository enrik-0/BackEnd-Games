package edu.uclm.esi.ds.games.services;

import edu.uclm.esi.ds.games.domain.NumberMatch;
import edu.uclm.esi.ds.games.domain.WaitingRoom;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class GameService {
	private WaitingRoom waitingRoom;
	private ConcurrentHashMap<String, NumberMatch> matches;

	public GameService() {
		this.waitingRoom = new WaitingRoom();
		this.matches = new ConcurrentHashMap<>();
	}

	public NumberMatch requestGame(String game, String player) {
		NumberMatch match = waitingRoom.findMatch(game, player);
		if (match.isReady())
			matches.put(match.getId(), match);

		return match;
	}
}

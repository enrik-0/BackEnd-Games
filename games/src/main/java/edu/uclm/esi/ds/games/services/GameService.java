package edu.uclm.esi.ds.games.services;

import edu.uclm.esi.ds.games.domain.Match;
import edu.uclm.esi.ds.games.domain.WaitingRoom;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class GameService {
	private WaitingRoom waitingRoom;
	private ConcurrentHashMap<String, Match> matches;

	public GameService() {
		this.waitingRoom = new WaitingRoom();
		this.matches = new ConcurrentHashMap<>();
	}

	public Match requestGame(String game, String player) {
		Match match = waitingRoom.findMatch(game, player);
		if (match.isReady())
			matches.put(match.getId(), match);

		return match;
	}
}

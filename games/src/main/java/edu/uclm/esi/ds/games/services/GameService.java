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

	public Match requestGame(String juego, String player) {
		Match match = waitingRoom.findMatch(juego, player);
		if (match.isReady())
			matches.put(match.getId(), match);

		return match;
	}
}

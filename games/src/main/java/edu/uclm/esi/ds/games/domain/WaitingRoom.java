package edu.uclm.esi.ds.games.domain;

import java.util.concurrent.ConcurrentHashMap;

public class WaitingRoom {

	private ConcurrentHashMap<String, Match> matches;

	public WaitingRoom() {
		this.matches = new ConcurrentHashMap<>();
	}

	public Match findMatch(String juego, String player) {
		Match match = matches.get(juego);
		if (match == null) {
			match = new Match();
			match.addPlayer(player);
			this.matches.put(juego, match);
		} else {
			match.addPlayer(player);
			this.matches.remove(juego, match);
		}

		return match;
	}

}

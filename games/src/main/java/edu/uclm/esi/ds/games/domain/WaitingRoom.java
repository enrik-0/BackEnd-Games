package edu.uclm.esi.ds.games.domain;

import java.util.concurrent.ConcurrentHashMap;

public class WaitingRoom {

	private ConcurrentHashMap<String, NumberMatch> matches;

	public WaitingRoom() {
		this.matches = new ConcurrentHashMap<>();
	}

	public NumberMatch findMatch(String game, String player) {
		NumberMatch match = matches.get(game);
		if (match == null) {
			match = new NumberMatch();
			match.addPlayer(player);
			this.matches.put(game, match);
		} else {
			match.addPlayer(player);
			this.matches.remove(game, match);
		}

		return match;
	}

}

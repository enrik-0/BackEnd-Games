package edu.uclm.esi.ds.games.domain;

import java.util.concurrent.ConcurrentHashMap;

import edu.uclm.esi.ds.games.entities.Player;

public class WaitingRoom {
	// { "MatchName": Match }
	private ConcurrentHashMap<String, Match> matches;

	public WaitingRoom() {
		this.matches = new ConcurrentHashMap<>();
	}

	public Match findMatch(String game, Player player) {
		Match match = matches.get(game);

		if (match == null) {
			match = createMatch(game);
			match.addPlayer(player);
			this.matches.put(game, match);
		} else {
			match.addPlayer(player);
			this.matches.remove(game, match);
		}

		return match;
	}

	private Match createMatch(String game) {
		Match match = null;
		
		if (game.equals(GameName.nm.toString())) {
			match = new NumberMatch();
		}
		
		return match;
	}
}

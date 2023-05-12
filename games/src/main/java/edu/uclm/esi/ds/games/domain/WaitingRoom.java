package edu.uclm.esi.ds.games.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import edu.uclm.esi.ds.games.entities.Match;
import edu.uclm.esi.ds.games.entities.NumberMatch;
import edu.uclm.esi.ds.games.entities.Player;

public class WaitingRoom {
	// { "MatchName": Match }
	private ConcurrentHashMap<String, Match> matches;
	private ConcurrentHashMap<String, List<Match>> customMatches;

	public WaitingRoom() {
		this.matches = new ConcurrentHashMap<>();
		this.customMatches = new ConcurrentHashMap<>();
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
	
	public Match createCustomMatch(String game, Player player) {
		Match match = createMatch(game);
		
		if (this.customMatches.get(game) == null) {
			this.customMatches.put(game, new ArrayList<Match>());
		}

		this.customMatches.get(game).add(match);
		match.addPlayer(player);

		return match;
	}
	
	public Match joinCustomMatch(String game, Player player, String code) {
		List<Match> matchList = this.customMatches.get(game);

		// check there is custom Match for that game
		if (matchList == null)
			return null;

		// check the code exists in a custom match
		Match match = matchList.stream()
				.filter(m -> m.getId().equals(code))
				.findFirst().orElse(null);

		if (match != null) {
			match.addPlayer(player);
			this.customMatches.get(game).remove(match);
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

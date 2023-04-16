package edu.uclm.esi.ds.games.services;

import edu.uclm.esi.ds.games.domain.Match;
import edu.uclm.esi.ds.games.domain.WaitingRoom;
import edu.uclm.esi.ds.games.entities.Player;

import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class GameService {
	private WaitingRoom waitingRoom;
	// { "idMatch": Match }
	private ConcurrentHashMap<String, Match> matches;

	public GameService() {
		this.waitingRoom = new WaitingRoom();
		this.matches = new ConcurrentHashMap<>();
	}

	public Match requestGame(String game, JSONObject player) {
		Match match = waitingRoom.findMatch(game, createPlayer(player));

		if (match.isReady())
			matches.put(match.getId(), match);

		return match;
	}
	
	private Player createPlayer(JSONObject player) {
		return new Player(
					player.getString("id"),
					player.getString("name"),
					player.getString("email")
				);
	}

	public Match getMatch(String idMatch) {
		return this.matches.get(idMatch);
	}
}

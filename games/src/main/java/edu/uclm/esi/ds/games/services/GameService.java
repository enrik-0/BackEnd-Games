package edu.uclm.esi.ds.games.services;

import edu.uclm.esi.ds.games.dao.MatchDAO;
import edu.uclm.esi.ds.games.domain.WaitingRoom;
import edu.uclm.esi.ds.games.entities.Match;
import edu.uclm.esi.ds.games.entities.Player;

import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {
	private WaitingRoom waitingRoom;
	// { "idMatch": Match }
	private ConcurrentHashMap<String, Match> matches;
	@Autowired
	private MatchDAO matchDAO;

	public GameService() {
		this.waitingRoom = new WaitingRoom();
		this.matches = new ConcurrentHashMap<>();
	}

	public Match requestGame(String game, JSONObject player) {
		Match match = waitingRoom.findMatch(game, createPlayer(player));

		if (match.isReady()) {
			matches.put(match.getId(), match);
			//this.matchDAO.save(match);
		}

		return match;
	}

	public Match createCustomMatch(String game, JSONObject player) {
		return waitingRoom.createCustomMatch(game, createPlayer(player));
	}

	public Match joinCustomMatch(String game, JSONObject player, String code) {
		Match match = this.waitingRoom.joinCustomMatch(game, createPlayer(player), code);

		if (match == null) return null;
		if (match.isReady())
			matches.put(match.getId(), match);

		return match;
	}

	public Match getMatch(String idMatch) {
		return this.matches.get(idMatch);
	}

	public void saveMatch(Match match) {
		this.matchDAO.save(match);
	}
	
	private Player createPlayer(JSONObject player) {
		return new Player(
					player.getString("id"),
					player.getString("name"),
					player.getString("email")
				);
	}
}

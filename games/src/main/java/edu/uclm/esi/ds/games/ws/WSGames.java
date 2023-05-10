package edu.uclm.esi.ds.games.ws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import edu.uclm.esi.ds.games.domain.Match;
import edu.uclm.esi.ds.games.entities.MatchPlayer;
import edu.uclm.esi.ds.games.entities.Player;
import edu.uclm.esi.ds.games.entities.User;
import edu.uclm.esi.ds.games.exceptions.BoardIsFullException;
import edu.uclm.esi.ds.games.services.APIService;
import edu.uclm.esi.ds.games.services.GameService;

@Component
public class WSGames extends TextWebSocketHandler {
	private GameService gameService;
	private APIService apiService;
	private HashMap<String, ArrayList<WebSocketSession>> sessions = new HashMap<>();

	public WSGames(GameService games, APIService api) {
		this.gameService = games;
		this.apiService = api;
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		String uri = session.getUri().getQuery();
		String idMatch = uri.split("=")[1];
		System.out.println("conectado");
		
		if (this.sessions.get(idMatch) == null) {
			this.sessions.put(idMatch, new ArrayList<WebSocketSession>());
		}
		
		this.sessions.get(idMatch).add(session);
		this.sendToMatch(this.sessions.get(idMatch), "type", "OK");
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String payload = message.getPayload();
		JSONObject jso = new JSONObject(payload);

		try {
			this.handleMessage(session, jso);
		} catch (JSONException e) {
			this.sendToSession(session, "type", "ERROR", "message", "JSON format error");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			this.sendToSession(session, "type", "ERROR", "message", "Unexpected error");
		}
	}

	private void handleMessage(WebSocketSession session, JSONObject jso) throws Exception {
		String type = jso.getString("type");

		if (type.equals("PLAYER READY")) {
			this.notifyIfMatchStarted(jso);
		} else if (type.equals("MOVEMENT")) { 
			this.move(jso);
		} else if (type.equals("ADD NUMBERS")) {
			this.addNumbers(jso);
		} else if (type.equals("CHAT")) {
			this.chat(jso);
		} else {
			this.sendToSession(session, "type", "ERROR", "message", "Unknown message");
		}
	}

	private void notifyIfMatchStarted(JSONObject jso) throws JSONException {
		String idMatch = jso.getString("idMatch");
		Match match = this.gameService.getMatch(idMatch);

		if (match != null) {
			JSONObject board = new JSONObject(match.getBoards().get(0));
			JSONArray players = new JSONArray(match.getPlayersNames());
			System.out.println("enviar partida");
			this.sendToMatch(this.sessions.get(idMatch), "type", "MATCH STARTED", "idMatch", idMatch,
					"players", players.toString(), "board", board.toString());
		}
	}	

	private void sendToMatch(ArrayList<WebSocketSession> sessions, String... tv) throws JSONException {
		JSONObject jso = new JSONObject();
		for (int i = 0; i < tv.length; i += 2)
			jso.put(tv[i], tv[i + 1]);

		TextMessage message = new TextMessage(jso.toString());

		for (WebSocketSession client : sessions) {
			Runnable r = new Runnable() {
				@Override
				public void run() {
					try {
						client.sendMessage(message);
					} catch (IOException e) {
						WSGames.this.sessions.values().forEach(value -> value.remove(client));
					}
				}
			};
			new Thread(r).start();
		}
	}


	private void sendToSession(WebSocketSession session, String... tv) {
		JSONObject jso = new JSONObject();

		for (int i = 0; i < tv.length; i += 2)
			jso.put(tv[i], tv[i + 1]);

		TextMessage message = new TextMessage(jso.toString());

		try {
			session.sendMessage(message);
		} catch (IOException e) {
			this.sessions.values().forEach(value -> value.remove(session));
		}
	}

	private void move(JSONObject jso) throws Exception {
		String idMatch = jso.getString("idMatch");
		String sessionID = jso.getString("sessionID");
		JSONArray move = jso.getJSONArray("movement");
		Match match = this.gameService.getMatch(idMatch);
		JSONObject userJson = this.getUser(sessionID);
		User user = null;
		if (userJson != null)
			user = new Player(userJson.getString("id"),
					userJson.getString("name"), userJson.getString("email"));

			
		boolean isWin = false;

		if (match != null && userJson != null) {
			if (this.isValidMovement(match, user, move)) {
				isWin = this.updateBoard(match, userJson.getString("id"), move);
				this.sendUpdate(this.sessions.get(idMatch), match, sessionID, userJson.getString("id"));
				if (isWin) {
					this.sendToMatch(this.sessions.get(idMatch), "type", "WIN", "sessionID", sessionID);
					match.getPlayerById(user.getId()).setWinner(true);
					this.gameService.saveMatch(match);
				}
			} else {
				this.sendToMatch(this.sessions.get(idMatch), "type", "INVALID MOVE", "message", "Movement is not valid!");
			}
		}
	}

	private boolean isValidMovement(Match match, User user, JSONArray move) throws Exception, JSONException {
		boolean valid = false;

		if (match.isValidMovement(user, move.getInt(0), move.getInt(1))) 
			valid = true;
		
		return valid;
	}

	private boolean updateBoard(Match match, String userId, JSONArray move) throws Exception {
		return match.updateUserBoard(userId, move.getInt(0), move.getInt(1));
	}

	private void sendUpdate(ArrayList<WebSocketSession> sessions, Match match, String sessionID, String userId) {
		JSONObject board = new JSONObject(match.getPlayerBoard(userId));

		this.sendToMatch(sessions, "type", "UPDATE", "idMatch", match.getId(),
				"sessionID", sessionID, "board", board.toString());
	}
	
	private void addNumbers(JSONObject jso) throws JSONException {
		String idMatch = jso.getString("idMatch");
		String sessionID = jso.getString("sessionID");
		Match match = this.gameService.getMatch(idMatch);
		JSONObject userJson = this.getUser(sessionID);
		
		try {
			match.getPlayerBoard(userJson.getString("id")).addNumbers();
			this.sendUpdate(this.sessions.get(idMatch), match, sessionID, userJson.getString("id"));
		} catch (BoardIsFullException e) {
			this.sendToMatch(this.sessions.get(idMatch), "type", "LOSE", "sessionID", sessionID);
			this.getDiffPlayerFromId(match, userJson).setWinner(true);;
			this.gameService.saveMatch(match);
		};
	}

	private void chat(JSONObject jso) {
		String idMatch = jso.getString("idMatch");
		String sessionID = jso.getString("sessionID");
		String msg = jso.getString("message");
		Match match = this.gameService.getMatch(idMatch);
		JSONObject userJson = this.getUser(sessionID);
		
		if (match != null && userJson != null) {
			this.sendToMatch(this.sessions.get(match.getId()), "type", "CHAT MESSAGE",
					"username", userJson.getString("name"), "text", msg);
		}
	}

	private MatchPlayer getDiffPlayerFromId(Match match, JSONObject userJson) throws JSONException {
		MatchPlayer player = null;

		for (MatchPlayer p : match.getPlayers())
			if (p.getId().getPlayer() != userJson.getString("id"))
				player = p;
		
		return player;
	}

	private JSONObject getUser(String sessionID) {
		return this.apiService.getUser(sessionID);
	}

	@Override
	protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
	}

	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws JSONException {
		String uri = session.getUri().getQuery();
		String idMatch = uri.split("=")[1];

		this.sessions.get(idMatch).remove(session);
		if (this.sessions.get(idMatch).isEmpty()) {
			this.sessions.remove(idMatch);
		}

		this.sendToMatch(this.sessions.get(idMatch), "type", "BYE", "message", "Un usuario se ha ido");
		System.out.println("closed");
	}
}
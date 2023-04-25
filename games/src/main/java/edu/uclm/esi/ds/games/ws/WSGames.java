package edu.uclm.esi.ds.games.ws;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import edu.uclm.esi.ds.games.domain.Match;
import edu.uclm.esi.ds.games.entities.User;
import edu.uclm.esi.ds.games.services.APIService;
import edu.uclm.esi.ds.games.services.GameService;

@Component
public class WSGames extends TextWebSocketHandler {
	@Autowired
	private GameService gameService;
	@Autowired
	private APIService apiService;
	private ArrayList<WebSocketSession> sessions = new ArrayList<>();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		this.sessions.add(session);
		this.send(session, "type", "OK", "message", "Session opened succesfully");
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String payload = message.getPayload();
		JSONObject jso = new JSONObject(payload);
		String type = jso.getString("type");

		try {
			if (type.equals("MOVEMENT")) { 
				this.move(jso);
			} else if (type.equals("PLAYER READY")) {
				String idMatch = jso.getString("idMatch");
				Match match = this.gameService.getMatch(idMatch);

				if (match != null) {
					this.send(session, "type", "MATCH STARTED", "idMatch", idMatch);
				}
			} else if (type.equals("CHAT")) {
				this.chat(jso);
			} else if (type.equals("BROADCAST")) {
				this.broadcast(jso);
			} else {
				this.send(session,"type","ERROR","message","Unknown message");
			}
		} catch (JSONException e) {
			this.send(session, "type", "ERROR", "message", "JSON format error");
		}
	}	

	private void send(WebSocketSession session, String... tv) throws JSONException {
		JSONObject jso = new JSONObject();
		for (int i = 0; i < tv.length; i += 2)
			jso.put(tv[i], tv[i + 1]);

		TextMessage message = new TextMessage(jso.toString());

		try {
			session.sendMessage(message);
		} catch (IOException e) {
			this.sessions.remove(session);
		}
	}

	private void move(JSONObject jso) throws Exception {
		String idMatch = jso.getString("idMatch");
		String sessionID = jso.getString("sessionID");
		JSONArray move = jso.getJSONArray("movement");

		if (this.checkMovement(idMatch, sessionID, move)) {
			// updateBoard()
			// sendUpdate()
		} else {
			// error movement not valid.
		}
	}

	private boolean checkMovement(String idMatch, String sessionID, JSONArray move) throws Exception, JSONException {
		boolean valid = false;
		Match match = this.gameService.getMatch(idMatch);

		if (match != null) {
			JSONObject userJson = this.apiService.getUser(sessionID);
			if (!match.isValidMovement(userJson.getString("id"),
					(int) move.get(0), (int) move.get(1))) {
				valid = true;
			}
		}
		
		return valid;
	}

	private void chat(JSONObject jso) {
		// TODO Auto-generated method stub
		
	}
	
	public void broadcast(JSONObject jso) throws JSONException {
		TextMessage message = new TextMessage(jso.getString("message"));
		for (WebSocketSession client : this.sessions) {
			Runnable r = new Runnable() {
				@Override
				public void run() {
					try {
						client.sendMessage(message);
					} catch (IOException e) {
						WSGames.this.sessions.remove(client);
					}
				}
			};
			new Thread(r).start();
		}
	}

	@Override
	protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
	}

	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws JSONException {
		this.sessions.remove(session);
		JSONObject jso = new JSONObject();
		jso.put("type", "BYE");
		jso.put("message", "Un usuario se ha ido");
		this.broadcast(jso);
	}
}
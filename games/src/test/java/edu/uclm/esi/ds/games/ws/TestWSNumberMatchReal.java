package edu.uclm.esi.ds.games.ws;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.RequestHeadersSpec;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

import edu.uclm.esi.ds.games.domain.GameName;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class TestWSNumberMatchReal implements IWSReceiver {
	private WebTestClient server;
	
	private String pepeSessionID;
	private String anaSessionID;
	
	private WSClient wsPepe;
	private WSClient wsAna;
	
	/**
	 * Set up the environment for testing.
	 */
	@BeforeAll
	void setUp() throws Exception {
		this.server = WebTestClient.bindToServer()
				.baseUrl("http://localhost").build();
		
		this.register("Pepe");
		this.pepeSessionID = this.login("Pepe");
		
		this.register("Ana");
		this.anaSessionID = this.login("Ana");
	}
	
	@Test @Order(1)
	void testRedirection() throws Exception {
		// Build HTTP GET request.
		RequestHeadersSpec<?> request = this.server.get().uri("/games/requestGame?game=nm");

		// Execute request in server.
		ResponseSpec response = request.exchange();
		
		response.expectStatus().isFound();
	}

	@Test @Order(2)
	void testRequestMatch() throws Exception {
		JSONObject jsonPepe = this.sendGameRequest(GameName.nm.toString(), pepeSessionID);
		System.out.println(jsonPepe.toString());

		JSONObject jsonAna = this.sendGameRequest(GameName.nm.toString(), anaSessionID);
		System.out.println(jsonAna.toString());

		assertEquals(jsonPepe.getString("id"), jsonAna.getString("id"));
		assertFalse(jsonPepe.getBoolean("ready"));
		assertTrue(jsonAna.getBoolean("ready"));
		assertTrue(jsonAna.getJSONArray("players").length() == 2);

		this.wsPepe = this.connectWebsocket("ws://localhost:80/wsGames", this);
		this.wsAna = this.connectWebsocket("ws://localhost:80/wsGames", this);
	}

	@Test @Order(3)
	void RejectRequest() throws Exception {
		RequestHeadersSpec<?> request = this.server.get().uri("/games/requestGame?game=trivial");
		ResponseSpec response = request.exchange();
		
		response.expectStatus().isNotFound();
	}

	private void register(String player) throws Exception {
		JSONObject jso = new JSONObject()
				.put("name", player)
				.put("email", player+"@"+player)
				.put("pwd1", player+"123")
				.put("pwd2", player+"123");
		
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost request = new HttpPost("http://localhost:8080/users/register");

		StringEntity params = new StringEntity(jso.toString());
		request.addHeader("Content-type", "application/json");
		request.setEntity(params);

		httpClient.execute(request);
	}

	private String login(String player) throws Exception {
		String sessionID = null;
		JSONObject jso = new JSONObject()
				.put("name", player)
				.put("pwd", org.apache.commons.codec.digest.DigestUtils.sha512Hex(player+"123"));
		
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPut request = new HttpPut("http://localhost:8080/users/login");

		StringEntity params = new StringEntity(jso.toString());
		request.addHeader("Content-type", "application/json");
		request.setEntity(params);

		HttpResponse response = httpClient.execute(request);	
		Header headerSessionID = response.getFirstHeader("sessionID");
		sessionID = headerSessionID.getValue();

		return sessionID;
	}
	
	private JSONObject sendGameRequest(String game, String sessionID) throws JSONException {
		// Build HTTP GET request.
		RequestHeadersSpec<?> request = this.server.get()
				.uri("/games/requestGame?game=nm")
				.header("sessionID", sessionID);

		// Execute request in server.
		ResponseSpec response = request.exchange();

		// Get body of response.
		EntityExchangeResult<String> result = response.expectBody(String.class).returnResult();
		String body = result.getResponseBody();	
		
		return new JSONObject(body);
	}

	private WSClient connectWebsocket(String url, IWSReceiver receiver) throws Exception {
		WSClient client = new WSClient();

		client.setReceiver(receiver);
		client.openConnection(url);

		return client;
	}

	@Override
	public void receive(WSClient client, int event, String message) {
		if (event != WSStates.ON_OPEN.ordinal())
			fail("Unexpected message in the WebSocket");
	}
}

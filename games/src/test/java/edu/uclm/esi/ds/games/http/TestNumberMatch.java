package edu.uclm.esi.ds.games.http;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import edu.uclm.esi.ds.games.domain.GameName;
@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class TestNumberMatch {
	@Autowired
	private MockMvc server;
	
	@Test @Order(1)
	void testRedirecc() throws Exception {
		ResultActions response = 
				sendGameRequest(GameName.nm.toString(), "1234");

		response.andExpect(status().isUnauthorized());
	}

	@Test @Order(2)
	void testRequestMatch() throws Exception {
		register("Pepe");
		String sessionID = login("Pepe");
		String payload = getResponseGameRequest(sessionID);
		JSONObject jsonPepe = new JSONObject(payload);
		System.out.println(jsonPepe.toString());

		register("Ana");
		sessionID = login("Ana");
		String payloadAna = getResponseGameRequest(sessionID);
		JSONObject jsonAna = new JSONObject(payloadAna);
		System.out.println(jsonAna.toString());
	}

	@Test @Order(3)
	void RejectRequest() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/games/requestGame?game=trivial");
		this.server.perform(request).andExpect(status().isNotFound());
	}

	private String login(String player) throws JSONException {
		String sessionID = null;
		JSONObject jso = new JSONObject();
		jso.put("name", player);
		jso.put("pwd", org.apache.commons.codec.digest.DigestUtils.sha512Hex(player));
		
		HttpClient httpClient = HttpClientBuilder.create().build();
		try {
		    HttpPut request = new HttpPut("http://localhost:8080/users/login");

		    StringEntity params = new StringEntity(jso.toString());
		    request.addHeader("Content-type", "application/json");
		    request.setEntity(params);

		    HttpResponse response = httpClient.execute(request);
		    Header headerSessionID = response.getFirstHeader("sessionID");
		    sessionID = headerSessionID.getValue();
		} catch (Exception ex) {
		}

		return sessionID;
	}

	private ResultActions sendGameRequest(String game, String sessionID) throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/games/requestGame?game=" + game)
				.header("sessionID", sessionID);
		ResultActions response = this.server.perform(request);

		return response;
	}

	private String getResponseGameRequest(String sessionID) throws Exception, UnsupportedEncodingException {
		ResultActions response = sendGameRequest(GameName.nm.toString(), sessionID);
		MvcResult result = response.andExpect(status().isOk()).andReturn();
		MockHttpServletResponse http = result.getResponse();
		String payload = http.getContentAsString();

		return payload;
	}

	private void register(String player) throws Exception {
		JSONObject jso = new JSONObject();
		jso.put("name", player);
		jso.put("email", player);
		jso.put("pwd1", org.apache.commons.codec.digest.DigestUtils.sha512Hex(player));
		jso.put("pwd2", org.apache.commons.codec.digest.DigestUtils.sha512Hex(player));
		
		HttpClient httpClient = HttpClientBuilder.create().build();

		HttpPost request = new HttpPost("http://localhost:8080/users/register");
		StringEntity params = new StringEntity(jso.toString());
		request.addHeader("Content-type", "application/json");
		request.setEntity(params);
		httpClient.execute(request);
	}
}

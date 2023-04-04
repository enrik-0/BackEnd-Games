package edu.uclm.esi.ds.games.http;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
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

import edu.uclm.esi.ds.games.dao.UserDAO;
import edu.uclm.esi.ds.games.domain.GameName;
import edu.uclm.esi.ds.games.entities.User;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class TestGames {

	@Autowired
	private MockMvc server;
	@Autowired
	private UserDAO userDao;
	
	@Test
	void testRedirecc() throws Exception {
		ResultActions response = 
				createRequest(GameName.nm.toString(), "Maria");
		response.andExpect(status().isPermanentRedirect());
		
	}
	@Test
	void testRequestMatch() throws Exception {
		createUsers();
		String payload = sendRequest("Pepe");
		JSONObject jsonPepe = new JSONObject(payload);
		String payloadAna = sendRequest("Ana");
		assertFalse(jsonPepe.getBoolean("ready"));
		JSONObject jsonAna = new JSONObject(payloadAna);
		assertTrue(jsonAna.getBoolean("ready"));
		assertTrue(jsonAna.getJSONArray("players").length() == 2);

	}
	private ResultActions createRequest(String game, String player) throws Exception {
		JSONObject jso = new JSONObject();
		jso.put("game", game);
		jso.put("player", player);
		RequestBuilder request = MockMvcRequestBuilders.get("/games/requestGame?")
				.contentType("application/json")
				.content(jso.toString());
		ResultActions response = this.server.perform(request);
		return response;
	}

	private String sendRequest(String player) throws Exception, UnsupportedEncodingException {
		
		ResultActions response = 
				createRequest(GameName.nm.toString(), player);
		MvcResult result = response.andExpect(status().isOk()).andReturn();
		MockHttpServletResponse http = result.getResponse();
		String payload = http.getContentAsString();
		return payload;
	}

	@Test
	void RejectRequest() throws Exception {
		JSONObject jso = new JSONObject();
		jso.put("game", "trivial");
		jso.put("player", "maria");
		RequestBuilder request = MockMvcRequestBuilders.get("/games/requestGame?")
				.contentType("application/json")
				.content(jso.toString());
		this.server.perform(request).andExpect(status().isNotFound());
	}
	private void createUsers() {
		User user = new User();
		user.setName("Pepe");
		this.userDao.save(user);
		User us = new User();
		us.setName("Ana");
		this.userDao.save(us);
	}
}

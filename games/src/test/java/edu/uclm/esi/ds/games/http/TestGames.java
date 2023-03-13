package edu.uclm.esi.ds.games.http;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.io.UnsupportedEncodingException;
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

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class TestGames {

	@Autowired
	private MockMvc server;

	@Test
	void testRequestMatch() throws Exception {
		String payload = sendRequest("Pepe");
		JSONObject jsonPepe = new JSONObject(payload);
		String payloadAna = sendRequest("Ana");
		assertFalse(jsonPepe.getBoolean("ready"));
		JSONObject jsonAna = new JSONObject(payloadAna);
		assertTrue(jsonAna.getBoolean("ready"));
		assertTrue(jsonAna.getJSONArray("players").length() == 2);

	}

	private String sendRequest(String player) throws Exception, UnsupportedEncodingException {
		RequestBuilder request = MockMvcRequestBuilders.get("/games/requestGame?juego=nm&player=" + player);
		ResultActions response = this.server.perform(request);
		MvcResult result = response.andExpect(status().isOk()).andReturn();
		MockHttpServletResponse http = result.getResponse();
		String payload = http.getContentAsString();
		return payload;
	}

	@Test
	void RejectRequest() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/games/requestGame?juego=trivial&player=Maria");
		this.server.perform(request).andExpect(status().isNotFound());
	}
}

package edu.uclm.esi.ds.games.http;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
public class TestAPI {

	@Autowired
	private MockMvc server;

	
	@Test
	public void testNewUser() throws Exception {
		ResultActions response = this.sendRequest("pepe", "http://hacker@hacher.com", "lkj");
		MvcResult result =response.andExpect(status().isForbidden()).andReturn();
		MockHttpServletResponse http = result.getResponse();
		String payload  = http.getContentAsString();
		assertTrue(payload.equals("Invalid CORS request"));

	}

	
	private ResultActions sendRequest(String name, String origin, String property) throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.put("http://localhost:80/api/newUser?name=" + name)
				.header("origin",  origin)
				.header("Property", property);
		ResultActions response = this.server.perform(request);
		return response;
	}


	
	

}

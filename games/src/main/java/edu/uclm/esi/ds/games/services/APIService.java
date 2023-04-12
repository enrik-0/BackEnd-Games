package edu.uclm.esi.ds.games.services;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class APIService {
	private String baseURL;
	private CloseableHttpClient http;
	
	public APIService() {
		this.baseURL = "http://localhost:8080/api/";
		this.http = HttpClientBuilder.create().build();
	}
	
	/**
	 * Make an HTTP GET request to the /getUser service of the Accounts API.
	 * @param id: ID of the user.
	 * @return user json or null if user don't exists.
	 * JSON format is { "id": "...", "name": "...", "email": "..." }
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public JSONObject getUser(String id) throws ClientProtocolException, IOException {
		JSONObject json = null;

		HttpGet request = new HttpGet(this.baseURL + "getUser?id=" + id);
		CloseableHttpResponse response = this.http.execute(request);
		int code = response.getStatusLine().getStatusCode();
		
		if (code >= 200 && code < 300) {
			String payload = response.getEntity().toString();
			json = new JSONObject(payload);
		}
		
		response.close();
		
		return json;
	}
}

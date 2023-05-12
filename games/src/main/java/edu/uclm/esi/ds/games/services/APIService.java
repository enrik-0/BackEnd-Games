package edu.uclm.esi.ds.games.services;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class APIService {
	private final String baseURL;
	
	public APIService() {
		this.baseURL = "http://localhost:8080/api/";
	}
	
	/**
	 * Make an HTTP GET request to the /getUser service of the Accounts API.
	 * 
	 * @param sessionID: ID of the user.
	 * @return user json or null if user don't exists.
	 * JSON format is { "id": "...", "name": "...", "email": "...", "points": ... }
	 */
	public JSONObject getUser(String sessionID) {
		JSONObject json = null;

		try (CloseableHttpClient http = HttpClientBuilder.create().build()) {
			HttpGet request = new HttpGet(this.baseURL + "getUser?sessionID=" + sessionID);
			CloseableHttpResponse response = http.execute(request);
			int code = response.getStatusLine().getStatusCode();
		
			if (code >= 200 && code < 300) {
				String payload = EntityUtils.toString(response.getEntity());
				json = new JSONObject(payload);
			}

			response.close();
		} catch (Exception e) {
			json = null;
		}
		
		return json;
	}

	/**
	 * Make an HTTP PUT request to the /updatePoints service of the Accounts API.
	 * 
	 * @param sessionID: ID of the user.
	 * @param ammount: ammount of points to update.
	 * @param add: true if update is for adding points, false if is for removing points.
	 * @return true if operation is succesful, false otherwise.
	 */
	public boolean updatePoints(String sessionID, int ammount) {
		boolean success = false;

		try (CloseableHttpClient http = HttpClientBuilder.create().build()) {
			HttpPut request = new HttpPut(
					this.baseURL + "updatePoints?sessionID=" + sessionID +
					"&ammount=" + ammount
					);
			CloseableHttpResponse response = http.execute(request);
			int code = response.getStatusLine().getStatusCode();
		
			if (code >= 200 && code < 300) {
				success = true;
			}

			response.close();
		} catch (Exception e) {
			success = false;
		}

		return success;
	}
}

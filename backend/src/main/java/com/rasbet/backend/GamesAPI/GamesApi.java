package com.rasbet.backend.GamesAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.rasbet.backend.Database.EventsDB;
import com.rasbet.backend.Entities.Event;
import com.rasbet.backend.Entities.Odd;
import com.rasbet.backend.Exceptions.readJsonException;

public class GamesApi {

	private static final String GET_URL = "http://ucras.di.uminho.pt/v1/games";

	// Read all api output
	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	// Populate the JSONObject
	// Change to use API
	public static JSONObject readJsonFromUrl() throws IOException, JSONException {
		URL url = new URL(GET_URL);
		HttpURLConnection huc = (HttpURLConnection) url.openConnection();
    	HttpURLConnection.setFollowRedirects(false);
    	huc.setConnectTimeout(10 * 1000);
    	huc.setRequestMethod("GET");
    	huc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
    	huc.connect();
		InputStream is = huc.getInputStream();


		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = "{\'futebol\': " + readAll(rd) + "}";
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

	public static ArrayList<Event> getEvents() throws Exception {
		try {
			ArrayList<Event> events = new ArrayList<>();

			// Get info from API
			JSONObject json = readJsonFromUrl();
			JSONArray jsonArray = json.getJSONArray("futebol");

			// Go through all events
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonEvent = jsonArray.getJSONObject(i);
				JSONArray bookmakers = jsonEvent.getJSONArray("bookmakers");

				// Get newest odds for a event
				LocalDateTime lastUpdate = null;
				JSONArray markets = null;
				for (int j = 0; j < bookmakers.length(); j++) {
					String string = bookmakers.getJSONObject(j).getString("lastUpdate");
					LocalDateTime new_lastUpdate = LocalDateTime.parse(string.substring(0, string.length() - 1));
					if (lastUpdate == null || new_lastUpdate.isAfter(lastUpdate)) {
						lastUpdate = new_lastUpdate;
						markets = bookmakers.getJSONObject(j).getJSONArray("markets");
					}
				}

				// Get all the odds
				if (markets == null)
					throw new readJsonException("No markets found");

				JSONArray odds_json = markets.getJSONObject(0).getJSONArray("outcomes");
				Map<String, Odd> odds = new HashMap<>();

				for (int j = 0; j < odds_json.length(); j++) {
					JSONObject odd_json = odds_json.getJSONObject(j);
					odds.put(odd_json.getString("name"),
							new Odd(odd_json.getString("name"), odd_json.getDouble("price"), false));
				}

				// Create the event
				DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
				String sport = EventsDB.FOOTBALL;
				String result = jsonEvent.getString("completed") == "true"
						&& !jsonEvent.getString("scores").equals("null") ? jsonEvent.getString("scores") : null;
				String description = jsonEvent.get("homeTeam") + " v " + jsonEvent.get("awayTeam");
				events.add(new Event(jsonEvent.getString("id"), sport,LocalDateTime.parse(jsonEvent.getString("commenceTime"), formatter), description,
						result, null, odds));
			}
			return events;
		} catch (JSONException | readJsonException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return null;
		}
	}
}

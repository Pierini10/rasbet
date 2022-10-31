package com.rasbet.backend.GamesAPI;

import com.rasbet.backend.Database.EventsDB;
import com.rasbet.backend.Entities.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.time.LocalDateTime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
	public static JSONObject readJsonFromUrl() throws IOException, JSONException {
		InputStream is = new URL(GET_URL).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = "{\'futebol\': " + readAll(rd) + "}";
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

	public static ArrayList<Event> getEvents() {
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
				JSONArray odds_json = markets.getJSONObject(0).getJSONArray("outcomes");
				Map<String, Odd> odds = new HashMap<>();

				for (int j = 0; j < odds_json.length(); j++) {
					JSONObject odd_json = odds_json.getJSONObject(j);
					odds.put(odd_json.getString("name"),
							new Odd(odd_json.getString("name"), odd_json.getDouble("price"), false));
				}

				// Create the event
				String sport = EventsDB.FOOTBALL; // TODO
				String result = jsonEvent.getString("completed") == "true"
						&& !jsonEvent.getString("scores").equals("null") ? jsonEvent.getString("scores") : null;
				String description = jsonEvent.get("homeTeam") + " v " + jsonEvent.get("awayTeam");
				events.add(new Event(jsonEvent.getString("id"), sport, jsonEvent.getString("commenceTime"), description,
						result, null, odds));
			}
			return events;
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return null;
		}
	}
}

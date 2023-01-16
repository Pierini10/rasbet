package com.rasbet.backend.GamesAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.util.Pair;

import com.rasbet.backend.Database.SportsDB;
import com.rasbet.backend.Entities.Event;
import com.rasbet.backend.Entities.Odd;

public class GamesApi {

    private static final int MAX_RETRIES = 3;
	private static final String GET_URL = "http://ucras.di.uminho.pt/v1/games";
	private static final String SPORTS_API_URL = "https://api.the-odds-api.com";
	private static final String SPORTS_API_KEY = "e54ee6aaa6143d3ed4d01dda39f95630";

	private static final List<String> sports = Stream.of(
			"soccer_epl",
			"soccer_fifa_world_cup",
			"basketball_nba",
			"soccer_fifa_world_cup_winner").collect(Collectors.toList());

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
	public static JSONArray readJsonFromUrl(String api_url) throws IOException, JSONException {
		URL url = new URL(api_url);
		HttpURLConnection huc = (HttpURLConnection) url.openConnection();
		HttpURLConnection.setFollowRedirects(false);
		huc.setConnectTimeout(10 * 1000);
		huc.setRequestMethod("GET");
		huc.connect();
		InputStream is = huc.getInputStream();
		//System.out.println("Response code: " + api_url + " " + huc.getResponseCode());

		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONArray json = new JSONArray(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

	private static String build_odds_sports_api_URL(String sport, String region) {
		return SPORTS_API_URL + "/v4/sports/" + sport +
				"/odds/?apiKey=" + SPORTS_API_KEY +
				"&regions=" + region;
	}

	private static String build_scores_sports_api_URL(String sport, String region, String days) {
		return SPORTS_API_URL + "/v4/sports/" + sport +
				"/scores/?apiKey=" + SPORTS_API_KEY +
				"&regions=" + region +
				"&daysFrom=" + days;
	}

	public static List<Event> getallEvents() throws SQLException {

		List<Event> events = new ArrayList<Event>();

		Map<String, Pair<String, String>> sports_name = getSports();
		
		
		int retries = 0;
		while (retries < MAX_RETRIES) {
			try {
				for (String sport : sports) {
					events.addAll(getEvents(build_odds_sports_api_URL(sport, "eu"),
							sports_name.get(sport), false));
					events.addAll(getEventsScores(build_scores_sports_api_URL(sport, "eu",
							"3")));
				}
				break;
			} catch (Exception e) {
				e.printStackTrace();
				retries++;
			}
		}

		retries = 0;
		while (retries < MAX_RETRIES) {
			try {
				events.addAll(getEvents(GET_URL,sports_name.get("soccer_primeira_liga"), true));
				break;
			} catch (Exception e) {
				e.printStackTrace();
				retries++;
			}
		}
		
		return events;
	}

	private static Map<String, Pair<String, String>> getSports() throws SQLException {
		Map<String, Pair<String, String>> sports_map = new HashMap<String, Pair<String, String>>();
		sports_map.put("soccer_primeira_liga", Pair.of("Soccer", "Primeira Liga"));
		Map<String, List<String>> db_sports = new HashMap<String, List<String>>();
		db_sports.put("Soccer", Stream.of("Primeira Liga").collect(Collectors.toList()));

		int retries = 0;
		while (retries < MAX_RETRIES) {
			try {
				JSONArray jsonArray = readJsonFromUrl(SPORTS_API_URL + "/v4/sports/?apiKey=" + SPORTS_API_KEY);

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonSport = jsonArray.getJSONObject(i);
					String sport_key = jsonSport.getString("key");
					if (sports.contains(sport_key)) {
						String sport_group = jsonSport.getString("group");
						String sport_title = jsonSport.getString("title");
						sports_map.put(sport_key, Pair.of(sport_group, sport_title));
						if (db_sports.keySet().contains(sport_group)) {
							db_sports.get(sport_group).add(sport_title);
						} else {
							List<String> sport_list = new ArrayList<String>();
							sport_list.add(sport_title);
							db_sports.put(sport_group, sport_list);
						}
					}
				}
				break;
			} catch (Exception e) {
				e.printStackTrace();
				retries++;
			}
		}
		

		SportsDB.addSports(db_sports);
		return sports_map;
	}

	private static ArrayList<Event> getEvents(String url, Pair<String, String> pair, boolean custom_api)
			throws Exception {
		try {
			ArrayList<Event> events = new ArrayList<>();

			// Get info from API
			JSONArray jsonArray = readJsonFromUrl(url);

			// Go through all events
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonEvent = jsonArray.getJSONObject(i);
				JSONArray bookmakers = jsonEvent.getJSONArray("bookmakers");

				// Get newest odds for a event
				LocalDateTime lastUpdate = null;
				JSONArray markets = null;
				for (int j = 0; j < bookmakers.length(); j++) {
					String lastUpdateString = custom_api ? "lastUpdate" : "last_update";
					String string = bookmakers.getJSONObject(j).getString(lastUpdateString);
					LocalDateTime new_lastUpdate = LocalDateTime.parse(string.substring(0, string.length() - 1));
					if (lastUpdate == null || new_lastUpdate.isAfter(lastUpdate)) {
						lastUpdate = new_lastUpdate;
						markets = bookmakers.getJSONObject(j).getJSONArray("markets");
					}
				}

				// Get all the odds
				if (markets != null){
					
					JSONArray odds_json = markets.getJSONObject(0).getJSONArray("outcomes");
					Map<String, Odd> odds = new HashMap<>();
						
					for (int j = 0; j < odds_json.length(); j++) {
						JSONObject odd_json = odds_json.getJSONObject(j);
						odds.put(odd_json.getString("name"),
								new Odd(odd_json.getString("name"), odd_json.getDouble("price"), false));
					}

					// Create the event
					DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
					String homeString = custom_api ? "homeTeam" : "home_team";
					String awayString = custom_api ? "awayTeam" : "away_team";
					String comanceString = custom_api ? "commenceTime" : "commence_time";
					String result = custom_api && jsonEvent.getString("completed") == "true"
							&& !jsonEvent.getString("scores").equals("null") ? jsonEvent.getString("scores") : null;
					String description = !jsonEvent.get(homeString).equals(null)
							? jsonEvent.get(homeString) + " v " + jsonEvent.get(awayString)
							: jsonEvent.getString("sport_title");
					events.add(new Event(jsonEvent.getString("id"),
							pair.getFirst(),
							pair.getSecond(),
							LocalDateTime.parse(jsonEvent.getString(comanceString), formatter),
							description,
							result,
							null,
							odds));
				}
			}
			return events;
		} catch (JSONException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return null;
		}

	}

	private static ArrayList<Event> getEventsScores(String url) throws Exception {
		try {
			ArrayList<Event> events = new ArrayList<>();

			// Get info from API
			JSONArray jsonArray = readJsonFromUrl(url);

			// Go through all events
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonEvent = jsonArray.getJSONObject(i);
				boolean completed = jsonEvent.getBoolean("completed");
				String result = null, description = null;
				if (completed) {
					String homeTeam = jsonEvent.getString("home_team");
					String awayTeam = jsonEvent.getString("away_team");
					if (homeTeam.equals("null") && awayTeam.equals("null")) {
						result = jsonEvent.getString("scores");
						description = jsonEvent.getString("sport_title");
					} else {
						JSONArray scores_array = jsonEvent.getJSONArray("scores");
						String homeScore = null, awayScore = null;
						for (int j = 0; j < scores_array.length(); j++) {
							JSONObject scores_obj = scores_array.getJSONObject(j);
							if (scores_obj.getString("name").equals(homeTeam)) {
								homeScore = scores_obj.getString("score");
							} else {
								awayScore = scores_obj.getString("score");
							}
						}
						result = homeScore + "x" + awayScore;
						description = homeTeam + " v " + awayTeam;
					}
					events.add(new Event(
							jsonEvent.getString("id"),
							description,
							result));
				}
			}

			return events;
		} catch (JSONException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return null;
		}
	}
}

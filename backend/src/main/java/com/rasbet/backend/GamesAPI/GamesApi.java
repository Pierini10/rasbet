package com.rasbet.backend.GamesAPI;

import com.google.gson.Gson;
import com.rasbet.backend.Entities.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;


public class GamesApi {

	private static final String GET_URL = "http://ucras.di.uminho.pt/v1/games";

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
		  sb.append((char) cp);
		}
		return sb.toString();
	  }
	
	  public static JSONObject[] readJsonFromUrl() throws IOException, JSONException {
		InputStream is = new URL(GET_URL).openStream();
		try {
		  BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		  String jsonText = readAll(rd);
		  Gson gson = new Gson(); 
		  JSONObject[] jsonArray = gson.fromJson(jsonText, JSONObject[].class);
		  return jsonArray;
		} finally {
		  is.close();
		}
	  }

	public static ArrayList<Event> getEvents() throws IOException {
		try {
			JSONObject[] json = readJsonFromUrl();
			System.out.println(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}

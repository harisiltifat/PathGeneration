package de.tum.in.aics.thesis.project.fourSquare;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.tum.in.aics.thesis.project.models.Place;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class FourSquarePlaces {

	private static final String PLACES_API_BASE = "https://api.foursquare.com/v2/venues";
	private static final String TYPE_SEARCH = "/search";
	private static final String TYPE_EXPLORE = "/explore";
	private static final String CLIENT_ID = "CMTIF2RMFGA154DH5XKQH5YG35CCD2ZICHECHNSL5SJDT4DY";
	private static final String CLIENT_SECRET = "H2UEG50ODD0TLQLMDNKARYFGLXSA4I4B3ADG20C2RDLLHMYW&v=20130815";
	private static final float  NO_RATING = 0F;
	private static final Integer ZERO_LIKES = 0;
	
	public FourSquarePlaces(){}

	public ArrayList<Place> search(double lat, double lng, int radius,String categoriesID, int limit) {

		ArrayList<Place> resultList = null;
		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();
		try {
			StringBuilder sb = new StringBuilder(PLACES_API_BASE);
			sb.append(TYPE_SEARCH);
			sb.append("?client_id=" + CLIENT_ID);
			sb.append("&client_secret=" + CLIENT_SECRET);
			sb.append("&radius=" + String.valueOf(radius));
			sb.append("&ll=" + String.valueOf(lat) + "," + String.valueOf(lng));
			sb.append("&global=true");
			sb.append("&categoryId=" + categoriesID);
			sb.append("&limit=" + limit);

			URL url = new URL(sb.toString());
			conn = (HttpURLConnection) url.openConnection();
			InputStreamReader in = new InputStreamReader(conn.getInputStream());

			int read;
			char[] buff = new char[1024];
			while ((read = in.read(buff)) != -1) {
				jsonResults.append(buff, 0, read);
			}
		} catch (MalformedURLException e) {
			return resultList;
		} catch (IOException e) {
			return resultList;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		try {
			// Create a JSON object hierarchy from the results
			JSONObject jsonObj = new JSONObject(jsonResults.toString());

			JSONObject ResponseJsonObj = new JSONObject(jsonObj.get("response").toString());
			JSONArray predsJsonArray = ResponseJsonObj.getJSONArray("venues");

			// Extract the Place descriptions from the results
			resultList = new ArrayList<Place>(predsJsonArray.length());
			int ratingCounter=0;
			for (int i = 0; i < predsJsonArray.length(); i++) {
				ratingCounter++;
				Place place = new Place();

				place.setName(predsJsonArray.getJSONObject(i).getString("name"));

				place.setGeometry(predsJsonArray.getJSONObject(i).getString("location"));

				JSONArray catJsonArray = predsJsonArray.getJSONObject(i).getJSONArray("categories");
				StringBuilder categories = new StringBuilder();
				for (int j = 0; j < catJsonArray.length(); j++) {
					categories.append(catJsonArray.getJSONObject(j).getString("name") + ",");
				}
				place.setTypes(categories.substring(0, categories.length() - 1));

				JSONObject stats = predsJsonArray.getJSONObject(i).getJSONObject("stats");
				place.setStats((Integer) stats.get("checkinsCount"));

				 if (predsJsonArray.getJSONObject(i).has("rating")){
					 place.setRating(Float.parseFloat(predsJsonArray.getJSONObject(i).getString("rating"))); 
				 }
				place.setRating(ratingCounter);
				resultList.add(place);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return resultList;
	}

	public ArrayList<Place> explore(double lat, double lng, int radius, int limit) {

		ArrayList<Place> resultList = null;
		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();
		try {
			StringBuilder sb = new StringBuilder(PLACES_API_BASE);
			sb.append(TYPE_EXPLORE);
			sb.append("?client_id=" + CLIENT_ID);
			sb.append("&client_secret=" + CLIENT_SECRET);
			sb.append("&radius=" + String.valueOf(radius));
			sb.append("&ll=" + String.valueOf(lat) + "," + String.valueOf(lng));
			sb.append("&global=true");
			sb.append("&limit=" + limit);

			URL url = new URL(sb.toString());
			conn = (HttpURLConnection) url.openConnection();
			InputStreamReader in = new InputStreamReader(conn.getInputStream());

			int read;
			char[] buff = new char[1024];
			while ((read = in.read(buff)) != -1) {
				jsonResults.append(buff, 0, read);
			}
		} catch (MalformedURLException e) {
			return resultList;
		} catch (IOException e) {
			return resultList;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		try {
			
			JSONObject jsonObj = new JSONObject(jsonResults.toString());
			JSONObject responseJsonObj = new JSONObject(jsonObj.get("response").toString());
			JSONArray groupJsonArray = responseJsonObj.getJSONArray("groups");
			JSONArray itemJsonArray = groupJsonArray.getJSONObject(0).getJSONArray("items");

			resultList = new ArrayList<Place>(itemJsonArray.length());
			
			for (int i = 0; i < itemJsonArray.length(); i++) {
				
				Place place = new Place();		
				JSONObject venueJsonObj = itemJsonArray.getJSONObject(i).getJSONObject("venue");
				
				place.setName(venueJsonObj.getString("name"));

				place.setGeometry(venueJsonObj.getJSONObject("location").toString());
				
				JSONArray catgoryJsonArray = venueJsonObj.getJSONArray("categories");
				StringBuilder categories = new StringBuilder();
				for (int j = 0; j < catgoryJsonArray.length(); j++) {
					categories.append(catgoryJsonArray.getJSONObject(j).getString("name") + ",");
				}
				place.setTypes(categories.substring(0, categories.length() - 1));
				
				place.setStats(venueJsonObj.getJSONObject("stats").getInt("checkinsCount"));
				
				if(venueJsonObj.has("likes"))
					place.setLikes(venueJsonObj.getJSONObject("likes").getInt("count"));
				else 
					place.setLikes(ZERO_LIKES);
				
				if(venueJsonObj.has("hours"))
					place.setOpenNow(venueJsonObj.getJSONObject("hours").getBoolean("isOpen"));
				
				if(venueJsonObj.has("rating"))
					place.setRating(Float.parseFloat(venueJsonObj.getString("rating")));	
				else 
					place.setRating(NO_RATING);
				
				resultList.add(place);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return resultList;
	}
}
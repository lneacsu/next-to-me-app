package com.neal.android.upitplatforma;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * Created by Loredana on 13.05.2019.
 */
public class Parse {

    public String getNextPage(String jsonData) {
        String jsonNextPage = null;
        JSONObject jsonObject;
        try {
            Log.d("Places", "parse");
            jsonObject = new JSONObject((String) jsonData);
            jsonNextPage = jsonObject.getString("next_page_token");

        } catch (JSONException e) {
            Log.d("Places", "parse error");
            e.printStackTrace();
        }
        return jsonNextPage;
    }

    public List<HashMap<String, String>> parse(String jsonData) {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {
            Log.d("Places", "parse");
            jsonObject = new JSONObject((String) jsonData);
            jsonArray = jsonObject.getJSONArray("results");

            Log.d("jsonListadeineput", jsonArray.toString());
        } catch (JSONException e) {
            Log.d("Places", "parse error");
            e.printStackTrace();
        }
        return getPlaces(jsonArray);
    }

    private List<HashMap<String, String>> getPlaces(JSONArray jsonArray) {
        int placesCount = jsonArray.length();
        List<HashMap<String, String>> placesList = new ArrayList<>();
        HashMap<String, String> placeMap = null;
        Log.d("Places", "getPlaces");

        for (int i = 0; i < placesCount; i++) {
            try {
                placeMap = getPlace((JSONObject) jsonArray.get(i));
                placesList.add(placeMap);
                Log.d("Places", "Adding places");

            } catch (JSONException e) {
                Log.d("Places", "Error in Adding places");
                e.printStackTrace();
            }
        }
        return placesList;
    }

    private HashMap<String, String> getPlace(JSONObject googlePlaceJson) {
        HashMap<String, String> googlePlaceMap = new HashMap<String, String>();
        String placeName = "-NA-";
        String vicinity = "-NA-";
        String formatted_address = "-NA-";
        String latitude = "";
        String longitude = "";
        String reference = "";
        Double rating = null;
        String international_phone_number = "";
        String website = "";
        Log.d("jsonImportntFile", googlePlaceJson.toString());

        try {
            if (!googlePlaceJson.isNull("name")) {
                placeName = googlePlaceJson.getString("name");
            }
            if (!googlePlaceJson.isNull("vicinity")) {
                vicinity = googlePlaceJson.getString("vicinity");
            }
            if (!googlePlaceJson.isNull("formatted_address")) {
                formatted_address = googlePlaceJson.getString("formatted_address");
            }
            if (!googlePlaceJson.isNull("website")) {
                website = googlePlaceJson.getString("website");
            }
            if (!googlePlaceJson.isNull("international_phone_number")) {
                international_phone_number = googlePlaceJson.getString("international_phone_number");
            }
            if (!googlePlaceJson.isNull("rating")) {
                rating = googlePlaceJson.getDouble("rating");
            }
            latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
            reference = googlePlaceJson.getString("reference");

            googlePlaceMap.put("place_name", placeName);
            googlePlaceMap.put("vicinity", vicinity);
            googlePlaceMap.put("lat", latitude);
            googlePlaceMap.put("lng", longitude);
            googlePlaceMap.put("reference", reference);
            if (rating != null) {
                googlePlaceMap.put("rating", rating.toString());
            }
            googlePlaceMap.put("international_phone_number", international_phone_number);
            googlePlaceMap.put("website", website);
            googlePlaceMap.put("formatted_address", formatted_address);


            Log.d("getPlaceListJson", googlePlaceMap.toString());
        } catch (JSONException e) {
            Log.d("getPlace", "Error");
            e.printStackTrace();
        }
        return googlePlaceMap;
    }
}

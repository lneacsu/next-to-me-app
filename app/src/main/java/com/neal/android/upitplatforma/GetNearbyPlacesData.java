package com.neal.android.upitplatforma;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.neal.android.upitplatforma.part2.ProcessFinish;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * Created by Loredana on 11.11.2018.
 */
public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

    String googlePlacesData;
    GoogleMap mMap;
    String url;
    public static List<Place> v;
    private MapsActivity activity;
    private ProcessFinish mCallback;
    private Context mContext;


    public GetNearbyPlacesData(Context context) {
        this.mContext = context;
        this.mCallback = (ProcessFinish) context;

    }

    @Override
    protected String doInBackground(Object... params) {
        try {
            Log.d("GetNearbyPlacesData", "doInBackground entered");
            mMap = (GoogleMap) params[0];
            url = (String) params[1];
            DownURL downloadUrl = new DownURL();
            googlePlacesData = downloadUrl.readUrl(url);
            Log.d("GooglePlacesReadTask", "doInBackground Exit");
        } catch (Exception e) {
            Log.d("GooglePlacesReadTask", e.toString());
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("GooglePlacesReadTask", "onPostExecute Entered");
        List<HashMap<String, String>> nearbyPlacesList = null;
        Parse dataParser = new Parse();
        nearbyPlacesList = dataParser.parse(result);
        ShowNearbyPlaces(nearbyPlacesList);
        Log.d("GooglePlacesReadTask", "onPostExecute Exit");


    }

    private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {
        v = new ArrayList<>();

        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            if (vicinity.compareTo("-NA-") == 0) {
                vicinity = googlePlace.get("formatted_address");
            }
            String place_id = googlePlace.get("reference");
            String phone = googlePlace.get("international_phone_number");
            String rating = null;
            if (googlePlace.get("rating") != null) {
                rating = googlePlace.get("rating");
            }
            String url = googlePlace.get("website");
            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName + " : " + vicinity);
            mMap.addMarker(markerOptions);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
            Place p;
            if (rating != null) {
                p = new Place(placeName, lat, lng, vicinity, place_id, phone, rating, url);
            } else p = new Place(placeName, lat, lng, vicinity, place_id, phone, null, url);
            v.add(p);
            p.toString();

        }
        mCallback.processFinish(v);
    }
}

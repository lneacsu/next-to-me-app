package com.neal.android.upitplatforma;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.neal.android.upitplatforma.part2.ProcessFinish;

import java.util.List;
/**
 * Created by Loredana on 13.11.2018.
 */
public class GetNextPageKey extends AsyncTask<Object, String, String> {
    String googlePlacesData;
    GoogleMap mMap;
    String url;
    public static List<Place> v;
    private MapsActivity activity;
    private ProcessFinish mCallback;
    private Context mContext;

    public GetNextPageKey(Context context) {
        this.mContext = context;
        this.mCallback = (ProcessFinish) context;

    }

    @Override
    protected String doInBackground(Object... params) {
        try {
            mMap = (GoogleMap) params[0];
            url = (String) params[1];
            DownURL downloadUrl = new DownURL();
            googlePlacesData = downloadUrl.readUrl(url);
        } catch (Exception e) {
            Log.d("GooglePlacesReadTask", e.toString());
        }
        return googlePlacesData;

    }

    @Override
    protected void onPostExecute(String result) {
        String nearbyPlacesList = null;
        Parse dataParser = new Parse();
        nearbyPlacesList = dataParser.getNextPage(result);

        mCallback.processFinishNextPage(nearbyPlacesList);

    }
}

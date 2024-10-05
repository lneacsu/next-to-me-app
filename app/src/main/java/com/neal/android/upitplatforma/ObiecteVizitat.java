package com.neal.android.upitplatforma;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.neal.android.upitplatforma.part2.ProcessFinish;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
/**
 * Created by Loredana on 13.05.2019.
 */
public class ObiecteVizitat extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        ProcessFinish,
        RecyclerViewClickListener {
    SupportMapFragment mapFrag;
    GoogleMap mGoogleMap;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    int PROXIMITY_RADIUS = 2000;
    GeoDataClient mGeoDataClient;
    Double longitudine;
    Double latitudine;
    String locatie, city;
    List<com.neal.android.upitplatforma.Place> listaObiective = new ArrayList<com.neal.android.upitplatforma.Place>();
    private RecyclerView recyclerView;
    private PlacesAdapter mAdapter;
    private String nextPage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.obiect_de_vizitat);
        Intent intent = getIntent();
        latitudine = intent.getExtras().getDouble("latitudine");
        longitudine = intent.getExtras().getDouble("longitudine");
        locatie = intent.getExtras().getString("string");
        city = intent.getExtras().getString("city");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        CheckBox vizitat = (CheckBox) findViewById(R.id.vizitat);
        mGeoDataClient = Places.getGeoDataClient(this, null);
        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

    }

    private void getNextPage() {
        // mGoogleMap.clear();
        String url = getUrlNextPage(latitudine, longitudine, locatie);
        Object[] DataTransfer = new Object[2];
        DataTransfer[0] = mGoogleMap;
        DataTransfer[1] = url;
        Log.d("onClick", url);
        GetNextPageKey netNextPageKey = new GetNextPageKey(ObiecteVizitat.this);
        try {
            netNextPageKey.execute(DataTransfer);
        } catch (Exception e) {
            Log.d("Error", e.toString());
        }
    }

    private void getPlaces() {
        mGoogleMap.clear();
        String url = getUrl(latitudine, longitudine, locatie, city);
        Object[] DataTransfer = new Object[2];
        DataTransfer[0] = mGoogleMap;
        DataTransfer[1] = url;
        Log.d("onClick", url);
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData(ObiecteVizitat.this);
        try {
            getNearbyPlacesData.execute(DataTransfer);
        } catch (Exception e) {
            Log.d("Error", e.toString());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }

    }

    private String getUrl(double latitude, double longitude, String nearbyPlace, String city) {
        StringBuilder googlePlacesUrl = null;
        if (nearbyPlace.compareTo("monumente") == 0) {
            googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json?");
            googlePlacesUrl.append("query=" + city.toLowerCase() + "+city+tourist+interest");
            googlePlacesUrl.append("&language=ro");
            googlePlacesUrl.append("&key=" + "add_your_key");
            Log.d("getUrl", googlePlacesUrl.toString());
        } else {
            googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            googlePlacesUrl.append("location=" + latitude + "," + longitude);
            googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
            googlePlacesUrl.append("&type=" + nearbyPlace);
            googlePlacesUrl.append("&sensor=false");
            googlePlacesUrl.append("&key=" + "AIzaSyCYj2lBZq4Ekr9kKu5eXrg1HKeCq4pYse0");

            Log.d("getUrl", googlePlacesUrl.toString());
        }

        return (googlePlacesUrl.toString());
    }

    private String getUrlNextPage(double latitude, double longitude, String nearbyPlace) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&hasNextPage=true&nextPage()=true");
        googlePlacesUrl.append("&sensor=false");
        googlePlacesUrl.append("&key=" + "AIzaSyCYj2lBZq4Ekr9kKu5eXrg1HKeCq4pYse0");


        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();

        mGoogleApiClient.connect();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(latitudine, longitudine);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

        //move map camera
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));


        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = new ArrayList<Address>();
        try {
            addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.size() > 0) {
        } else {
            // do your stuff

        }

        getPlaces();
    }

    @Override
    public void processFinishNextPage(String output) {
        Log.d("IMPORTANTString", " " + output);
        nextPage = output;
    }

    @Override
    public void processFinish(List<com.neal.android.upitplatforma.Place> output) {
        Log.d("IMPORTANT", " " + output.size());
        for (com.neal.android.upitplatforma.Place s : output) {
            Log.d("adress", " " + s.toString());
            double newLat = s.getmLatitudine();
            double newLong = s.getmLongitudine();
            s.setDistance(distance(latitudine, longitudine, newLat, newLong));
            this.listaObiective.add(s);
        }
        mAdapter = new PlacesAdapter(listaObiective, getApplicationContext(), locatie);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }


    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {

    }
}

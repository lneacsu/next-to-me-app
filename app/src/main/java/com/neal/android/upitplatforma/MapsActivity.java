package com.neal.android.upitplatforma;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
 * Created by Loredana on 10.09.2018.
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        ProcessFinish {

    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    TextView txtView;
    Button spital;
    Button monumente;
    Button mall;
    Button restaurant;
    Button parc;
    Locale myLocale;
    Drawable drawable;
    double latitudine;
    double longitudine;
    String city;
    GeoDataClient mGeoDataClient;
    int PROXIMITY_RADIUS = 3000;
    List<com.neal.android.upitplatforma.Place> listaObiective = new ArrayList<com.neal.android.upitplatforma.Place>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mGeoDataClient = Places.getGeoDataClient(this, null);
        spital = (Button) findViewById(R.id.spital);
        monumente = (Button) findViewById(R.id.monumente);
        mall = (Button) findViewById(R.id.mall);
        restaurant = (Button) findViewById(R.id.restaurant);
        parc = (Button) findViewById(R.id.parc);


        spital.setOnClickListener(new View.OnClickListener() {
            String spital = "hospital";

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ObiecteVizitat.class);
                i.putExtra("latitudine", latitudine);
                i.putExtra("longitudine", longitudine);
                i.putExtra("string", spital);
                startActivity(i);
            }
        });
        mall.setOnClickListener(new View.OnClickListener() {
            String mall = "shopping_mall";

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ObiecteVizitat.class);
                i.putExtra("latitudine", latitudine);
                i.putExtra("longitudine", longitudine);
                i.putExtra("string", mall);
                startActivity(i);
            }
        });

        restaurant.setOnClickListener(new View.OnClickListener() {
            String restaurant = "restaurant";

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ObiecteVizitat.class);
                i.putExtra("latitudine", latitudine);
                i.putExtra("longitudine", longitudine);
                i.putExtra("string", restaurant);
                startActivity(i);
            }
        });
        parc.setOnClickListener(new View.OnClickListener() {
            String parc = "park";

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ObiecteVizitat.class);
                i.putExtra("latitudine", latitudine);
                i.putExtra("longitudine", longitudine);
                i.putExtra("string", parc);
                startActivity(i);
            }
        });

        monumente.setOnClickListener(new View.OnClickListener() {
            String monumente = "monumente"; //,museum,churc,stadium,zoo,point_of_interest,establishment

            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ObiecteVizitat.class);
                i.putExtra("latitudine", latitudine);
                i.putExtra("longitudine", longitudine);
                i.putExtra("string", monumente);
                i.putExtra("city", city);
                startActivity(i);
            }
        });


        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

    }

    private String getUrl(double latitude, double longitude, String nearbyPlace) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&hasNextPage=true&nextPage()=true");

        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyCYj2lBZq4Ekr9kKu5eXrg1HKeCq4pYse0");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }


    @Override
    public final boolean onCreateOptionsMenu(final Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.romana:
                Toast.makeText(this.getApplicationContext(),
                        "You have selected romana", Toast.LENGTH_SHORT)
                        .show();
                setLocale("ro");
                return true;
            case R.id.engleza:
                Toast.makeText(this.getApplicationContext(),
                        "You have selected engleza", Toast.LENGTH_SHORT)
                        .show();

                setLocale("en");
                return true;
            case R.id.franceza:
                Toast.makeText(this.getApplicationContext(),
                        "You have selected franceza", Toast.LENGTH_SHORT)
                        .show();
                setLocale("fr");
                return true;
            case R.id.spaniola:
                Toast.makeText(this.getApplicationContext(),
                        "You have selected spaniola", Toast.LENGTH_SHORT)
                        .show();

                setLocale("sp");
                return true;
            case R.id.profil: {
                Intent i = new Intent(MapsActivity.this, Activity_register.class);
                startActivity(i);
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setLocale(String lang) {
        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, MapsActivity.class);
        startActivity(refresh);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }
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
    public void onConnected(Bundle bundle) {
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
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
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

            latitudine = location.getLatitude();
            longitudine = location.getLongitude();
            txtView = (TextView) findViewById(R.id.City);
            txtView.setText(" " + addresses.get(0).getLocality() + "!");
            city = addresses.get(0).getLocality();

        } else {
            txtView = (TextView) findViewById(R.id.City);
            txtView.setText("Nu s-a gasit ");
        }

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {


            if (android.support.v4.app.ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                                android.support.v4.app.ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {

                android.support.v4.app.ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public void processFinish(List<com.neal.android.upitplatforma.Place> output) {
        Log.d("IMPORTANT", " " + output.size());
        for (com.neal.android.upitplatforma.Place s : output) {
            Log.d("adress", " " + s.toString());
            this.listaObiective.add(s);
        }
    }

    @Override
    public void processFinishNextPage(String output) {

    }
}

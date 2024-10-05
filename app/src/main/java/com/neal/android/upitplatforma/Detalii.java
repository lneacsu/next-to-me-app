package com.neal.android.upitplatforma;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Loredana on 22.03.2019.
 */
public class Detalii extends AppCompatActivity {
    public static final String TAG = "Detalii";
    private static final int PLACE_PICKER_REQ_CODE = 2;
    private ImageView placeImage, save, share;
    private List<PlacePhotoMetadata> photosDataList;
    private int currentPhotoIndex = 0;
    private GeoDataClient geoDataClient;
    private String placeId, loc, tel, stars, website, adress, distance, lat, longit, type;
    private Button nextPhoto;
    private Button prevPhoto;
    private TextView locatie, nota, url, phone, placeAdress, departare, tipLoc, detaliiDepartare;
    GoogleApiClient googleApiClient;
    private static final int GOOGLE_API_CLIENT_ID = 0;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalii);
        Intent intent = getIntent();
        placeId = intent.getExtras().getString("place_id");
        loc = intent.getExtras().getString("locatie");
        tel = intent.getExtras().getString("tel");
        stars = intent.getExtras().getString("nota");
        website = intent.getExtras().getString("website");
        adress = intent.getExtras().getString("adresa");
        distance = intent.getExtras().getString("distanta");
        lat = intent.getExtras().getString("lat");
        longit = intent.getExtras().getString("lon");
        type = intent.getExtras().getString("tipLoc");
        Log.d("somethinghere>", " " + lat + " " + longit);
        placeImage = findViewById(R.id.imagineLocatie);
        final RatingBar ratingBar = findViewById(R.id.rating);
        geoDataClient = Places.getGeoDataClient(this, null);
        getPhotoMetadata(placeId);


        locatie = findViewById(R.id.placeName);
        locatie.setText(loc);

        tipLoc = findViewById(R.id.typeLoc);
        //tipLoc.setText(type);
        switch (type) {
            case "monumente": {
                tipLoc.setText(R.string.monumente);
                break;
            }
            case "park": {
                tipLoc.setText(R.string.parc);
                break;
            }
            case "restaurant": {
                tipLoc.setText(R.string.restaurant);
                break;
            }
            case "shopping_mall": {
                tipLoc.setText(R.string.mall);
                break;
            }
            case "hospital": {
                locatie.setText(R.string.spital);
                break;
            }
        }
        detaliiDepartare = findViewById(R.id.dep);
        detaliiDepartare.setText(distance);
        nota = findViewById(R.id.nota);
        if (stars.equals("")) {
            nota.setTextSize(25);
            nota.setText("-N/A-");
            ratingBar.setRating(0);
        } else if (stars != null) {
            nota.setText(stars);
            ratingBar.setRating(Float.parseFloat(stars));
        }
        placeAdress = findViewById(R.id.placeAddress);
        placeAdress.setText(adress);

        placeAdress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri gmmIntentUri = Uri.parse("google.streetview:cbll=" + lat + "," + longit);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);


            }
        });
        departare = findViewById(R.id.distance);
        departare.setText(distance + " (See more)");
        departare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + adress);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
        share = (ImageView) findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String uri = "http://maps.google.com/maps?daddr=" + lat + "," + longit;
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String ShareSub = "Am vizitat recent " + locatie.getText() + ". Utilizeaza si tu acum aplicatia Next to me! \n" + uri;
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, ShareSub);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));

            }
        });
        save = (ImageView) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                Intent intent = new Intent(v.getContext(), Activity_register.class);
                                intent.putExtra("nume", locatie.getText().toString());
                                intent.putExtra("adresa", placeAdress.getText().toString());
                                v.getContext().startActivity(intent);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;

                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Bun Venit!");
                builder.setMessage("Adaugati locatie in jurnal de calatorie?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

            }
        });
    }

    private void getPhotoMetadata(String placeId) {

        final Task<PlacePhotoMetadataResponse> photoResponse =
                geoDataClient.getPlacePhotos(placeId);

        photoResponse.addOnCompleteListener
                (new OnCompleteListener<PlacePhotoMetadataResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                        currentPhotoIndex = 0;
                        photosDataList = new ArrayList<>();
                        PlacePhotoMetadataResponse photos = task.getResult();
                        PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();

                        Log.d(TAG, "number of photos " + photoMetadataBuffer.getCount());

                        for (PlacePhotoMetadata photoMetadata : photoMetadataBuffer) {
                            photosDataList.add(photoMetadataBuffer.get(0).freeze());
                        }

                        photoMetadataBuffer.release();

                        displayPhoto();
                    }
                });

    }

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e("test", "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();
            if (attributions != null) {
                Log.d("ce e asta?", " " + place.getPhoneNumber() + " " + place.getWebsiteUri());
            }
        }
    };

    private void getPhoto(PlacePhotoMetadata photoMetadata) {
        Task<PlacePhotoResponse> photoResponse = geoDataClient.getPhoto(photoMetadata);
        photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
                PlacePhotoResponse photo = task.getResult();
                Bitmap photoBitmap = photo.getBitmap();
                Log.d(TAG, "photo " + photo.toString());

                placeImage.invalidate();
                placeImage.setImageBitmap(photoBitmap);
            }
        });
    }

    private void displayPhoto() {
        Log.d(TAG, "index " + currentPhotoIndex);
        Log.d(TAG, "photosDataListsize " + photosDataList.size());
        if (photosDataList.isEmpty() || currentPhotoIndex > photosDataList.size() - 1) {
            return;
        } else {
            getPhoto(photosDataList.get(currentPhotoIndex));
            Log.d(TAG, "impPhoto " + photosDataList.get(currentPhotoIndex));
        }
    }

}
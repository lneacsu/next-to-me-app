package com.neal.android.upitplatforma;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Loredana on 24.05.2019.
 */
public class Activity_register extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public static final String ANONYMOUS = "anonymous";
    public static final int RC_SIGN_IN = 1;
    private ProgressBar mProgressBar;
    private TextView username;
    private String mUsername;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public static FirebaseDatabase Mfirebase;
    private ImageView imgp;
    String nume;
    RatingBar ratingBar;
    String adresa, date;
    int pozitieItem;
    private RecyclerView mRecyclerView;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    FirebaseStorage storage;
    StorageReference storageReference;
    Uri getDownloadUrl;
    Button inLineView, ingridView, expunereDiapozitive, logOut;
    String user_uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_fragment);
        if (Mfirebase != null) {
            Mfirebase = FirebaseDatabase.getInstance();
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        mUsername = ANONYMOUS;
        username = (TextView) findViewById(R.id.username);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.showContextMenu();

        initializeFirebase();
        final ProgressDialog mProgress = new ProgressDialog(this);
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    user_uid = user.getUid();
                    mProgress.setMessage("Preluare date");
                    mProgress.show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mMessagesDatabaseReference = mFirebaseDatabase.getReference(user_uid);
                            inLineView.performClick();
                            mProgress.dismiss();
                        }
                    }, 1000);
                    onSignedInInitialize(user.getDisplayName());
                    if (username.getText() == "") {
                        username.append(user.getDisplayName());
                    }

                } else {
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(
                                            Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build())).build(), RC_SIGN_IN);
                }

            }
        };
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);

        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        inLineView = (Button) findViewById(R.id.lineView);
        inLineView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView = (RecyclerView) findViewById(R.id.recycler_jurnal);
                new Test(mFirebaseDatabase, mMessagesDatabaseReference).readVisit(new Test.DataStatus() {
                    @Override
                    public void loadData(List<Postare> post, List<String> keys) {
                        new RecyclerView_Config().setConfig(mRecyclerView, Activity_register.this, post, keys);
                    }
                });
            }
        });

        ingridView = (Button) findViewById(R.id.frameView);
        ingridView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView = (RecyclerView) findViewById(R.id.recycler_jurnal);
                new Test(mFirebaseDatabase, mMessagesDatabaseReference).readVisit(new Test.DataStatus() {
                    @Override
                    public void loadData(List<Postare> post, List<String> keys) {
                        new RecyclerGridView().setConfig(mRecyclerView, Activity_register.this, post, keys);
                    }
                });
            }
        });

        expunereDiapozitive = (Button) findViewById(R.id.expunere_diap);
        expunereDiapozitive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView = (RecyclerView) findViewById(R.id.recycler_jurnal);
                new Test(mFirebaseDatabase, mMessagesDatabaseReference).readVisit(new Test.DataStatus() {
                    @Override
                    public void loadData(List<Postare> post, List<String> keys) {
                        new RecyclerGetImagesView().setConfig(mRecyclerView, Activity_register.this, post, keys);
                    }
                });
            }

        });
        logOut = (Button) findViewById(R.id.logout);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance()
                        .signOut(Activity_register.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {

                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent i = new Intent(Activity_register.this, MapsActivity.class);
                                startActivity(i);
                            }
                        });
            }
        });


        if (getIntent().getExtras() != null) {
            nume = getIntent().getExtras().getString("nume");
            adresa = getIntent().getExtras().getString("adresa");
            pozitieItem = getIntent().getExtras().getInt("pozitieItem");
        }

        if (nume != null) {
            LayoutInflater inflater = getLayoutInflater();
            View alertLayout = inflater.inflate(R.layout.add_to_diary, null);
            TextView text = (TextView) alertLayout.findViewById(R.id.locatie);
            imgp = (ImageView) alertLayout.findViewById(R.id.post_image);
            text.setText(nume);
            ratingBar = (RatingBar) alertLayout.findViewById(R.id.rating);
            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                public void onRatingChanged(RatingBar ratingBar, float rating,
                                            boolean fromUser) {

                    Toast.makeText(Activity_register.this, "Nota: " + String.valueOf(rating),
                            Toast.LENGTH_LONG).show();

                }
            });
            imgp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chooseImage();
                }
            });
            final EditText descriere = (EditText) alertLayout.findViewById(R.id.descriere);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Ati vizitat recent:");
            builder.setView(alertLayout);
            builder.setCancelable(false);
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //   uploadImage();

                    if (filePath != null) {
                        final ProgressDialog progressDialog = new ProgressDialog(Activity_register.this);
                        progressDialog.setTitle("Uploading...");
                        progressDialog.show();

                        final StorageReference ref = storageReference.child(mFirebaseAuth.getCurrentUser().getUid() + "/" + filePath.getLastPathSegment());
                        ref.putFile(filePath).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }
                                int nr = 0;
                                progressDialog.setMessage("Uploaded " + (int) nr++ + "%");
                                return ref.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Uri uri = task.getResult();
                                    Date crdate = new Date();
                                    date = new SimpleDateFormat("yyyy-MM-dd").format(crdate);
                                    Postare post = null;
                                    String rating = Float.toString(ratingBar.getRating());
                                    if (uri != Uri.EMPTY) {
                                        post = new Postare(nume, pozitieItem, descriere.getText().toString(), adresa, date, uri.toString(), rating);
                                    } else {
                                        post = new Postare(nume, pozitieItem, descriere.getText().toString(), adresa, date, null, rating);
                                    }
                                    mMessagesDatabaseReference.push().setValue(post);
                                    Log.d(TAG, "onComplete: Url: " + uri.toString());
                                    progressDialog.dismiss();
                                    Toast.makeText(Activity_register.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_jurnal);
        new Test(mFirebaseDatabase, mMessagesDatabaseReference).readVisit(new Test.DataStatus() {
            @Override
            public void loadData(List<Postare> post, List<String> keys) {
                new RecyclerView_Config().setConfig(mRecyclerView, Activity_register.this, post, keys);
            }
        });


    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            final ProgressDialog mProgress = new ProgressDialog(this);
            if (username.getText() == "") {
                username.append(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            }
            mProgress.setMessage("Preluare date");
            mProgress.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mMessagesDatabaseReference = mFirebaseDatabase.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    inLineView.performClick();
                    mProgress.dismiss();
                }
            }, 1000);
        }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            Uri abc = data.getData();
            CropImage.activity(abc)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                filePath = result.getUri();
                imgp.setImageURI(filePath);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void uploadImage() {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child(mFirebaseAuth.getCurrentUser().getUid() + "/" + filePath.getLastPathSegment());
            ref.putFile(filePath).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        getDownloadUrl = task.getResult();
                        Log.d(TAG, "onComplete: Url: " + getDownloadUrl.toString());
                    }
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        detachDatabaseReadListener();
    }


    private void onSignedInInitialize(String username) {
        mUsername = username;
    }

    private void onSignedOutCleanup() {
        mUsername = ANONYMOUS;
        detachDatabaseReadListener();
    }


    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mMessagesDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    private void initializeFirebase() {

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference(mUsername);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }


}



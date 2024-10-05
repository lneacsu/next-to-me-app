package com.neal.android.upitplatforma;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
/**
 * Created by Loredana on 13.05.2019.
 */
public class RecyclerGridView {
    private Context context;
    private PostAdapter mPostAdapter;
    private Uri uri;

    public void setConfig(RecyclerView recyclerView, Context context, List<Postare> posts, List<String> keys) {
        this.context = context;
        mPostAdapter = new PostAdapter(posts, keys);
        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(context, numberOfColumns));
        recyclerView.setAdapter(mPostAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


    }

    class PostareItemView extends RecyclerView.ViewHolder {
        private ImageView img, dots;
        private String key;
        private String test;
        private Dialog builder;
        private LinearLayout linearLayout;
        private TextView data, loc;
        FirebaseStorage storage;

        public PostareItemView(ViewGroup parent) {
            super(LayoutInflater.from(context).inflate(R.layout.load_postare_grid, parent, false));
            img = (ImageView) itemView.findViewById(R.id.imagine);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.longPress);
            data = (TextView) itemView.findViewById(R.id.data);
            loc = (TextView) itemView.findViewById(R.id.locVizitat);
            dots = (ImageView) itemView.findViewById(R.id.overflow);

        }

        public void bind(Postare post, String key, final int position) {
            loc.setText(post.getLocatie());
            data.setText(post.getData());
            boolean isPhoto = post.getPhotoUrl() != null;
            test = post.getPhotoUrl();
            if (isPhoto) {
                img.setVisibility(View.VISIBLE);
                Glide.with(img.getContext())
                        .load(post.getPhotoUrl())
                        .into(img);
            } else {
                img.setVisibility(View.GONE);
            }


            img.setOnTouchListener(mTouchListener);
            dots.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getBackground(v, test, position);
                }

            });

            this.key = key;
        }

        private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                builder = new Dialog(v.getContext());
                builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                builder.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        //nothing;
                    }
                });
                int a = v.getId();
                if (R.id.imagine == a) {
                    uri = Uri.parse(test); //path of image
                    //test
                    Log.d("cevaaici?", uri.toString());
                }
                ImageView imageView = new ImageView(v.getContext());
                Glide.with(v.getContext())
                        .load(test)
                        .into(imageView);

                builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                builder.show();
                imageView.setOnTouchListener(mTouchOutside);
                return false;
            }
        };
        private View.OnTouchListener mTouchOutside = new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                builder.dismiss();

                return false;
            }
        };

        private void getBackground(final View v, final String test, final int position) {
            //DISPLAY DIALOG TO CHOOSE CAMERA OR GALLERY
            final CharSequence[] items = {"Delete this memory",
                    "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Settings");

            //SET ITEMS AND THERE LISTENERS
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (items[item].equals("Delete this memory")) {
                        delete(v, test, position);
                    } else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();

        }

        private void delete(View v, String test, int position) {
            FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
            storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            Query applesQuery = ref.child(mFirebaseAuth.getCurrentUser().getUid()).orderByChild("photoUrl").equalTo(test.toString());
            applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                        appleSnapshot.getRef().removeValue();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("error?", "" + databaseError.toException());
                }
            });
        }
    }

    class PostAdapter extends RecyclerView.Adapter<PostareItemView> {
        private List<Postare> mPosts;
        private List<String> keys;

        public PostAdapter(List<Postare> mPosts, List<String> keys) {
            this.mPosts = mPosts;
            this.keys = keys;
        }

        @Override
        public PostareItemView onCreateViewHolder(ViewGroup parent, int viewType) {
            return new PostareItemView(parent);
        }

        @Override
        public void onBindViewHolder(PostareItemView holder, int position) {
            holder.bind(mPosts.get(position), keys.get(position), position);
        }

        @Override
        public int getItemCount() {
            return mPosts.size();
        }
    }
}


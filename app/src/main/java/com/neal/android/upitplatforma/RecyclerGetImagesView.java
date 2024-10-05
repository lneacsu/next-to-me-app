package com.neal.android.upitplatforma;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
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

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Loredana on 10.06.2019.
 */
public class RecyclerGetImagesView {
    private Context context;
    private RecyclerGetImagesView.PostAdapter mPostAdapter;
    private Uri uri;
    public static List<String> listaImagini = new ArrayList<>();

    public void setConfig(RecyclerView recyclerView, Context context, List<Postare> posts, List<String> keys) {
        this.context = context;
        mPostAdapter = new RecyclerGetImagesView.PostAdapter(posts, keys);
        int numberOfColumns = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(context, numberOfColumns));
        recyclerView.setAdapter(mPostAdapter);


    }

    class PostareItemView extends RecyclerView.ViewHolder {
        private ImageView img;
        private String key;
        private String test;
        private Dialog builder1;
        private Dialog builder;
        private LinearLayout linearLayout;
        private int time;
        FirebaseStorage storage;
        ImageView imageView;
        final ProgressDialog mProgress;


        public PostareItemView(ViewGroup parent) {
            super(LayoutInflater.from(context).inflate(R.layout.null_activity, parent, false));
            mProgress = new ProgressDialog(parent.getContext());
            builder = new Dialog(parent.getContext());
            builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
            builder.getWindow().setBackgroundDrawable(
                    new ColorDrawable(android.graphics.Color.TRANSPARENT));
            imageView = new ImageView(parent.getContext());
            builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            builder.show();
        }

        public void bind(Postare post, String key, final int position) {

            final boolean isPhoto = post.getPhotoUrl() != null;
            test = post.getPhotoUrl();
            listaImagini.add(test);
            Log.d("listAllImages", listaImagini.toString() + " ");
            mProgress.setMessage("Wait");
            mProgress.show();
            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                int i = 0;

                public void run() {
                    if (isPhoto) {
                        imageView.setVisibility(View.VISIBLE);
                        Glide.with(imageView.getContext())
                                .load(listaImagini.get(i))
                                .into(imageView);
                    }
                    i++;
                    if (i > listaImagini.size() - 1) {
                        i = 0;
                    }
                    handler.postDelayed(this, 3000);
                    mProgress.dismiss();
                }
            };
            handler.postDelayed(runnable, 3000);


            imageView.setOnTouchListener(mTouchOutside);
            this.key = key;
        }

        private View.OnTouchListener mTouchOutside = new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                builder.dismiss();
                return false;
            }
        };
    }

    class PostAdapter extends RecyclerView.Adapter<RecyclerGetImagesView.PostareItemView> {
        private List<Postare> mPosts;
        private List<String> keys;

        public PostAdapter(List<Postare> mPosts, List<String> keys) {
            this.mPosts = mPosts;
            this.keys = keys;
        }

        @Override
        public RecyclerGetImagesView.PostareItemView onCreateViewHolder(ViewGroup parent, int viewType) {
            return new RecyclerGetImagesView.PostareItemView(parent);

        }


        @Override
        public void onBindViewHolder(RecyclerGetImagesView.PostareItemView holder, int position) {
            holder.bind(mPosts.get(position), keys.get(position), position);

        }

        @Override
        public int getItemCount() {
            return mPosts.size();
        }
    }
}

package com.neal.android.upitplatforma;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Loredana on 29.09.2018.
 */
public class Test {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabase;
    private List<Postare> listaPost=new ArrayList<>();

    public Test(FirebaseDatabase mFirebaseDatabase, DatabaseReference mMessagesDatabaseReference) {
        this.mFirebaseDatabase=mFirebaseDatabase;
        this.mDatabase=mMessagesDatabaseReference;

    }


    public interface DataStatus{
        void loadData(List<Postare> post,List<String> keys);
    }

    public void readVisit(final DataStatus dataStatus){
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaPost.clear();
                List<String> keys=new ArrayList<>();
                for(DataSnapshot keynode:dataSnapshot.getChildren()){
                    keys.add(keynode.getKey());
                    Postare post=keynode.getValue(Postare.class);
                    listaPost.add(post);
                }
                dataStatus.loadData(listaPost,keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

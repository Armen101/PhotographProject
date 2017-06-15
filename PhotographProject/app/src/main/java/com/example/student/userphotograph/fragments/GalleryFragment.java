package com.example.student.userphotograph.fragments;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.student.userphotograph.R;
import com.example.student.userphotograph.models.Picture;
import com.example.student.userphotograph.utilityes.MyAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {

    private ArrayList<Picture> pictures;
    private ProgressDialog progressDialog;
    private MyAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        pictures = new ArrayList<>();

        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mDatabaseGalleryRef = mDatabaseRef.child("photographs").child(mUser.getUid()).child("gallery");

        mDatabaseGalleryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                progressDialog.dismiss();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Log.i("=== galleryFragment", " add in list from adapter");

                    Picture picture = postSnapshot.getValue(Picture.class);
                    pictures.add(picture);
                }
                mAdapter = new MyAdapter(pictures);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });

    }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);
            RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_gallery);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(mAdapter);
            return rootView;
        }
}

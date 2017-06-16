package com.example.student.userphotograph.fragments;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.student.userphotograph.R;
import com.example.student.userphotograph.models.Picture;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GalleryFragment extends Fragment {


    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabaseGalleryRef;

    public GalleryFragment() {
    }

    public static GalleryFragment newInstance() {
        return new GalleryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);

        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mDatabaseGalleryRef = mDatabaseRef.child("photographs").child(mUser.getUid()).child("gallery");

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        FirebaseRecyclerAdapter<Picture, MyViewHolder> adapter;

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerView.setHasFixedSize(true);

        adapter = new FirebaseRecyclerAdapter<Picture, MyViewHolder>(
                Picture.class,
                R.layout.fragment_gallery,
                MyViewHolder.class,
                mDatabaseGalleryRef
        ) {
            @Override
            protected void populateViewHolder(MyViewHolder viewHolder, Picture model, int position) {

                viewHolder.tvGallery.setText(model.getTitle());
                Glide.with(getActivity())
                        .load(model.getImageUri())
                        .into(viewHolder.imgGallery);


            }
        };

        recyclerView.setAdapter(adapter);
        return rootView;
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgGallery;
        TextView tvGallery;
        public MyViewHolder(View view) {
            super(view);
            tvGallery = (TextView) view.findViewById(R.id.tv_image_gallery);
            imgGallery = (ImageView) view.findViewById(R.id.gallery_img);
        }
    }
}

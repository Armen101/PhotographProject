package com.example.student.userphotograph.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.student.userphotograph.R;
import com.example.student.userphotograph.models.Picture;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GalleryFragment extends Fragment {


    private DatabaseReference mDatabaseGalleryRef;
    private static GalleryFragment instance;

    public static GalleryFragment newInstance() {
        if(instance == null) instance = new GalleryFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);

        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        assert mUser != null;
        mDatabaseGalleryRef = mDatabaseRef.child("photographs").child(mUser.getUid()).child("gallery");

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setHasFixedSize(true);
        onCreateFirebaseRecyclerAdapter(recyclerView);

        return rootView;
    }

    private void onCreateFirebaseRecyclerAdapter(RecyclerView recyclerView) {

        FirebaseRecyclerAdapter<Picture, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Picture, MyViewHolder>(
                Picture.class,
                R.layout.layout_images,
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

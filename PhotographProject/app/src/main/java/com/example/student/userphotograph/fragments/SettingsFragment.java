package com.example.student.userphotograph.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.student.userphotograph.R;
import com.example.student.userphotograph.utilityes.MyAdapter;
import com.example.student.userphotograph.utilityes.SettingsHelper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    private EditText mName;
    private EditText mAddress;
    private EditText mCameraInfo;
    private EditText mPhone;

    private ArrayList<Uri> mUriList;

    private DatabaseReference mDatabaseRef;
    private ImageView mAvatar;
    private StorageReference mStorageAvatarRef;
    private StorageReference mStorageGalleryRef;
    private RecyclerView mRecyclerView;
    private SettingsHelper mHelper;
    private Button mAddImg;
    private MyAdapter mAdapter;

//    private FirebaseRecyclerAdapter<Image, MviewHolder> adapter(Query query){
//        return new FirebaseRecyclerAdapter<Image, MviewHolder>(
//                Image.class,
//                R.layout.content_grid_layout,
//                MviewHolder.class,
//                query) {
//            @Override
//            protected void populateViewHolder(MviewHolder viewHolder, Image model, int position) {
//
//            }
//        };
//    }

    public SettingsFragment() {}

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        mHelper = new SettingsHelper();



        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("photographs").child(mUser.getUid());
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        mStorageAvatarRef = mStorageRef.child("photographs").child("avatar").child(mUser.getUid());
        mStorageGalleryRef = mStorageRef.child("photographs").child("gallery").child(mUser.getUid());
        mHelper.downloadImageAndSetGallery(mStorageAvatarRef);
        mAdapter = new MyAdapter(mStorageGalleryRef, mUriList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        mName = (EditText) rootView.findViewById(R.id.et_st_name);
        mAddress = (EditText) rootView.findViewById(R.id.et_st_address);
        mCameraInfo = (EditText) rootView.findViewById(R.id.st_camera_info);
        mPhone = (EditText) rootView.findViewById(R.id.et_st_phone);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_gallery);
        mAddImg = (Button)rootView.findViewById(R.id.btn_st_add);
        recyclerViewActions();

        mAvatar = (ImageView) rootView.findViewById(R.id.st_avatar);
        mAvatar.setOnClickListener(this);
        Button mSave = (Button) rootView.findViewById(R.id.btn_st_save);
        mSave.setOnClickListener(this);
        return rootView;
    }

    private void recyclerViewActions() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(new MyAdapter(mStorageGalleryRef, mUriList));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_st_save: {
                saveInFbDb();
                break;
            }
            case R.id.st_avatar: {
                mHelper.actionPic(getActivity(), true);
                break;

            }
            case R.id.btn_st_add: {
                mHelper.actionPic(getActivity(), false);
                break;

            }

        }
    }



    private void saveInFbDb() {
        mDatabaseRef.child("name").setValue(mName.getText().toString());
        mDatabaseRef.child("address").setValue(mAddress.getText().toString());
        mDatabaseRef.child("cameraInfo").setValue(mCameraInfo.getText().toString());
        mDatabaseRef.child("phone").setValue(mPhone.getText().toString());
        Toast.makeText(getContext(), "Successfull saveing dates", Toast.LENGTH_SHORT).show();
    }

//    private void writeWithFbDb() {
//        mDatabaseRef.child("name").setValue(mName.getText().toString());
//        mDatabaseRef.child("address").setValue(mAddress.getText().toString());
//        mDatabaseRef.child("cameraInfo").setValue(mCameraInfo.getText().toString());
//        mDatabaseRef.child("phone").setValue(mPhone.getText().toString());
//        Toast.makeText(getContext(), "Successfull saveing dates", Toast.LENGTH_SHORT).show();
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SettingsHelper.REQUEST_AVATAR_ACTION_PICK && resultCode == RESULT_OK){
            final Uri uri = data.getData();
            mUriList.add(uri);
            mStorageAvatarRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mAvatar.setImageURI(uri);
                }
            });
        }

        if(requestCode == SettingsHelper.REQUEST_GALLERY_ACTION_PICK && resultCode == RESULT_OK){
            final Uri uri = data.getData();
            mUriList.add(uri);
            mStorageGalleryRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //mRecyclerView.setAdapter(new MyAdapter(mStorageGalleryRef,mUriList));
                    mAdapter.notifyDataSetChanged();



                }
            });
        }
    }

}

package com.example.student.userphotograph.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.student.userphotograph.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    private static final int REQUEST_ACTION_PICK = 100;
    private EditText mName;
    private EditText mAddress;
    private EditText mCameraInfo;
    private EditText mPhone;

    private DatabaseReference mRef;
    private ImageView mAvatar;
    private StorageReference avatarRef;
    private StorageReference galleryRef;
    private RecyclerView recyclerView;
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
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        mRef = FirebaseDatabase.getInstance().getReference().child("photographs").child(mUser.getUid());
        avatarRef = mStorageRef.child("photographs").child("avatar").child(mUser.getUid());
        galleryRef = mStorageRef.child("photographs").child("gallery").child(mUser.getUid());
        downloadImageAndSetGallery(avatarRef);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        mName = (EditText) rootView.findViewById(R.id.et_st_name);
        mAddress = (EditText) rootView.findViewById(R.id.et_st_address);
        mCameraInfo = (EditText) rootView.findViewById(R.id.st_camera_info);
        mPhone = (EditText) rootView.findViewById(R.id.et_st_phone);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_gallery);
        recyclerViewActions();

        mAvatar = (ImageView) rootView.findViewById(R.id.st_avatar);
        mAvatar.setOnClickListener(this);
        Button mSave = (Button) rootView.findViewById(R.id.btn_st_save);
        mSave.setOnClickListener(this);
        return rootView;
    }

    private void recyclerViewActions() {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        recyclerView.setHasFixedSize(true);
        // recyclerView.setAdapter(adapter());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_st_save: {
                saveInFbDb();
                break;
            }
            case R.id.st_avatar: {
                Intent actionPick = new Intent(Intent.ACTION_PICK);
                actionPick.setType("image/*");
                startActivityForResult(actionPick,REQUEST_ACTION_PICK);
                break;
            }
        }
    }

    private void saveInFbDb() {
        mRef.child("name").setValue(mName.getText().toString());
        mRef.child("address").setValue(mAddress.getText().toString());
        mRef.child("cameraInfo").setValue(mCameraInfo.getText().toString());
        mRef.child("phone").setValue(mPhone.getText().toString());
        Toast.makeText(getContext(), "Successfull saveing dates", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_ACTION_PICK && resultCode == RESULT_OK){
            final Uri uri = data.getData();
            avatarRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mAvatar.setImageURI(uri);
                }
            });
        }
    }

    private void downloadImageAndSetGallery(StorageReference ref) {
        ref.getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                mAvatar.setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {}
        });
    }



    private static class MviewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView tv;

        public MviewHolder(View itemView) {
            super(itemView);
            img = (ImageView)itemView.findViewById(R.id.temp_img_gallery);
            tv = (TextView)itemView.findViewById(R.id.temp_tv_gallery);
        }
    }
}

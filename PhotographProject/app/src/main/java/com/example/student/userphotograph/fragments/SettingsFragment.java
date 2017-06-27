package com.example.student.userphotograph.fragments;


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.student.userphotograph.R;
import com.example.student.userphotograph.models.Pictures;
import com.example.student.userphotograph.utilityes.Constants;
import com.example.student.userphotograph.utilityes.DownloadAvatar;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    private EditText mName;
    private EditText mAddress;
    private EditText mCameraInfo;
    private EditText mPhone;
    private EditText mNamePhoto;
    private ImageView mAvatar;

    private Uri mFilePath;
    private ImageView mImage;

    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabaseGalleryRef;
    private StorageReference mStorageAvatarRef;
    private StorageReference mStorageGalleryRef;

    private LinearLayout mCooseFileLayout;
    private List<Pictures> mItemViewPager;
    private FirebaseUser mUser;

    public SettingsFragment() {
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        findViewById(rootView);
        firebaseRef();

        mItemViewPager = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setHasFixedSize(true);
        onCreateFirebaseRecyclerAdapter(recyclerView);

        writeWithFbDb();
        DownloadAvatar.downloadImageAndSetAvatar(mStorageAvatarRef, mAvatar);
        return rootView;
    }

    private void findViewById(View rootView) {

        mName = (EditText) rootView.findViewById(R.id.et_st_name);
        mAddress = (EditText) rootView.findViewById(R.id.et_st_address);
        mCameraInfo = (EditText) rootView.findViewById(R.id.st_camera_info);
        mPhone = (EditText) rootView.findViewById(R.id.et_st_phone);
        mAvatar = (ImageView) rootView.findViewById(R.id.st_avatar);
        mImage = (ImageView) rootView.findViewById(R.id.img_gallery);
        mNamePhoto = (EditText) rootView.findViewById(R.id.st_name_photo);
        ImageView mAddImg = (ImageView) rootView.findViewById(R.id.add_image);
        mCooseFileLayout = (LinearLayout) rootView.findViewById(R.id.choose_file_layout);

        Button saveAllInfo = (Button) rootView.findViewById(R.id.btn_st_save_info);
        Button choosePhoto = (Button) rootView.findViewById(R.id.btn_st_choose_photo);
        Button uploadPhoto = (Button) rootView.findViewById(R.id.btn_st_upload_phote);

        uploadPhoto.setOnClickListener(this);
        choosePhoto.setOnClickListener(this);
        saveAllInfo.setOnClickListener(this);
        mAvatar.setOnClickListener(this);
        mAddImg.setOnClickListener(this);
    }

    private void onCreateFirebaseRecyclerAdapter(RecyclerView recyclerView) {

        final FirebaseRecyclerAdapter<Pictures, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Pictures, MyViewHolder>(
                Pictures.class,
                R.layout.layout_images,
                MyViewHolder.class,
                mDatabaseGalleryRef
        ) {
            @Override
            protected void populateViewHolder(MyViewHolder viewHolder, Pictures model, final int position) {
                viewHolder.tvGallery.setText(model.getTitle());
                Glide.with(getActivity())
                        .load(model.getImageUri())
                        .into(viewHolder.imgGallery);

                mItemViewPager.add(model);

                viewHolder.imgGallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("images", (Serializable) mItemViewPager);
                        bundle.putInt("position", position);

                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                        newFragment.setArguments(bundle);
                        newFragment.show(ft, "slideshow");
                    }
                });

                viewHolder.imgGallery.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        final AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(getContext());
                        mAlertDialog.setTitle("Remove")
                                .setMessage("Are you sure?");
                        mAlertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {


                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                String imageName = getItem(position).getImageName();
                                getRef(position).removeValue();

                                StorageReference sRef = FirebaseStorage.getInstance()
                                        .getReference().child("photographs").child("gallery")
                                        .child(mUser.getUid()).child("uploads").child(imageName);
                                sRef.delete();

                                notifyDataSetChanged();
                                Toast.makeText(getContext(), "Removed", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getContext(), "Canceled", Toast.LENGTH_SHORT).show();
                            }
                        })
                                .create().show();
                        return true;
                    }
                });
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

    private void firebaseRef() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        mUser = auth.getCurrentUser();

        assert mUser != null;
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("photographs").child(mUser.getUid());
        mDatabaseGalleryRef = mDatabaseRef.child("gallery");

        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        mStorageAvatarRef = mStorageRef.child("photographs").child("avatar").child(mUser.getUid());
        mStorageGalleryRef = mStorageRef.child("photographs").child("gallery").child(mUser.getUid());

        mDatabaseRef.child("uid").setValue(mUser.getUid());
        mDatabaseRef.child("email").setValue(mUser.getEmail());
    }

    private void writeWithFbDb() {
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue(String.class);
                String address = dataSnapshot.child("address").getValue(String.class);
                String cameraInfo = dataSnapshot.child("cameraInfo").getValue(String.class);
                String phone = dataSnapshot.child("phone").getValue(String.class);

                mName.setText(name);
                mAddress.setText(address);
                mCameraInfo.setText(cameraInfo);
                mPhone.setText(phone);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    private void saveInFbDb() {
        mDatabaseRef.child("name").setValue(mName.getText().toString());
        mDatabaseRef.child("address").setValue(mAddress.getText().toString());
        mDatabaseRef.child("cameraInfo").setValue(mCameraInfo.getText().toString());
        mDatabaseRef.child("phone").setValue(mPhone.getText().toString());
        Toast.makeText(getContext(), "Successfull saveing dates", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_st_save_info: {
                saveInFbDb();
                break;
            }
            case R.id.st_avatar: {
                choosePic(Constants.REQUEST_AVATAR_CHOOSE_PICK);
                break;
            }
            case R.id.btn_st_upload_phote: {
                mNamePhoto.setVisibility(View.GONE);
                mNamePhoto.setText(mNamePhoto.getText().toString());
                uploadFile();
                mFilePath = null;
                mImage.setImageBitmap(null);
                break;
            }
            case R.id.btn_st_choose_photo: {
                mNamePhoto.setText("");
                mNamePhoto.setVisibility(View.VISIBLE);
                choosePic(Constants.REQUEST_GALLERY_CHOOSE_PICK);
                break;
            }
            case R.id.add_image: {
                if (mCooseFileLayout.getVisibility() == View.VISIBLE) {
                    mCooseFileLayout.setVisibility(View.GONE);
                    if (getResources().getDisplayMetrics().widthPixels > getResources().getDisplayMetrics().
                            heightPixels) {
                        mCameraInfo.setVisibility(View.VISIBLE);
                        mAddress.setVisibility(View.VISIBLE);
                    }
                } else {
                    mCooseFileLayout.setVisibility(View.VISIBLE);
                    if (getResources().getDisplayMetrics().widthPixels > getResources().getDisplayMetrics().
                            heightPixels) {
                        mCameraInfo.setVisibility(View.GONE);
                        mAddress.setVisibility(View.GONE);
                    }
                }
                break;
            }
        }
    }

    private void choosePic(int requestCode) {
        Intent choosePicIntent = new Intent(Intent.ACTION_GET_CONTENT);
        choosePicIntent.setType("image/*");
        startActivityForResult(choosePicIntent, requestCode);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadFile() {
        if (mFilePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            final String imageName = System.currentTimeMillis() + "." + getFileExtension(mFilePath);
            StorageReference sRef = mStorageGalleryRef.child(Constants.STORAGE_PATH_UPLOADS + imageName);

            sRef.putFile(mFilePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "File Uploaded ", Toast.LENGTH_LONG).show();

                            @SuppressWarnings("VisibleForTests")
                            Pictures picture = new Pictures(mNamePhoto.getText().toString().trim(), taskSnapshot.getDownloadUrl().toString(), imageName);

                            String uploadId = mDatabaseGalleryRef.push().getKey();
                            mDatabaseGalleryRef.child(uploadId).setValue(picture);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext().getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests")
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });

        } else {
            Toast.makeText(getContext(), "Warning !!!, Error file ", Toast.LENGTH_LONG).show();
            mNamePhoto.setText("");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constants.REQUEST_AVATAR_CHOOSE_PICK && resultCode == RESULT_OK) {
            final Uri uri = data.getData();

            mStorageAvatarRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mAvatar.setImageURI(uri);

                }
            });
            //mDatabaseRef.child("avatar").setValue(uri.toString());
        }
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_GALLERY_CHOOSE_PICK && resultCode == RESULT_OK && data.getData() != null) {
            mFilePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mFilePath);
                mImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

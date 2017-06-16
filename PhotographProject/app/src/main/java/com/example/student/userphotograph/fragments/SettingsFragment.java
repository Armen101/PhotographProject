package com.example.student.userphotograph.fragments;


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.student.userphotograph.R;
import com.example.student.userphotograph.models.Picture;
import com.example.student.userphotograph.utilityes.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static com.example.student.userphotograph.utilityes.Constants.REQUEST_AVATAR_CHOOSE_PICK;
import static com.example.student.userphotograph.utilityes.Constants.REQUEST_GALLERY_CHOOSE_PICK;
import static com.example.student.userphotograph.utilityes.Constants.STORAGE_PATH_UPLOADS;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    private EditText mName;
    private EditText mAddress;
    private EditText mCameraInfo;
    private EditText mPhone;
    private EditText mNamePhoto;
    private ImageView mAvatar;

    private Uri filePath;
    private ImageView mImage;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;

    private DatabaseReference mDatabaseGalleryRef;
    private StorageReference mStorageAvatarRef;
    private StorageReference mStorageGalleryRef;

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

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("photographs").child(mUser.getUid());
        mDatabaseGalleryRef = mDatabaseRef.child("gallery");

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mStorageAvatarRef = mStorageRef.child("photographs").child("avatar").child(mUser.getUid());
        mStorageGalleryRef = mStorageRef.child("photographs").child("gallery").child(mUser.getUid());

        writeWithFbDb();
        replaceFragment();
        downloadImageAndSetGallery(mStorageAvatarRef);

        return rootView;
    }

    private void findViewById(View rootView) {
        mName = (EditText) rootView.findViewById(R.id.et_st_name);
        mAddress = (EditText) rootView.findViewById(R.id.et_st_address);
        mCameraInfo = (EditText) rootView.findViewById(R.id.st_camera_info);
        mPhone = (EditText) rootView.findViewById(R.id.et_st_phone);
        mAvatar = (ImageView) rootView.findViewById(R.id.st_avatar);

        Button mSave = (Button) rootView.findViewById(R.id.btn_st_save_info);
        Button mChoosePhoto = (Button) rootView.findViewById(R.id.btn_st_choose_photo);
        Button mUploadPhoto = (Button) rootView.findViewById(R.id.btn_st_upload_phote);

        mImage = (ImageView) rootView.findViewById(R.id.img_gallery);
        mNamePhoto = (EditText) rootView.findViewById(R.id.st_name_photo);

        mUploadPhoto.setOnClickListener(this);
        mChoosePhoto.setOnClickListener(this);
        mSave.setOnClickListener(this);
        mAvatar.setOnClickListener(this);
    }

    private void saveInFbDb() {
        mDatabaseRef.child("name").setValue(mName.getText().toString());
        mDatabaseRef.child("address").setValue(mAddress.getText().toString());
        mDatabaseRef.child("cameraInfo").setValue(mCameraInfo.getText().toString());
        mDatabaseRef.child("phone").setValue(mPhone.getText().toString());
        Toast.makeText(getContext(), "Successfull saveing dates", Toast.LENGTH_SHORT).show();
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

    private void replaceFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.container_gallery_fragment, new GalleryFragment())
                .commit();
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
                mNamePhoto.setText(mNamePhoto.getText().toString());
                uploadFile();
                break;
            }
            case R.id.btn_st_choose_photo: {
                choosePic(Constants.REQUEST_GALLERY_CHOOSE_PICK);
                break;
            }
        }
    }

    public void choosePic(int requestCode) {
        Intent choosePicIntent = new Intent(Intent.ACTION_GET_CONTENT);
        choosePicIntent.setType("image/*");
        startActivityForResult(choosePicIntent, requestCode);
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            StorageReference sRef = mStorageGalleryRef.child(Constants.STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + getFileExtension(filePath));

            sRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext().getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();

                            @SuppressWarnings("VisibleForTests")
                            Picture picture = new Picture(mNamePhoto.getText().toString().trim(), taskSnapshot.getDownloadUrl().toString());
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
            Toast.makeText(getContext().getApplicationContext(), "Warning !!!, Error file ", Toast.LENGTH_LONG).show();
        }
    }

    public void downloadImageAndSetGallery(StorageReference ref) {
         ref.getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        mAvatar.setImageBitmap(bitmap);
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_AVATAR_CHOOSE_PICK && resultCode == RESULT_OK) {
            final Uri uri = data.getData();
            mStorageAvatarRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mAvatar.setImageURI(uri);
                }
            });
        }

        if (requestCode == Constants.REQUEST_GALLERY_CHOOSE_PICK && resultCode == RESULT_OK && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                mImage.setImageBitmap(bitmap);
                replaceFragment();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
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


    private DatabaseReference mDatabaseRef;
    private ImageView mAvatar;
    private StorageReference mStorageAvatarRef;
    private StorageReference mStorageGalleryRef;
    private DatabaseReference mDatabaseGalleryRef;
    private Uri filePath;
    private ImageView mImage;



    public SettingsFragment() {}

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("photographs").child(mUser.getUid());
        mDatabaseGalleryRef = mDatabaseRef.child("gallery");

        writeWithFbDb();

        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        mStorageAvatarRef = mStorageRef.child("photographs").child("avatar").child(mUser.getUid());
        mStorageGalleryRef = mStorageRef.child("photographs").child(mUser.getUid());



        downloadImageAndSetGallery(mStorageAvatarRef);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        mName = (EditText) rootView.findViewById(R.id.et_st_name);
        mAddress = (EditText) rootView.findViewById(R.id.et_st_address);
        mCameraInfo = (EditText) rootView.findViewById(R.id.st_camera_info);
        mPhone = (EditText) rootView.findViewById(R.id.et_st_phone);

        Button mSave = (Button) rootView.findViewById(R.id.btn_st_save_info);
        Button mChoosePhoto = (Button)rootView.findViewById(R.id.btn_st_choose_photo);
        Button mUploadPhoto = (Button) rootView.findViewById(R.id.btn_st_upload_phote);

        mUploadPhoto.setOnClickListener(this);
        mChoosePhoto.setOnClickListener(this);
        mSave.setOnClickListener(this);

        mImage = (ImageView)rootView.findViewById(R.id.img_gallery);
        mNamePhoto = (EditText)rootView.findViewById(R.id.st_name_photo);

        mAvatar = (ImageView) rootView.findViewById(R.id.st_avatar);
        mAvatar.setOnClickListener(this);

        replaceFragment();
        return rootView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_st_save_info: {
                saveInFbDb();
                break;
            }
            case R.id.st_avatar: {
                choosePic(REQUEST_AVATAR_CHOOSE_PICK);
                break;
            }
            case R.id.btn_st_upload_phote: {
                mNamePhoto.setVisibility(View.GONE);
                mNamePhoto.setText("");
                uploadFile();
                break;
            }
            case R.id.btn_st_choose_photo: {
                mNamePhoto.setVisibility(View.VISIBLE);
                choosePic(REQUEST_GALLERY_CHOOSE_PICK);
                break;
            }
        }
    }

    private void replaceFragment(){
        getFragmentManager().beginTransaction()
                .replace(R.id.container_gallery_fragment, new GalleryFragment())
                .commit();
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
            public void onCancelled(DatabaseError error) {}
        });




        Toast.makeText(getContext(), "Successfull saveing dates", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_AVATAR_CHOOSE_PICK && resultCode == RESULT_OK){
            final Uri uri = data.getData();
            mStorageAvatarRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mAvatar.setImageURI(uri);
                }
            });
        }

        if (requestCode == REQUEST_GALLERY_CHOOSE_PICK && resultCode == RESULT_OK && data.getData() != null) {
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

    /*
     We will rename the file to a unique name
     as if we upload the file with same name
     then files would be overwritten.
     So after renaming the file the extension should remain the same.
     So inside MainActivity.java create a method getFileExtension()
     and it will return as the extension of the selected file by taking Uri object.
     */
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
            StorageReference sRef = mStorageGalleryRef.child(STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + getFileExtension(filePath));

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
            Toast.makeText(getContext().getApplicationContext(), "Warning !!!, null file ", Toast.LENGTH_LONG).show();
        }
    }

    public void choosePic(int requestCode) {
        Intent choosePicIntent = new Intent(Intent.ACTION_GET_CONTENT);
        choosePicIntent.setType("image/*");
        startActivityForResult(choosePicIntent, requestCode);
    }

    public void downloadImageAndSetGallery(StorageReference ref) {
        ref.getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap  bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                mAvatar.setImageBitmap(bitmap);
            }
        });
    }
}
package com.example.student.userphotograph.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.student.userphotograph.utilityes.FirebaseHelper;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.example.student.userphotograph.utilityes.Constants.ADDRESS;
import static com.example.student.userphotograph.utilityes.Constants.AVATAR;
import static com.example.student.userphotograph.utilityes.Constants.AVATAR_URI;
import static com.example.student.userphotograph.utilityes.Constants.CAMERA_INFO;
import static com.example.student.userphotograph.utilityes.Constants.EMAIL;
import static com.example.student.userphotograph.utilityes.Constants.GALLERY;
import static com.example.student.userphotograph.utilityes.Constants.NAME;
import static com.example.student.userphotograph.utilityes.Constants.PHONE;
import static com.example.student.userphotograph.utilityes.Constants.PHOTOGRAPHS;
import static com.example.student.userphotograph.utilityes.Constants.USER_ID;

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
        FirebaseHelper.downloadImageAndSetAvatar(mStorageAvatarRef, mAvatar);

        return rootView;
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(PHONE, mPhone.getText().toString());
        outState.putString(ADDRESS, mAddress.getText().toString());
        outState.putString(CAMERA_INFO, mCameraInfo.getText().toString());
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
                        bundle.putSerializable(getString(R.string.images), (Serializable) mItemViewPager);
                        bundle.putInt(getString(R.string.position), position);

                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                        newFragment.setArguments(bundle);
                        newFragment.show(ft, getString(R.string.slideshow));
                    }
                });

                viewHolder.imgGallery.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        final AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(getContext());
                        mAlertDialog.setTitle(R.string.removed)
                                .setMessage(R.string.are_you_sure);
                        mAlertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                String imageName = getItem(position).getImageName();
                                getRef(position).removeValue();

                                StorageReference sRef = FirebaseStorage.getInstance()
                                        .getReference().child(PHOTOGRAPHS).child(GALLERY)
                                        .child(mUser.getUid()).child(imageName);
                                sRef.delete();

                                notifyDataSetChanged();
                                Toast.makeText(getContext(), R.string.removed, Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getContext(), R.string.canceled, Toast.LENGTH_SHORT).show();
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
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child(PHOTOGRAPHS).child(mUser.getUid());
        mDatabaseGalleryRef = mDatabaseRef.child(GALLERY);

        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        mStorageAvatarRef = mStorageRef.child(PHOTOGRAPHS).child(AVATAR).child(mUser.getUid());
        mStorageGalleryRef = mStorageRef.child(PHOTOGRAPHS).child(GALLERY).child(mUser.getUid());

        mDatabaseRef.child(USER_ID).setValue(mUser.getUid());
        mDatabaseRef.child(EMAIL).setValue(mUser.getEmail());
    }

    private void writeWithFbDb() {
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child(NAME).getValue(String.class);
                String address = dataSnapshot.child(ADDRESS).getValue(String.class);
                String cameraInfo = dataSnapshot.child(CAMERA_INFO).getValue(String.class);
                String phone = dataSnapshot.child(PHONE).getValue(String.class);

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
        mDatabaseRef.child(NAME).setValue(mName.getText().toString());
        mDatabaseRef.child(ADDRESS).setValue(mAddress.getText().toString());
        mDatabaseRef.child(CAMERA_INFO).setValue(mCameraInfo.getText().toString());
        mDatabaseRef.child(PHONE).setValue(mPhone.getText().toString());
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

                String mImageName = System.currentTimeMillis() + "." + FirebaseHelper.getFileExtension(mFilePath, getActivity());
                FirebaseHelper.upload(getContext(), mImageName, mNamePhoto, mDatabaseGalleryRef, mStorageGalleryRef, mFilePath);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constants.REQUEST_AVATAR_CHOOSE_PICK && resultCode == RESULT_OK) {
            final Uri uri = data.getData();

            mStorageAvatarRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mAvatar.setImageURI(uri);
                    @SuppressWarnings("VisibleForTests")Uri uri = taskSnapshot.getDownloadUrl();
                    assert uri != null;
                    mDatabaseRef.child(AVATAR_URI).setValue(uri.toString());
                }
            });
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

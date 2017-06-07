package com.example.student.userphotograph.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.student.userphotograph.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    private EditText mName;
    private EditText mAddress;
    private EditText mCameraInfo;
    private EditText mPhone;

    private DatabaseReference mRef;

    public SettingsFragment() {}

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference().child("photograph").child(mUser.getUid());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        mName = (EditText)rootView.findViewById(R.id.et_st_name);
        mAddress = (EditText)rootView.findViewById(R.id.et_st_address);
        mCameraInfo = (EditText)rootView.findViewById(R.id.st_camera_info);
        mPhone = (EditText)rootView.findViewById(R.id.et_st_phone);
        Button mSave = (Button) rootView.findViewById(R.id.btn_st_save);
        mSave.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_st_save:{
                mRef.child("name").setValue(mName.getText().toString());
                mRef.child("address").setValue(mAddress.getText().toString());
                mRef.child("cameraInfo").setValue(mCameraInfo.getText().toString());
                mRef.child("phone").setValue(mPhone.getText().toString());
                break;
            }
            case R.id.st_avatar:{

                break;
            }
        }
    }

    private boolean isValidateForm(String field){
        return field.length() != 0;
    }
}

package com.example.student.userphotograph.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.student.userphotograph.R;

public class SignInSignUpFragment extends Fragment {

    public SignInSignUpFragment() {
        // Required empty public constructor
    }


    public static SignInSignUpFragment newInstance() {
        SignInSignUpFragment fragment = new SignInSignUpFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in_sign_up, container, false);
    }


}

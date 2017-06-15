package com.example.student.userphotograph.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.student.userphotograph.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class ForgotPasswordFragment extends Fragment implements View.OnClickListener {

    private EditText etEmail;
    private ProgressBar progressBar;
    private Button btnReset;

    private FirebaseAuth mAuth;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    public static ForgotPasswordFragment newInstance() {
        ForgotPasswordFragment fragment = new ForgotPasswordFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        findIdAndListeners(rootView);
        return rootView;
    }

    private void findIdAndListeners(View rootView){
        etEmail = (EditText) rootView.findViewById(R.id.et_email);
        btnReset = (Button) rootView.findViewById(R.id.btn_reset);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressbar);

        btnReset.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reset: {

                String email = etEmail.getText().toString().trim();
                if(email.isEmpty()){
                    Toast.makeText(getContext(), "Email is a empty", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (TextUtils.isEmpty(email)) {
//                    Toast.makeText(getContext(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
//                }

                progressBar.setVisibility(View.VISIBLE);
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getContext(), "Reset password send email", Toast.LENGTH_SHORT).show();
                            Fragment fragment = SignInFragment.newInstance();
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.containerLogin, fragment);
                            ft.addToBackStack(null);
                            ft.commit();
                        }else {
                            Toast.makeText(getContext(), "Failed to send reset email!", Toast.LENGTH_SHORT).show();

                        }
                        progressBar.setVisibility(View.GONE);

                    }
                });
                break;
            }

        }
    }

}

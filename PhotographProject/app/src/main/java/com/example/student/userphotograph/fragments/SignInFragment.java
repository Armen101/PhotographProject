package com.example.student.userphotograph.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.student.userphotograph.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInFragment extends Fragment implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText etEmail;
    private EditText etPassword;
    private Button btnSignIn;

    public SignInFragment() {}

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_sign_in, container, false);
        etEmail = (EditText)rootView.findViewById(R.id.et_email_sign_in);
        etPassword = (EditText)rootView.findViewById(R.id.et_email_sign_in);
        btnSignIn = (Button)rootView.findViewById(R.id.sing_in_button);
        btnSignIn.setOnClickListener(this);
        return rootView;
    }

    public void signin(String email , String password)
    {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(getContext(), "Aвторизация успешна", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(getContext(), "Aвторизация провалена", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sing_in_button:{
                signin(etEmail.getText().toString(),etPassword.getText().toString());
                break;
            }
        }
    }
}

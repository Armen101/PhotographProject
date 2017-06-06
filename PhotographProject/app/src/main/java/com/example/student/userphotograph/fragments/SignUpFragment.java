package com.example.student.userphotograph.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpFragment extends Fragment implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private static SignUpFragment instance;
    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private DatabaseReference mRef;

    public SignUpFragment() {}

    public static SignUpFragment newInstance() {
        if (instance == null) instance = new SignUpFragment();
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference().child("photographs");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);
        findViewsSetLIsteners(rootView);
        return rootView;
    }

    private void findViewsSetLIsteners(View rootView) {
        etName = (EditText) rootView.findViewById(R.id.et_name);
        etEmail = (EditText) rootView.findViewById(R.id.et_sp_email);
        etPassword = (EditText) rootView.findViewById(R.id.et_sp_password);
        Button btnSignUp = (Button) rootView.findViewById(R.id.btn_sign_up);
        btnSignUp.setOnClickListener(this);

    }

    private boolean isValidateForm() {
        boolean valid = true;

        String email = etEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Required");
            valid = false;
        } else {
            etEmail.setError(null);
        }

        String password = etPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Required");
            valid = false;
        } else {
            etPassword.setError(null);
        }

        return valid;
    }

    public void registration() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (isValidateForm()) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                mRef.child(user.getUid()).child("name").setValue(etName.getText().toString());
                                Toast.makeText(getContext(), "Successful registration", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(getContext(), "Unsuccessful registration", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_sign_up) registration();
    }
}

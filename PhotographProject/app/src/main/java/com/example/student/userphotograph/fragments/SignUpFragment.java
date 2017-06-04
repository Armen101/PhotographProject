package com.example.student.userphotograph.fragments;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
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
public class SignUpFragment extends BlankFragment implements View.OnClickListener {

    private static final String TAG = "======= ";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private static SignUpFragment instance;
    private OnSignUpFragmentListener mListener;
    private EditText etName;
    private EditText etLastName;
    private EditText etUserName;
    private EditText etEmail;
    private EditText etPassword;

    public SignUpFragment() {}

    public static SignUpFragment newInstance() {
        if(instance == null) instance = new SignUpFragment();
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        setAuthStateListener();
    }

    private void setAuthStateListener() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    createAccount();
                } else {
                    Log.d(TAG, "user signed_out");
                }
            }
        };
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
        etLastName = (EditText)rootView.findViewById(R.id.et_last_name);
        etUserName = (EditText)rootView.findViewById(R.id.et_user_name);
        etEmail = (EditText)rootView.findViewById(R.id.et_sp_email);
        etPassword = (EditText)rootView.findViewById(R.id.et_sp_password);
        Button btnSignUp = (Button)rootView.findViewById(R.id.btn_sign_up);
        btnSignUp.setOnClickListener(this);

    }
    private void createAccount() {
        if (!validateForm()) {
            return;
        }
        showProgressDialog();

        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail :onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail :onComplete: not soccesfull" + task.isSuccessful());
                        }
                        hideProgressDialog();
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = etEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Required.");
            valid = false;
        } else {
            etEmail.setError(null);
        }

        String password = etPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Required.");
            valid = false;
        } else {
            etPassword.setError(null);
        }

        return valid;
    }

    public void registration (){
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getContext(), "Регистрация успешна", Toast.LENGTH_SHORT).show();
                        }
                        else Toast.makeText(getContext(), "Регистрация провалена", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSignUpFragmentListener) {
            mListener = (OnSignUpFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_sign_up) registration();
    }

    public interface OnSignUpFragmentListener {
        void onSignUp(FirebaseUser user);
    }
}

package com.example.student.userphotograph.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.student.userphotograph.R;

public class SignInSignUpFragment extends Fragment implements View.OnClickListener {
    private Button btnSignIn;
    private EditText edtLogin;
    private EditText edtPassword;
    private Button btnSignUp;
    private TextView tvForgotPassword;
    private mListener mListener;
    private mListenerUp mListenerUp;
    private mListenerForgot mListenerForgot;


    public static SignInSignUpFragment newInstance() {
        SignInSignUpFragment fragment = new SignInSignUpFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mListener = (mListener) getContext();
        mListenerUp = (mListenerUp) getContext();
        mListenerForgot = (mListenerForgot) getContext();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_in_sign_up, container, false);

        btnSignIn = (Button) rootView.findViewById(R.id.btnSignIn);
        btnSignUp = (Button) rootView.findViewById(R.id.btnSignUp);

        edtLogin = (EditText) rootView.findViewById(R.id.edtLogin);
        edtPassword = (EditText) rootView.findViewById(R.id.edtPassword);
        tvForgotPassword = (TextView) rootView.findViewById(R.id.txtForgotPassword);

        btnSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignIn: {
                mListener.onClickable();
                break;
            }
            case R.id.btnSignUp: {
                mListenerUp.onClickableUp();
                break;
            }
            case R.id.txtForgotPassword: {
                mListenerForgot.onClickableForgot();
                break;
            }
        }

    }

    public interface mListener {
        void onClickable();
    }

    public interface mListenerUp {
        void onClickableUp();
    }

    public interface mListenerForgot {
        void onClickableForgot();
    }




}

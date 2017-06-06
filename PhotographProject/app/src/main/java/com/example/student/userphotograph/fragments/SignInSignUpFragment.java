package com.example.student.userphotograph.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.student.userphotograph.R;

public class SignInSignUpFragment extends Fragment implements View.OnClickListener {

    public SignInSignUpFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_in_sign_up, container, false);
        findViewsSetListeners(rootView);
        return rootView;
    }

    private void findViewsSetListeners(View rootView) {
        Button signIn = (Button) rootView.findViewById(R.id.choose_sign_in);
        Button signUp = (Button) rootView.findViewById(R.id.choose_sign_up);
        signIn.setOnClickListener(this);
        signUp.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.choose_sign_in:{
                replaceFragment(SignInFragment.newInstance());
                break;
            }
            case R.id.choose_sign_up:{
                replaceFragment(SignUpFragment.newInstance());
                break;
            }
        }
    }

    private  void replaceFragment(Fragment fragment){
        getFragmentManager().beginTransaction()
                .replace(R.id.containerLogin, fragment)
                .addToBackStack(null)
                .commit();
    }
}

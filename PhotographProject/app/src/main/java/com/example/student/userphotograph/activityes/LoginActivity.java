package com.example.student.userphotograph.activityes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.example.student.userphotograph.R;
import com.example.student.userphotograph.fragments.SignInFragment;

public class LoginActivity  extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        replaceFragment(SignInFragment.newInstance());
    }

    private void replaceFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.containerLogin, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

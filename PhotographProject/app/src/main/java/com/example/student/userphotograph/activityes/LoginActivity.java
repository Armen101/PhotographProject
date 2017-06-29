package com.example.student.userphotograph.activityes;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.example.student.userphotograph.R;
import com.example.student.userphotograph.fragments.ForgotPasswordFragment;
import com.example.student.userphotograph.fragments.SignInFragment;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.example.student.userphotograph.utilityes.Constants.IS_SHOW__FORGOT_PASSWORD;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("choco_cooky.ttf")
                .build());

        if (IS_SHOW__FORGOT_PASSWORD) {
            replaceFragment(ForgotPasswordFragment.newInstance());
        } else {
            replaceFragment(SignInFragment.newInstance());
        }
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_login, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        Fragment mFragment = getSupportFragmentManager().findFragmentById(R.id.container_login);
        if (mFragment != null) {
            super.onBackPressed();
        }
    }
}

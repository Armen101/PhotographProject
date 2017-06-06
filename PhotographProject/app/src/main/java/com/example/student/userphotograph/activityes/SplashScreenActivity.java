package com.example.student.userphotograph.activityes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.student.userphotograph.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageView splashScreen = (ImageView) findViewById(R.id.splash_screen);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animation);
        splashScreen.setAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //TODO write
    private void startActivity() {
        boolean isLoggedIn = true;
        finish();
        if(isLoggedIn) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        } else {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
    }
}

package com.example.student.userphotograph.activityes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.student.userphotograph.R;
import com.example.student.userphotograph.models.RatingModel;
import com.example.student.userphotograph.utilityes.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static com.example.student.userphotograph.utilityes.Constants.RATING_UID;
import static com.example.student.userphotograph.utilityes.Constants.R_UID1;
import static com.example.student.userphotograph.utilityes.Constants.R_UID2;
import static com.example.student.userphotograph.utilityes.Constants.R_UID3;
import static com.example.student.userphotograph.utilityes.Constants.R_UID4;
import static com.example.student.userphotograph.utilityes.Constants.R_UID5;

public class SplashScreenActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseRef;
    private RatingModel info;
    private Map<String, Long> photographersRatings = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabaseRef = database.getReference().child(Constants.PHOTOGRAPHS);

        setLocale();
        photographers();

        ImageView splashScreen = (ImageView) findViewById(R.id.splash_screen);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation);
        splashScreen.setAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                goToActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private void setLocale() {
        SharedPreferences shared = getSharedPreferences("localization", MODE_PRIVATE);
        String language = shared.getString(Constants.DEFAULT_LANGUAGE, "");
        if (TextUtils.isEmpty(language)) {
            shared.edit().putString(Constants.DEFAULT_LANGUAGE, Locale.getDefault().getLanguage()).apply();
        }
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    private void goToActivity() {
        finish();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Intent intent;
        if (user != null) {
            intent = new Intent(this, HomeActivity.class);
            Toast.makeText(this, user.getEmail(), Toast.LENGTH_LONG).show();
        } else {
            intent = new Intent(this, LoginActivity.class);
        }
        finish();
        startActivity(intent);
    }

    private void photographers() {
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    info = postSnapshot.getValue(RatingModel.class);
                    photographersRatings.put(info.getUid(), info.getRating());
                }
                sortedRating();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sortedRating() {
        String uid[] = new String[5];
        Set<Map.Entry<String, Long>> set = photographersRatings.entrySet();
        List<Map.Entry<String, Long>> list = new ArrayList<Map.Entry<String, Long>>(set);
        Collections.sort(list, new Comparator<Map.Entry<String, Long>>() {
            public int compare(Map.Entry<String, Long> o1,
                               Map.Entry<String, Long> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        SharedPreferences ratingShared = getSharedPreferences(RATING_UID, MODE_PRIVATE);
        for (int i = 0; i < 5; i++) {
            uid[i] = list.get(i).toString().substring(0, 28);
            ratingShared.edit().putString(R_UID1, uid[0]).apply();
            ratingShared.edit().putString(R_UID2, uid[1]).apply();
            ratingShared.edit().putString(R_UID3, uid[2]).apply();
            ratingShared.edit().putString(R_UID4, uid[3]).apply();
            ratingShared.edit().putString(R_UID5, uid[4]).apply();
        }
    }

}

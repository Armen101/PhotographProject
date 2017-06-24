package com.example.student.userphotograph.activityes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.student.userphotograph.R;
import com.example.student.userphotograph.fragments.GMapFragment;
import com.example.student.userphotograph.fragments.SettingsFragment;
import com.example.student.userphotograph.utilityes.DownloadAvatar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener {

    private StorageReference mStorageAvatarRef;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private DrawerLayout mDrawer;
    private NavigationView navigationView;
    private ImageView mNavDrawerAvatar;
    private TextView mLastName;
    private TextView mEmail;
    private Typeface mTypeface;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("choco_cooky.ttf")
                .build());
        mTypeface = Typeface.createFromAsset(getAssets(), "choco_cooky.ttf");

        findViewById();
        writeFbDb();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        replaceFragment(SettingsFragment.newInstance());

    }

    private void findViewById() {
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        View header = navigationView.getHeaderView(0);

        mLastName = (TextView) header.findViewById(R.id.tv_name_last_name);
        mLastName.setTypeface(mTypeface);
        mEmail = (TextView) header.findViewById(R.id.tv_email);
        mEmail.setTypeface(mTypeface);
        mNavDrawerAvatar = (ImageView) header.findViewById(R.id.img_nav_drawer);

        navigationView.setNavigationItemSelectedListener(this);
        mDrawer.addDrawerListener(this);
    }

    private void writeFbDb() {



        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("photographs").child(mUser.getUid());
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mStorageAvatarRef = mStorageRef.child("photographs").child("avatar").child(mUser.getUid());

        DownloadAvatar.downloadImageAndSetAvatar(mStorageAvatarRef, mNavDrawerAvatar);

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String mLastNameRef = dataSnapshot.child("name").getValue(String.class);
                mLastName.setText(mLastNameRef);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mEmail.setText(mUser.getEmail());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().findFragmentById(R.id.container_home) != null) {
            super.onBackPressed();
            if (getSupportFragmentManager().findFragmentById(R.id.container_home) == null){
                finish();
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_map: {
                replaceFragment(GMapFragment.newInstance());
            }
            break;

            case R.id.nav_settings: {
                replaceFragment(SettingsFragment.newInstance());
            }
            break;

            case R.id.nav_about: {

            }
            break;

            case R.id.nav_log_out: {
                ProgressDialog mProgressDialog = new ProgressDialog(HomeActivity.this);
                mProgressDialog.show();
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent i = new Intent(this, LoginActivity.class);
                i.putExtra("log_out", false);
                startActivity(i);
                mProgressDialog.dismiss();
            }
            break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_home, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        invalidateOptionsMenu();
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        DownloadAvatar.downloadImageAndSetAvatar(mStorageAvatarRef, mNavDrawerAvatar);
        invalidateOptionsMenu();
    }

    @Override
    public void onDrawerClosed(View drawerView) {
    }

    @Override
    public void onDrawerStateChanged(int newState) {
    }


}

package com.example.student.userphotograph.activityes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.student.userphotograph.R;
import com.example.student.userphotograph.fragments.SignInFragment;
import com.example.student.userphotograph.fragments.SignInSignUpFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        boolean logOut = getIntent().getBooleanExtra("log_out", true);
        mauth = FirebaseAuth.getInstance();
        if (logOut) {
            replaceFragment(SignInSignUpFragment.newInstance());
        } else replaceFragment(SignInFragment.newInstance());
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerLogin, fragment)
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void signIn() {
        Button btnLogin = (Button) findViewById(R.id.sign_in_button);
        final EditText imputemail = (EditText) findViewById(R.id.et_sign_in_email);
        final EditText imputpassword = (EditText) findViewById(R.id.et_sign_in_password);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = imputemail.getText().toString();
                final String password1 = imputpassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password1)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                mauth.signInWithEmailAndPassword(email, password1)
                        .addOnCompleteListener(com.example.student.userphotograph.activityes.LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (!task.isSuccessful()) {
                                    if (imputpassword.length() < 6) {
                                        imputpassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(com.example.student.userphotograph.activityes.LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Intent intent = new Intent(com.example.student.userphotograph.activityes.LoginActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });

            }
        });
    }
}


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


//        mauth = FirebaseAuth.getInstance();
//        getSupportFragmentManager()
//                .beginTransaction()
//                .add(R.id.containerLogin, SignUpFragment.newInstance())
//                .commit();
//
//        TODO add first (signin signup) fragment;
//         getFragmmentManager().gggggggggggg, etc
//
//        TODO firebase login functionality.
//    }

//    private void signIn() {
//        Button btnLogin = (Button) findViewById(R.id.sign_in);
//        final EditText username = (EditText) findViewById(R.id.username_layout);
//        final EditText password = (EditText) findViewById(R.id.password_layout);
//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String email = username.getText().toString();
//                final String password1 = password.getText().toString();
//
//                if (TextUtils.isEmpty(email)) {
//                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (TextUtils.isEmpty(password1)) {
//                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                progressBar.setVisibility(View.VISIBLE);
//
//                //authenticate user
//                auth.signInWithEmailAndPassword(email, password1)
//                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                // If sign in fails, display a message to the user. If sign in succeeds
//                                // the auth state listener will be notified and logic to handle the
//                                // signed in user can be handled in the listener.
//                                progressBar.setVisibility(View.GONE);
//                                if (!task.isSuccessful()) {
//                                    // there was an error
//                                    if (password.length() < 6) {
//                                        inputPassword.setError(getString(R.string.minimum_password));
//                                    } else {
//                                        Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
//                                    }
//                                } else {
//                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//                                    startActivity(intent);
//                                    finish();
//                                }
//                            }
//                        });
//            }
//        });
//    }
}

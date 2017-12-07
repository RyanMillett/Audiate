package edu.orangecoastcollege.cs273.rmillett.audiate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    private DBHelper mDB;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private EditText mUserNameEditText;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;

    private TextView mLowPitchTextView;
    private TextView mHighPitchTextView;
    private TextView mVocalRangeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // This should allow the users to return to the LoginActivity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Sets up the views
        mUserNameEditText = (EditText) findViewById(R.id.userNameCreateEditText);
        mEmailEditText = (EditText) findViewById(R.id.emailCreateEditText);
        mPasswordEditText = (EditText) findViewById(R.id.passwordCreateEditText);

        mLowPitchTextView = (TextView) findViewById(R.id.lowPitchTextView);
        mHighPitchTextView = (TextView) findViewById(R.id.highPitchTextView);
        mVocalRangeTextView = (TextView) findViewById(R.id.vocalRangeTextView);


        mAuth = FirebaseAuth.getInstance();

        mUser = mAuth.getCurrentUser();

    }

    /**
     * This checks to make sure that the profile is complete.
     * It checks the userName, email, password, lowPitch, highPitch, and vocalRange.
     * @return
     */
    private boolean profileComplete()
    {
        boolean valid = true;

        if(TextUtils.isEmpty(mUserNameEditText.getText())) {
            mUserNameEditText.setError("Required.");
            valid = false;
        }

        if(TextUtils.isEmpty(mEmailEditText.getText())) {
            mEmailEditText.setError("Required.");
            valid = false;
        }

        if(TextUtils.isEmpty(mPasswordEditText.getText())) {
            mPasswordEditText.setError("Required.");
            valid = false;
        }

        if(TextUtils.isEmpty(mLowPitchTextView.getText()))
        {
            mLowPitchTextView.setError("Complete Detect Vocal Range.");
            valid = false;
        }

        if(TextUtils.isEmpty(mHighPitchTextView.getText()))
        {
            mHighPitchTextView.setError("Complete Detect Vocal Range.");
            valid = false;
        }

        if(TextUtils.isEmpty(mVocalRangeTextView.getText()))
        {
            mVocalRangeTextView.setError("Complete Detect Vocal Range.");
            valid = false;
        }

        return valid;
    }

    private void goToLogin()
    {
        finish();
        Intent launchLogin = new Intent(this, LoginActivity.class);
        startActivity(launchLogin);
    }


    private void createUser(String email, String password)
    {
        if(!profileComplete())
            return;

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(ProfileActivity.this, "Account created successfully. Please verify account in your email.", Toast.LENGTH_LONG).show();
                    mUser = mAuth.getCurrentUser();
                    mUser.sendEmailVerification();
                    goToLogin();
                }
                else
                {
                    Toast.makeText(ProfileActivity.this, "Account already exists. Please sign in, or use different user name.", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Error creating new account", task.getException());
                }
            }
        });

        // Adds the user to the database
        // User newUser = new User(userName, email, lowPitch, highPitch, vocalRange);
        // mDB.addUser(newUser);
        //Log.i(TAG, "Check if user name is bwegener from database = " + user.getUserName());
    }


    public void handleProfileButtons(View v)
    {
        switch(v.getId())
        {
            case R.id.confirmProfileButton:
                createUser(mEmailEditText.getText().toString(), mPasswordEditText.getText().toString());
                break;

            case R.id.detectVocalRangeButton:
                Intent launchDetectVocalRange = new Intent(this, DetectVocalRangeActivity.class);
                startActivity(launchDetectVocalRange);
                break;
        }
    }


    // If I am unable to get firebase to work, this should add a user to the database
    /*
    public void confirmProfile(View v)
    {
        if(TextUtils.isEmpty(mUserNameEditText.getText())) {
            mUserNameEditText.setError("Required.");
        }

        if(TextUtils.isEmpty(mEmailEditText.getText())) {
            mEmailEditText.setError("Required.");
        }

        if(TextUtils.isEmpty(mPasswordEditText.getText())) {
            mPasswordEditText.setError("Required.");
        }

        if(TextUtils.isEmpty(mLowPitchTextView.getText()))
        {
            mLowPitchTextView.setError("Complete Detect Vocal Range.");
        }

        if(TextUtils.isEmpty(mHighPitchTextView.getText()))
        {
            mHighPitchTextView.setError("Complete Detect Vocal Range.");
        }

        if(TextUtils.isEmpty(mVocalRangeTextView.getText()))
        {
            mVocalRangeTextView.setError("Complete Detect Vocal Range.");
        }

        if(TextUtils.isEmpty(mUserNameEditText.getText()) || TextUtils.isEmpty(mEmailEditText.getText())
                || TextUtils.isEmpty(mPasswordEditText.getText()) || TextUtils.isEmpty(mLowPitchTextView.getText())
                || TextUtils.isEmpty(mHighPitchTextView.getText()) || TextUtils.isEmpty(mVocalRangeTextView.getText()))
            return;
        else {
            User newUser = new User(mUserNameEditText.getText().toString(), mEmailEditText.getText().toString(),
                    mLowPitchTextView.getText().toString(), mHighPitchTextView.getText().toString(),
                    mVocalRangeTextView.getText().toString()));
            mDB.addUser(newUser);
            usersListAdapter.add(newUser);
            mUserNameEditText.setText("");
            mLowPitchTextView.setText("");
            mHighPitchTextView.setText("");
            mVocalRangeTextView.setText("");
            goToLogin();
        }
    }

    public void detectVocalRange(View v)
    {
        Intent launchDetectVocalRange = new Intent(this, DetectVocalRangeActivity.class);
        startActivity(launchDetectVocalRange);
    }
    */

}

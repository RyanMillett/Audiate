package edu.orangecoastcollege.cs273.rmillett.audiate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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

    private DBHelper mDBHelper;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private EditText userNameEditText;
    private EditText passwordEditText;

    private TextView lowPitchTextView;
    private TextView highPitchTextView;
    private TextView vocalRangeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // This should allow the users to return to the
        // LoginActivity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        if(TextUtils.isEmpty(userNameEditText.getText())) {
            userNameEditText.setError("Required.");
            valid = false;
        }

        if(TextUtils.isEmpty(passwordEditText.getText())) {
            passwordEditText.setError("Required.");
            valid = false;
        }

        if(TextUtils.isEmpty(lowPitchTextView.getText()))
        {
            lowPitchTextView.setError("Complete Detect Vocal Range.");
            valid = false;
        }

        if(TextUtils.isEmpty(highPitchTextView.getText()))
        {
            highPitchTextView.setError("Complete Detect Vocal Range.");
            valid = false;
        }

        if(TextUtils.isEmpty(vocalRangeTextView.getText()))
        {
            vocalRangeTextView.setError("Complete Detect Vocal Range.");
            valid = false;
        }
        return valid;
    }

    public void handleProfileButtons(View v)
    {
        switch(v.getId())
        {
            case R.id.confirmProfileButton:
                createUser(userNameEditText.getText().toString(), passwordEditText.getText().toString(),
                        lowPitchTextView.getText().toString(), highPitchTextView.getText().toString(), vocalRangeTextView.getText().toString());
                goToMain();
                break;

            case R.id.detectVocalRangeButton:
                Intent launchDetectVocalRange = new Intent(this, DetectVocalRangeActivity.class);
                startActivity(launchDetectVocalRange);
                break;
        }
    }

    public void createUser(String userName, String password, String lowPitch, String highPitch, String vocalRange)
    {
        if(!profileComplete())
            return;

        mAuth.createUserWithEmailAndPassword(userName, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(ProfileActivity.this, "Account created successfully.", Toast.LENGTH_LONG).show();
                    mUser = mAuth.getCurrentUser();
                }
                else
                {
                    Toast.makeText(ProfileActivity.this, "Account already exists. Please sign in, or use different user name.", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Adds the user to the database
        mDBHelper.addUser(new User(userName, lowPitch, highPitch, vocalRange));
    }


    private void goToMain()
    {
        finish();

        Intent launchMainMenu = new Intent(this, MainMenuActivity.class);
        startActivity(launchMainMenu);
    }

}

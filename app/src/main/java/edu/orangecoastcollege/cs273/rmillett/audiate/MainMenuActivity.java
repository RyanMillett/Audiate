package edu.orangecoastcollege.cs273.rmillett.audiate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainMenuActivity extends AppCompatActivity {

    User user;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        TextView welcomeTextView = (TextView) findViewById(R.id.welcomeTextView);

//        welcomeTextView.setText(getString(R.string.welcome_message, user.getUserName()));

    }

    public void activitySelectionHandler(View view) {
        // create Intent
        Intent activityIntent;

        // determine selected button
        switch (view.getId()) {
            case R.id.libraryButton:
                activityIntent = new Intent(this, LibraryActivity.class);
                break;
            default:
                activityIntent = new Intent(this, ExerciseSelectionMenuActivity.class);
                break;
        }

        // Launch activity
        startActivity(activityIntent);
    }

    public void editProfile(View view) {
        // TODO: this method
    }

    public void logOut(View view) {
        mAuth.signOut();
        finish();
        Intent logInIntent = new Intent(this, LogInActivity.class);
        startActivity(logInIntent);
    }
}

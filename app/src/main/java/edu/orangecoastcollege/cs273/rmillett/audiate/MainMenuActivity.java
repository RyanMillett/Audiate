package edu.orangecoastcollege.cs273.rmillett.audiate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MainMenuActivity extends AppCompatActivity {

    private static final String TAG = "MainMenuActivity";


    private DBHelper db;

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

        db = new DBHelper(this);

        List<User> users = new ArrayList<>(db.getAllUsers());
        for(User u : users)
        {
            Log.i(TAG, "Users in database :\nuser_name: " + u.getUserName() + "\nemail: "
            + u.getEmail() + "\nlow_pitch: " + u.getLowPitch() + "\nhigh_pitch: " + u.getHighPitch()
            + "\nvocal_range: " + u.getVocalRange());
        }


        // Should display a welcome message to the user at the main menu
        //welcomeTextView.setText(getString(R.string.welcome_message, user.getUserName()));


        // If I have to go with alternate LoginActivity
        // User selectedUser = getIntent().getExtras().getParcelable("SelectedUser");
        // welcomeTextView.setText(getString(R.string.welcome_message, selectedUser.getUserName()));


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
        Intent logInIntent = new Intent(this, LoginActivity.class);
        startActivity(logInIntent);
    }
}

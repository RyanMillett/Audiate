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

/**
 * The <code>MainMenuActivity</code> allows the user to choose
 * between different activities as well as edit their profile(s) or logout.
 */
public class MainMenuActivity extends AppCompatActivity {

    private static final String TAG = "MainMenuActivity";

    private DBHelper db;

    User user;

    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;

    private TextView welcomeTextView;

    /**
     * This sets up the activity with the database and gets the current user.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        // Get this to work later
        // User user = getIntent().getExtras().getParcelable("user");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        db = new DBHelper(this);

        String email = mFirebaseUser.getEmail();
        String displayName = mFirebaseUser.getDisplayName();
        List<User> users = new ArrayList<>(db.getAllUsers());
        for (User u : users) {
            Log.i(TAG, "Users in database :\nuser_name: " + u.getUserName() + "\nemail: "
                    + u.getEmail() + "\nlow_pitch: " + u.getLowPitch() + "\nhigh_pitch: " + u.getHighPitch()
                    + "\nvocal_range: " + u.getVocalRange());
        }
        user = db.getUser(email);


        welcomeTextView = (TextView) findViewById(R.id.welcomeTextView);







        // Should display a welcome message to the user at the main menu
        welcomeTextView.setText(getString(R.string.welcome_message, user.getUserName()));


        // If I have to go with alternate LoginActivity
        // User selectedUser = getIntent().getExtras().getParcelable("SelectedUser");
        // welcomeTextView.setText(getString(R.string.welcome_message, selectedUser.getUserName()));
    }

    /**
     * This handles which activity is selected.
     * This handles all the buttons on the main menu activity layout.
     * it sends them to different places.
     *
     * @param view
     */
    public void activitySelectionHandler(View view) {
        // create Intent
        Intent activityIntent;

        // determine selected button
        switch (view.getId()) {
            case R.id.libraryButton:
                activityIntent = new Intent(this, LibraryActivity.class);
                break;
            case R.id.logoutButton:
                mAuth.signOut();
                finish();
                activityIntent = new Intent(this, LoginActivity.class);
                break;
            default:
                activityIntent = new Intent(this, ExerciseBuilderActivity.class);
                break;
        }

        startActivity(activityIntent);
    }
}

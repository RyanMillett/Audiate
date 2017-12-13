package edu.orangecoastcollege.cs273.rmillett.audiate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * The <code>LoginActivity</code> allows the user to either log in
 * or create an account with a profile.
 *
 * @author bwegener
 * @version 1.0
 *          <p>
 *          Created by Brian Wegener on 11/25/2017
 */
public class
LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    // Animation = used for tween(ed) animations
    private Animation alphaAnim;

    // Connects views (texts/buttons) to the LoginActivity
    private EditText mEmailLoginEditText;
    private EditText mPasswordLoginEditText;
    private Button LoginButton;
    private Button createProfileButton;
    private ImageView googleMaps;

    // Connects Firebase to the app
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private User user;

    /**
     * The <code>onCreate</code> sets up the authorization of the user from firebase.
     * It also instantiates the editTexts and buttons. It also launches the animation
     * that brings the views in.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        // Views
        mEmailLoginEditText = (EditText) findViewById(R.id.emailLoginET);
        mPasswordLoginEditText = (EditText) findViewById(R.id.passwordLoginET);
        LoginButton = (Button) findViewById(R.id.loginButton);
        createProfileButton = (Button) findViewById(R.id.createProfileButton);
        googleMaps = (ImageView) findViewById(R.id.googleMaps);

        // Initializes Firebase authentication
        mAuth = FirebaseAuth.getInstance();

        // Gets the current user
        mUser = mAuth.getCurrentUser();

        // Runs the animation that brings the Views in
        RunAnimation();

        // ASK MICHAEL
        Bundle profileBundle = getIntent().getExtras();
        if (profileBundle != null)
            user = profileBundle.getParcelable("CurrentUser");

        // Log.i(TAG, "This is the current user name: " + user.getUserName());
    }

    /**
     * This sends the user to the main menu activity.
     */
    private void goToMain() {
        finish();
        Intent mainMenuIntent = new Intent(this, MainMenuActivity.class);
        mainMenuIntent.putExtra("CurrentUser", user);
        startActivity(mainMenuIntent);
    }


    /**
     * This checks to see if the input is valid.
     * If it there is nothing in the edit texts it will send a message
     * letting the user know that the information is required.
     *
     * @return
     */
    private boolean isValidInput() {
        boolean valid = true;
        if (TextUtils.isEmpty(mEmailLoginEditText.getText())) {
            mEmailLoginEditText.setError("Required.");
            valid = false;
        }
        if (TextUtils.isEmpty(mPasswordLoginEditText.getText())) {
            mPasswordLoginEditText.setError("Required.");
            valid = false;
        }
        return valid;
    }

    /**
     * This allows the user to log in.
     *
     * @param email
     * @param password
     */
    private void Login(String email, String password) {
        // If user is already signed in go straight to main activity
        // Might move this back to the onCreate
        /*
        if (mUser != null)
            goToMain();
            */

        if (!isValidInput())
            return;
        else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        mUser = mAuth.getCurrentUser();
                        goToMain();
                    } else {
                        Toast.makeText(LoginActivity.this, "Sign in failed. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    /**
     * This handles both the createProfile button and the logIn button.
     * Through a switch statement it handles what happens when each of the buttons are pressed.
     *
     * @param v
     */
    public void handleLoginButtons(View v) {
        switch (v.getId()) {
            case R.id.createProfileButton:
                Intent launchProfile = new Intent(this, ProfileActivity.class);
                startActivity(launchProfile);
                break;

            case R.id.loginButton:
                Login(mEmailLoginEditText.getText().toString(), mPasswordLoginEditText.getText().toString());
                break;
        }
    }


    /**
     * Launches the GoogleMaps Activity
     *
     * @param view
     */
    public void activityGoogleMaps(View view) {
        Intent launchGoogleMaps = new Intent(this, GoogleMapsActivity.class);
        startActivity(launchGoogleMaps);
    }


    /**
     * This runs an alpha tween animation so that the edit texts, buttons, and
     * image view are brought in through changing the opacity.
     */
    private void RunAnimation() {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.anim);
        a.reset();
        mEmailLoginEditText.clearAnimation();
        mEmailLoginEditText.startAnimation(a);
        mPasswordLoginEditText.clearAnimation();
        mPasswordLoginEditText.startAnimation(a);
        LoginButton.clearAnimation();
        LoginButton.startAnimation(a);
        createProfileButton.clearAnimation();
        createProfileButton.startAnimation(a);
        googleMaps.clearAnimation();
        googleMaps.startAnimation(a);
    }
}

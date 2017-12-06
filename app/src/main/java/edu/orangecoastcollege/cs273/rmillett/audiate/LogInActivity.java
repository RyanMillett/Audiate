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
 *
 * Created by Brian Wegener on 11/25/2017
 */
public class LogInActivity extends AppCompatActivity {


    // Animation = used for tween(ed) animations
    private Animation alphaAnim;

    // Connects views (texts/buttons) to the LogInActivity
    private EditText emailLogInEditText;
    private EditText passwordLogInEditText;
    private Button signInButton;
    private Button newUserButton;
    private ImageView googleMaps;

    // Connects Firebase to the app
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;


    /**
     * The <code>onCreate</code> sets up the authorization of the user from firebase.
     * It also instantiates the editTexts and buttons. It also launches the animation
     * that brings the edit texts and buttons in.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        // Initializes Firebase authentication
        mAuth = FirebaseAuth.getInstance();

        // Gets the current user
        mUser = mAuth.getCurrentUser();

        emailLogInEditText = (EditText) findViewById(R.id.userNameLogInET);
        passwordLogInEditText = (EditText) findViewById(R.id.passwordLogInET);
        signInButton = (Button) findViewById(R.id.logInButton);
        newUserButton = (Button) findViewById(R.id.createProfileButton);
        googleMaps = (ImageView) findViewById(R.id.googleMaps);

        RunAnimation();

        if (mUser != null)
            goToMain();
    }


    /**
     * This runs an alpha tween animation so that the edit texts, buttons, and
     * image view are brought in through changing the opacity.
     */
    private void RunAnimation()
    {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.anim);
        a.reset();
        emailLogInEditText.clearAnimation();
        emailLogInEditText.startAnimation(a);
        passwordLogInEditText.clearAnimation();
        passwordLogInEditText.startAnimation(a);
        signInButton.clearAnimation();
        signInButton.startAnimation(a);
        newUserButton.clearAnimation();
        newUserButton.startAnimation(a);
        googleMaps.clearAnimation();
        googleMaps.startAnimation(a);
    }

    /**
     * This checks to see if the input is valid.
     * If it there is nothing in the edit texts it will send a message
     * letting the user know that the information is required.
     * @return
     */
    private boolean isValidInput() {
        boolean valid = true;
        if (TextUtils.isEmpty(emailLogInEditText.getText())) {
            emailLogInEditText.setError("Required.");
            valid = false;
        }
        if (TextUtils.isEmpty(passwordLogInEditText.getText())) {
            passwordLogInEditText.setError("Required.");
            valid = false;
        }
        return valid;
    }

    /**
     * This allows the user to log in.
     * @param email
     * @param password
     */
    private void LogIn(String email, String password)
    {
        if(!isValidInput())
            return;
        else
        {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        mUser = mAuth.getCurrentUser();
                        goToMain();
                    }
                    else {
                        Toast.makeText(LogInActivity.this, "Sign in failed. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }

    /**
     * This handles both the createProfile button and the logIn button.
     * Through a switch statement it handles what happens when each of the buttons are pressed.
     * @param v
     */
    public void handleLoginButtons(View v)
    {
        switch(v.getId())
        {
            case R.id.createProfileButton:
                Intent launchProfile = new Intent(this, ProfileActivity.class);
                startActivity(launchProfile);
                break;

            case R.id.logInButton:
                LogIn(emailLogInEditText.getText().toString(), passwordLogInEditText.getText().toString());
                break;
        }
    }

    /**
     * This sends the user to the main menu activity.
     */
    private void goToMain()
    {
        finish();
        Intent mainMenuIntent = new Intent(this, MainMenuActivity.class);
        startActivity(mainMenuIntent);
    }

    /**
     * Launches the GoogleMaps Activity
     * @param view
     */
    public void activityGoogleMaps(View view) {
        Intent launchGoogleMaps = new Intent(this, GoogleMapsActivity.class);
        startActivity(launchGoogleMaps);
    }
}

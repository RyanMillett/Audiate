package edu.orangecoastcollege.cs273.rmillett.audiate;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

public class LogInActivity extends AppCompatActivity {

    // AnimationDrawable = used for frame animations
    private AnimationDrawable frameAnim;

    // Animation = used for tween(ed) animations
    private Animation alphaAnim;

    private EditText userNameLogIn;
    private EditText passwordLogIn;
    private Button signInButton;
    private Button newUserButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        userNameLogIn = (EditText) findViewById(R.id.userNameLogInET);
        passwordLogIn = (EditText) findViewById(R.id.passwordLogInET);
        signInButton = (Button) findViewById(R.id.signInButton);
        newUserButton = (Button) findViewById(R.id.newUserButton);

        RunAnimation();


    }


    private void RunAnimation()
    {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.anim);
        a.reset();
        userNameLogIn.clearAnimation();
        userNameLogIn.startAnimation(a);
        passwordLogIn.clearAnimation();
        passwordLogIn.startAnimation(a);
        signInButton.clearAnimation();
        signInButton.startAnimation(a);
        newUserButton.clearAnimation();
        newUserButton.startAnimation(a);
    }



    public void createProfile(View v)
    {
        Intent launchProfile = new Intent(this, ProfileActivity.class);

        startActivity(launchProfile);
    }

    public void LogIn(View v)
    {
        /*
        Check to see if user has logged in before
         */
        Intent launchLogIn = new Intent(this, MainMenuActivity.class);

        startActivity(launchLogIn);
    }

    public void googleMaps(View v)
    {
        Intent launchGoogleMaps = new Intent(this, GoogleMapsActivity.class);

        startActivity(launchGoogleMaps);
    }
}

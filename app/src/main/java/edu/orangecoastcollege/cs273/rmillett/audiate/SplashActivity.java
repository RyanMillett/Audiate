package edu.orangecoastcollege.cs273.rmillett.audiate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The <code>SplashActivity</code> displays the name of the app through means
 * of an alpha animation, before sending the user to the log in activity.
 *
 * @author bwegener
 * @version 1.0
 *          <p>
 *          Created by Brian Wegener 11/28/2017
 */
public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SpashActivity";

    private Animation alphaAnim;

    private TextView audiateTextView;

    /**
     * The <code>onCreate</code> calls the TextView and runs an animation
     * as well as handling a timer task so that the user stays on the splash
     * activity for a couple seconds.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        audiateTextView = (TextView) findViewById(R.id.audiateTextView);

        RunAnimation();


        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                finish();

                Intent logInIntent = new Intent(SplashActivity.this, MainMenuActivity.class);

                startActivity(logInIntent);

            }
        };

        Timer timer = new Timer();
        timer.schedule(task, 3 * 1000);
    }

    /**
     * This runs an alpha animation so that the text slowly animates in.
     */
    private void RunAnimation() {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.anim);
        a.reset();
        audiateTextView.clearAnimation();
        audiateTextView.startAnimation(a);
    }
}

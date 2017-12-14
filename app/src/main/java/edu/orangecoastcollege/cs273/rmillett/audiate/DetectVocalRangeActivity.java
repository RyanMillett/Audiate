package edu.orangecoastcollege.cs273.rmillett.audiate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class DetectVocalRangeActivity extends AppCompatActivity {

    private static final String TAG = "DetectPitchActivity";

    PitchDetector pitchDetector;

    private Button detectHighButton;
    private Button detectLowButton;

    private Button confirmButton;

    private String toastText = "";
    private String detectedPitch = "";

    private String highPitch;
    private String lowPitch;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect_vocal_range);

        pitchDetector = new PitchDetector(this, this);

        ImageView pitchDetectImageView = findViewById(R.id.vocalRangeResultsImageView);

        detectHighButton = findViewById(R.id.detectHighNoteButton);
        detectLowButton = findViewById(R.id.detectLowNoteButton);

        confirmButton = findViewById(R.id.confirmVocalRangeButton);
        confirmButton.setVisibility(View.INVISIBLE);

        handler = new Handler();

        highPitch = "";
        lowPitch = "";

        pitchDetector.activatePitchDetection();
    }

    /**
     *
     *
     * @param view
     */
    public void detectPitchHandler(final View view) {

        Button selectedButton = (Button) view;

        //disableDetectionButtons();

        // determine which button was pressed
        switch (view.getId()) {
            case R.id.detectHighNoteButton:
                toastText = "highest";
                break;
            case R.id.detectLowNoteButton:
                toastText = "lowest";
                break;
        }

        // instructional toast
        Toast instructionToast = Toast.makeText(this, "Sing your " + toastText + " note!", Toast.LENGTH_LONG);
        instructionToast.setGravity(Gravity.CENTER,0,0);
        instructionToast.show();

        // TODO: change view to "listening mode"

        // start listening by resetting collections
        pitchDetector.resetCollections();
        //selectedButton.setText("Listening...");

        // delay while pitch detector is listening...
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                double freqAvg = pitchDetector.getFrequencyAverage();
                Log.i(TAG, "Frequency to parse->" + freqAvg);
                detectedPitch = pitchDetector.parsePitchFromFreqAvg(freqAvg);

                Toast resultToast = Toast.makeText(DetectVocalRangeActivity.this,
                        "Your " + toastText + " note is " + detectedPitch, Toast.LENGTH_LONG);
                resultToast.setGravity(Gravity.CENTER, 0, 0);
                resultToast.show();

                // TODO: change view back to resting mode

                // save value into user profile
                switch (toastText) {
                    case "highest":
                        // set user highest note
                        highPitch = detectedPitch;
                        break;
                    case "lowest":
                        // set user lowest note
                        lowPitch = detectedPitch;
                        break;
                }

                if (!lowPitch.isEmpty() && !highPitch.isEmpty()) {
                    confirmButton.setVisibility(View.VISIBLE);
                }

                enableDetectionButtons();
            }
        }, 5 * 1000);
    }

    /**
     *
     *
     * @param view
     */
    public void confirmVocalRange(View view) {
        // once both high- and low-notes have been detected, save to user profile and leave the activity
                                        // TODO: go to confirm screen, not profile
        Intent launchProfile = new Intent(this, ProfileActivity.class);
        launchProfile.putExtra("HighPitch", highPitch);
        launchProfile.putExtra("LowPitch", lowPitch);
        launchProfile.putExtra("VocalRange", lowPitch + " - " + highPitch);

        startActivity(launchProfile);
    }

    private void disableDetectionButtons() {
        detectHighButton.setEnabled(false);
        detectLowButton.setEnabled(false);
    }

    private void enableDetectionButtons() {
        detectHighButton.setEnabled(true);
        detectLowButton.setEnabled(true);
    }
}

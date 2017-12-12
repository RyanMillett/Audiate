package edu.orangecoastcollege.cs273.rmillett.audiate;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetectVocalRangeActivity extends AppCompatActivity {

    PitchDetector pitchDetector;

    private double mHighNoteFreqHz;
    private double mLowNoteFreqHz;

    private double[] mNoteFreqs; // collect held note variance to calculate average
    private double mFreqAvg; // average frequency based on values in mNoteFreqs array

    TextView lowNoteTextView;
    TextView highNoteTextView;

    private Button detectHighButton;
    private Button detectLowButton;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect_vocal_range);

        pitchDetector = new PitchDetector(this, this);

        ImageView pitchDetectImageView = findViewById(R.id.vocalRangeResultsImageView);
        highNoteTextView = findViewById(R.id.highNoteResultTextView);
        lowNoteTextView = findViewById(R.id.lowNoteResultTextView);

        detectHighButton = findViewById(R.id.detectHighNoteButton);
        detectLowButton = findViewById(R.id.detectLowNoteButton);

        pitchDetector.activatePitchDetection();

        highNoteTextView.setText("");
        lowNoteTextView.setText("");

        handler = new Handler();
    }

    /**
     *
     *
     * @param view
     */
    public void detectPitchHandler(View view) {


        Toast.makeText(this, "Sing your " + "" + "note", Toast.LENGTH_LONG).show();

        // reset collections
        pitchDetector.resetCollections();


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                double freqAvg = pitchDetector.getFrequencyAverage();
                Log.i("PD", "Frequency to parse->" + freqAvg);
                highNoteTextView.setText(pitchDetector.parsePitchFromFreqAvg(freqAvg));
            }
        }, SoundObjectPlayer.DEFAULT_SAMPLE_RATE);




        // save and display results
        // RELEASE: revert button
        // determine which button is selected (high/low)
    }

    /**
     *
     *
     * @param view
     */
    public void confirmVocalRange(View view) {
        // TODO: this method
        // button disabled unless both high/low fields are not null
        // take results, update current user profile
        // Are you sure? Y/N pop-up or something
        // launch View Profile activity to confirm results
    }
}

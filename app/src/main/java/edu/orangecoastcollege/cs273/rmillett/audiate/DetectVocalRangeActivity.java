package edu.orangecoastcollege.cs273.rmillett.audiate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DetectVocalRangeActivity extends AppCompatActivity {

    private double mHighNoteFreqHz;
    private double mLowNoteFreqHz;

    private double[] mNoteFreqs; // collect held note variance to calculate average
    private double mFreqAvg; // average frequency based on values in mNoteFreqs array

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect_vocal_range);

        PitchDetector pitchDetector = new PitchDetector(this, this);

        ImageView pitchDetectImageView = findViewById(R.id.vocalRangeResultsImageView);
        TextView highNoteTextView = findViewById(R.id.highNoteResultTextView);
        TextView lowNoteTextView = findViewById(R.id.lowNoteResultTextView);

        pitchDetector.activatePitchDetection();

        highNoteTextView.setText("");
        lowNoteTextView.setText("");
    }

    /**
     *
     *
     * @param view
     */
    public void detectPitchHandler(View view) {
        // TODO: this method
        // determine which button is selected (high/low)
            // LONGPRESS: show text view, change button
                // save and display results
            // RELEASE: revert button
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

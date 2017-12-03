package edu.orangecoastcollege.cs273.rmillett.audiate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetectVocalRangeActivity extends AppCompatActivity {

    private PitchDetector mPitchDetector;

    private TextView mPitchClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect_vocal_range);

        mPitchDetector = new PitchDetector(this, this);
    }
}

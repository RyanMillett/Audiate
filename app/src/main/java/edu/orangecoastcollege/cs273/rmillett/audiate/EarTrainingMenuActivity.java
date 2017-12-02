package edu.orangecoastcollege.cs273.rmillett.audiate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class EarTrainingMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ear_training_menu);

        // This should create the return button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void selectionHandler(View view) {
        // TODO: this method
        // displays exercise options in a ListView based on selected button (intervals/chords/modeScales)
    }

    public void startActivity(View view) {
        // TODO: this method
        // starts an ear training quiz activity based on selection
        // button is enabled only if a quiz type and option is selected
    }

    public void help(View view) {
        // TODO: this method
        // displays a toast or launches text activity with help information
    }
}

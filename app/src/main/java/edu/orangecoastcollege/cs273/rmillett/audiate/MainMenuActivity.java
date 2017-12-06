package edu.orangecoastcollege.cs273.rmillett.audiate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainMenuActivity extends AppCompatActivity {

    private DBHelper mDBHelper;

    private List<ChordScale> mAllIntervalsList;
    private List<ChordScale> mAllChordsList;
    private List<ChordScale> mAllScalesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        deleteDatabase(DBHelper.DATABASE_NAME);
        mDBHelper = new DBHelper(this);
        mDBHelper.importIntervalsFromCSV("intervals.csv");

        mAllIntervalsList = mDBHelper.getAllIntervals();
        mAllChordsList = new ArrayList<>(4); // TODO: get all chords
        mAllScalesList = mDBHelper.importScalesFromSCL();

    }

    public void activitySelectionHandler(View view) {
        // Intent
        Intent activityIntent;

        // get selected button
        Button selectedButton = (Button) view;

        // determine selected button
        switch (view.getId()) {
            case R.id.libraryButton:
                activityIntent = new Intent(this, LibraryActivity.class);
                break;
            default:
                activityIntent = new Intent(this, ExerciseSelectionMenuActivity.class);
        }

        // Load ChordScaleLibraries
        activityIntent.putExtra("AllIntervalsList", mAllIntervalsList.toArray());
        activityIntent.putExtra("AllChordsList", mAllChordsList.toArray());
        activityIntent.putExtra("AllScalesList", mAllScalesList.toArray());

        // Launch activity
        startActivity(activityIntent);
    }

    public void editProfile(View view) {
        // TODO: this method
    }

    public void logOut(View view) {
        // TODO: this method
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

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

    private SoundObjectLibrary mMasterChordScaleLibrary;
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

//        mMasterChordScaleLibrary = new SoundObjectLibrary("MasterLibrary", 3);
//        // load lists into SoundObjectLibrary
//        mMasterChordScaleLibrary.addList(mAllIntervalsList);
//        mMasterChordScaleLibrary.addList(mAllChordsList);
//        mMasterChordScaleLibrary.addList(mAllScalesList);

    }

    public void activitySelectionHandler(View view) {
        // TODO: finish this method

        // Intent
        Intent intent;

        // Bundle
        Bundle bundle = new Bundle();

        // get selected button
        Button selectedButton = (Button) view;

        // determine selected button
        if (selectedButton == findViewById(R.id.earTrainingButton)) {
            intent = new Intent(this, ExerciseSelectionMenuActivity.class);
            // TODO: put extra
        }
        else if (selectedButton == findViewById(R.id.sightSingingButton)) {
            intent = new Intent(this, ExerciseSelectionMenuActivity.class);
            // TODO: put extra
        }
        else {
            intent = new Intent(this, LibraryActivity.class);
            // TODO: put extra
        }
        startActivity(intent);
    }

    public void editProfile(View view) {
        // TODO: this method
    }

    public void logOut(View view) {
        // TODO: this method
    }
}

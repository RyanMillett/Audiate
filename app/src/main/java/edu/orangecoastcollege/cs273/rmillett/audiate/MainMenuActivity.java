package edu.orangecoastcollege.cs273.rmillett.audiate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.List;

public class MainMenuActivity extends AppCompatActivity {

    private DBHelper mDBHelper;
    private List<ChordScale> allScalesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        deleteDatabase(DBHelper.DATABASE_NAME);
        mDBHelper = new DBHelper(this);
        allScalesList = mDBHelper.importScalesFromSCL();

    }

    public void earTraining(View view) {
        // TODO: this method
    }

    public void sightSinging(View view) {
        // TODO: this method
    }

    public void soundLibrary(View view) {
        // TODO: this method
    }

    public void editProfile(View view) {
        // TODO: this method
    }

    public void logOut(View view) {
        // TODO: this method
    }

}

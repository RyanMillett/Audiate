package edu.orangecoastcollege.cs273.rmillett.audiate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

    }

    public void activitySelectionHandler(View view) {
        // TODO: finish this method

        // Intent
        Intent intent;

        // get selected button
        Button selectedButton = (Button) view;

        // determine selected button
        if (selectedButton == findViewById(R.id.earTrainingButton)) {
            intent = new Intent(this, ExerciseSelectionMenuActivity.class);
            // put extra
            startActivity(intent);
        }
        else if (selectedButton == findViewById(R.id.sightSingingButton)) {
            intent = new Intent(this, ExerciseSelectionMenuActivity.class);
            // put extra
            startActivity(intent);
        }
        else if (selectedButton == findViewById(R.id.libraryButton)) {
            intent = new Intent(this, LibraryActivity.class);
            // put extra
            startActivity(intent);
        }
    }

    public void editProfile(View view) {
        // TODO: this method
    }

    public void logOut(View view) {
        // TODO: this method
    }

}

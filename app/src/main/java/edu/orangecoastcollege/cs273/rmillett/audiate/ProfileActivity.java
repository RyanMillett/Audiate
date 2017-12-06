package edu.orangecoastcollege.cs273.rmillett.audiate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // This should allow the users to return to the
        // LogInActivity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public void createUser(View v)
    {
        // This will allow the user to be added to the database
        // once the user table has been added to the database
        // db.addUser(userName, firstName, lastName, email, password)
        // Needs to check to make sure the user has not already been created
        Intent launchMainMenu = new Intent(this, MainMenuActivity.class);

        startActivity(launchMainMenu);
    }

    public void cancel(View v)
    {
        Intent launchLogIn = new Intent(this, LogInActivity.class);

        startActivity(launchLogIn);
    }
}

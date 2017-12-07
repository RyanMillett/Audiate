package edu.orangecoastcollege.cs273.rmillett.audiate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.List;

public class AlternateLoginActivity extends AppCompatActivity {

    private DBHelper mDB;
    private List<User> usersList;
    private UserListAdapter usersListAdapter;
    private ListView usersListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alternate_login);

        mDB = new DBHelper(this);

        usersList = mDB.getAllUsers();
        usersListAdapter = new UserListAdapter(this, R.layout.user_list_item, usersList);
        usersListView = (ListView) findViewById(R.id.usersListView);
        usersListView.setAdapter(usersListAdapter);
    }

    public void chooseUser(View v)
    {
        if(v instanceof LinearLayout) {
            LinearLayout selectedLayout = (LinearLayout) v;
            User selectedUser = (User) selectedLayout.getTag();
            Log.i("Audiate", selectedUser.toString());
            Intent mainMenuIntent = new Intent(this, MainMenuActivity.class);
            mainMenuIntent.putExtra("SelectedUser", selectedUser);
            startActivity(mainMenuIntent);
        }
    }

    public void clearAllUsers(View v)
    {
        usersList.clear();
        mDB.deleteAllUsers();
        usersListAdapter.notifyDataSetChanged();
    }

    // Launches the profile where the user creates a profile
    public void createProfile(View v)
    {
        finish();
        Intent launchProfile = new Intent(this, ProfileActivity.class);
        startActivity(launchProfile);
    }
}

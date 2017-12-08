package edu.orangecoastcollege.cs273.rmillett.audiate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.List;

/**
 * This <code>AlternateLoginActivity</code> was created in
 * case Firebase did not work.
 * This activity displays a list of users that can be clicked
 * and then go to the main menu. There is no password or way
 * to keep a user from choosing another user's username/vocalrange
 *
 * @author bwegener
 * @version 1.0
 *
 * Created by Brian Wegener on 12/6/2017
 */
public class AlternateLoginActivity extends AppCompatActivity {

    private DBHelper mDB;
    private List<User> usersList;
    private UserListAdapter usersListAdapter;
    private ListView usersListView;

    /**
     * The <code>onCreate</code> instantiates the database, the list of users,
     * the userListAdapter, and the ListView of the users.
     * @param savedInstanceState
     */
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

    /**
     * This allows the current user of the app to choose a user from the list of users.
     * @param v
     */
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

    /**
     * This clearsAllUsers from the list and database.
     * @param v
     */
    public void clearAllUsers(View v)
    {
        usersList.clear();
        mDB.deleteAllUsers();
        usersListAdapter.notifyDataSetChanged();
    }

    /**
     * This launches the profileActivity so that the user can create
     * a profile
     * @param v
     */
    public void createProfile(View v)
    {
        finish();
        Intent launchProfile = new Intent(this, ProfileActivity.class);
        startActivity(launchProfile);
    }
}

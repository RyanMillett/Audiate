package edu.orangecoastcollege.cs273.rmillett.audiate;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brian Wegener on 12/6/17.
 */

public class UserListAdapter extends ArrayAdapter<User> {

    private Context mContext;
    private List<User> mUsersList = new ArrayList<>();
    private int mResourceId;

    public UserListAdapter(Context c, int rId, List<User> users)
    {
        super(c, rId, users);
        mContext = c;
        mResourceId = rId;
        mUsersList = users;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent)
    {
        final User selectedUser = mUsersList.get(pos);

        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(mResourceId, null);

        LinearLayout userListLinearLayout =
                (LinearLayout) view.findViewById(R.id.userListLinearLayout);

        TextView userListUserNameTextView =
                (TextView) view.findViewById(R.id.userNameTextView);

        TextView userListVocalRangeTextView =
                (TextView) view.findViewById(R.id.vocalRangeTextView);

        userListLinearLayout.setTag(selectedUser);
        userListUserNameTextView.setText(selectedUser.getUserName());
        userListVocalRangeTextView.setText(selectedUser.getVocalRange());

        return view;
    }
}
